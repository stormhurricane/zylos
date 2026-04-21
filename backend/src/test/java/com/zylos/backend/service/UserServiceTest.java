package com.zylos.backend.service;

import com.zylos.backend.model.dto.AuthResponse;
import com.zylos.backend.model.dto.LoginRequest;
import com.zylos.backend.model.dto.ProfileResponse;
import com.zylos.backend.model.dto.StudentRegistrationRequest;
import com.zylos.backend.model.entity.Student;
import com.zylos.backend.model.entity.Teacher;
import com.zylos.backend.repository.StudentRepository;
import com.zylos.backend.repository.UserRepository;
import com.zylos.backend.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private StudentRepository studentRepository;
    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserService userService;

    @Test
    void registerStudent_ShouldGenerateCorrectMatriculationNumber() {
        // Given
        StudentRegistrationRequest request = new StudentRegistrationRequest(
                "Max", "Mustermann", "password", "max@test.de", null, "Address", "IT"
        );
        
        when(studentRepository.findMaxMatriculationNumber()).thenReturn(Optional.of("1000005"));
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");

        // When
        userService.registerStudent(request);

        // Then
        ArgumentCaptor<Student> studentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(studentCaptor.capture());
        verify(userRepository).findByEmail(request.email()); // Überprüfe den Aufruf für die E-Mail-Einzigartigkeit
        
        Student savedStudent = studentCaptor.getValue();
        assertEquals("1000006", savedStudent.getMatriculationNumber());
        assertEquals("hashedPassword", savedStudent.getPassword());
        assertEquals("Max", savedStudent.getFirstName());
    }

    @Test
    void registerStudent_WhenNoPreviousStudentExists_ShouldStartWithDefaultNumber() {
        // Given
        StudentRegistrationRequest request = new StudentRegistrationRequest(
                "Max", "Mustermann", "password", "max@test.de", null, "Address", "IT"
        );

        // Simulieren einer leeren Tabelle
        when(studentRepository.findMaxMatriculationNumber()).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");

        // When
        userService.registerStudent(request);

        // Then
        ArgumentCaptor<Student> studentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(studentCaptor.capture());
        verify(userRepository).findByEmail(request.email()); // Überprüfe den Aufruf für die E-Mail-Einzigartigkeit
        assertEquals("1000000", studentCaptor.getValue().getMatriculationNumber());
    }

    @Test
    void registerStudent_WhenEmailAlreadyInUse_ShouldThrowException() {
        // Given
        StudentRegistrationRequest request = new StudentRegistrationRequest(
                "Max", "Mustermann", "password", "duplicate@test.de", null, "Address", "IT"
        );

        // Email existiert bereits
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(new Student()));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerStudent(request);
        });

        assertEquals("Email already in use", exception.getMessage());
        verify(studentRepository, never()).save(any());
    }

    @Test
    void registerStudent_WhenLimitReached_ShouldThrowException() {
        // Given
        StudentRegistrationRequest request = new StudentRegistrationRequest(
                "Max", "Mustermann", "password", "max@test.de", null, "Address", "IT"
        );

        // Höchste Nummer ist am Limit
        when(studentRepository.findMaxMatriculationNumber()).thenReturn(Optional.of("9999999"));

        // When & Then
        assertThrows(IllegalStateException.class, () -> userService.registerStudent(request));
        verify(studentRepository, never()).save(any());
    }

    @Test
    void login_WithMatriculationNumber_ShouldReturnAuthResponse() {
        // Given
        String matNr = "1234567";
        LoginRequest loginRequest = new LoginRequest(matNr, "password123");
        Student student = new Student("Max", "Mustermann", "max@test.de", "Address", "hashedPassword", null, matNr, "IT");

        // Mocken des Aufrufs über Matrikelnummer
        when(studentRepository.findByMatriculationNumber(matNr)).thenReturn(Optional.of(student));
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);
        when(jwtService.generateToken(anyString(), anyMap())).thenReturn("fake-jwt-token");

        // When
        AuthResponse response = userService.login(loginRequest);
        
        // Then
        assertEquals("STUDENT", response.role());
        assertEquals("fake-jwt-token", response.accessToken());
        assertEquals("Max", response.firstName());
        
        // Verifizieren, dass der Service zuerst das Repository für Matrikelnummern nutzt
        verify(studentRepository).findByMatriculationNumber(matNr);
        verify(userRepository, never()).findByEmail(anyString()); // Email-Suche sollte nicht stattfinden
    }

    @Test
    void login_WithEmailForStudent_ShouldReturnAuthResponse() {
        // Given
        String email = "max@test.de";
        LoginRequest loginRequest = new LoginRequest(email, "password123");
        Student student = new Student("Max", "Mustermann", email, "Address", "hashedPassword", null, "1234567", "IT");

        // Mocken der Email-Suche für Student
        when(studentRepository.findByMatriculationNumber(email)).thenReturn(Optional.empty()); // Keine Matrikelnummer
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(student));
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);
        when(jwtService.generateToken(anyString(), anyMap())).thenReturn("fake-jwt-token");

        // When
        AuthResponse response = userService.login(loginRequest);

        // Then
        assertEquals("STUDENT", response.role());
        assertEquals("fake-jwt-token", response.accessToken());
        assertEquals("Max", response.firstName());

        verify(studentRepository).findByMatriculationNumber(email); // Muss versuchen als Matrikelnummer
        verify(userRepository).findByEmail(email); // Muss dann als Email suchen
    }

    @Test
    void login_WithEmailForTeacher_ShouldReturnAuthResponse() {
        // Given
        String email = "prof@test.de";
        LoginRequest loginRequest = new LoginRequest(email, "password123");
        Teacher teacher = new Teacher("Prof.", "Lehrer", email, "Address", "hashedPassword", null, "Research", "Chair");

        // Mocken der Email-Suche für Teacher
        when(studentRepository.findByMatriculationNumber(email)).thenReturn(Optional.empty()); // Keine Matrikelnummer
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(teacher));
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);
        when(jwtService.generateToken(anyString(), anyMap())).thenReturn("fake-jwt-token");

        // When
        AuthResponse response = userService.login(loginRequest);

        // Then
        assertEquals("TEACHER", response.role());
        assertEquals("fake-jwt-token", response.accessToken());
        assertEquals("Prof.", response.firstName());

        verify(studentRepository).findByMatriculationNumber(email); // Muss versuchen als Matrikelnummer
        verify(studentRepository, never()).findByEmail(anyString());
    }

    @Test
    void getUserProfile_WithFullProfileTrue_ShouldIncludeAddress() {
        // Given
        int userId = 1;
        Student student = new Student("Max", "Mustermann", "max@test.de", "Musterweg 5", "pass", null, "1234567", "IT");
        when(userRepository.findById(userId)).thenReturn(Optional.of(student));

        // When
        ProfileResponse response = userService.getUserProfile(userId, true);

        // Then
        assertEquals("Musterweg 5", response.privateAddress());
        assertEquals("Max", response.firstName());
    }

    @Test
    void getUserProfile_WithFullProfileFalse_ShouldMaskAddress() {
        // Given
        int userId = 1;
        Student student = new Student("Max", "Mustermann", "max@test.de", "Musterweg 5", "pass", null, "1234567", "IT");
        when(userRepository.findById(userId)).thenReturn(Optional.of(student));

        // When
        ProfileResponse response = userService.getUserProfile(userId, false);

        // Then
        assertNull(response.privateAddress());
        assertEquals("Max", response.firstName());
    }

    @Test
    void searchUsers_ShouldReturnPublicProfilesWithoutAddresses() {
        // Given
        String searchTerm = "Max";
        Student student = new Student("Max", "Mustermann", "max@test.de", "Geheimweg 1", "pass", null, "1234567", "IT");
        
        when(userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(searchTerm, searchTerm))
                .thenReturn(List.of(student));

        // When
        List<ProfileResponse> results = userService.searchUsers(searchTerm);

        // Then
        assertEquals(1, results.size());
        assertEquals("Max", results.get(0).firstName());
        assertNull(results.get(0).privateAddress());
        verify(userRepository).findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(searchTerm, searchTerm);
    }
}
