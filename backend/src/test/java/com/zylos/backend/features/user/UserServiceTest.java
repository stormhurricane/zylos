package com.zylos.backend.features.user;

import com.zylos.backend.config.security.JwtService;
import com.zylos.backend.config.security.Role;
import com.zylos.backend.features.user.dto.AuthResponse;
import com.zylos.backend.features.user.dto.LoginRequest;
import com.zylos.backend.features.user.dto.ProfileResponse;
import com.zylos.backend.features.user.dto.StudentRegistrationRequest;
import com.zylos.backend.features.user.exceptions.EmailAlreadyExistsException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.w3c.dom.css.Counter;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
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
    private CounterRepository counterRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserService userService;

    @Test
    void registerStudent_ShouldSaveStudentToRepository() {
        // Arrange
        StudentRegistrationRequest request = new StudentRegistrationRequest(
            "Max", "Mustermann", "max@stud.de", "Musterstraße 1", "password123", "pic.png", "Informatik"
        );
        Mockito.when(counterRepository.getAndLockCounter()).thenReturn(10000000L);
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());

        // Act
        userService.registerStudent(request);

        // Assert
        ArgumentCaptor<Student> studentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(userRepository).save(studentCaptor.capture());

        Mockito.verify(counterRepository).getAndLockCounter();
        Mockito.verify(counterRepository).incrementCounter();
        
        Student savedStudent = studentCaptor.getValue();
        assertEquals("Max", savedStudent.getFirstName());
        assertEquals("Informatik", savedStudent.getStudySubject());
        // matriculationNumber wird hier NICHT geprüft, da sie erst durch die DB gesetzt wird!
    }

    @Test
    void registerStudent_WhenEmailAlreadyInUse_ShouldThrowException() {
        // Given
        StudentRegistrationRequest request = new StudentRegistrationRequest(
                "Max", "Mustermann", "duplicate@test.de", "password", "Address", null, "IT"
        );

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(new Student()));

        // When & Then
        EmailAlreadyExistsException exception = assertThrows(EmailAlreadyExistsException.class, () -> {
            userService.registerStudent(request);
        });

        // assertEquals("Email already in use", exception.getMessage());
        verify(userRepository, never()).save(any());
        verify(studentRepository, never()).save(any());
    }

    @Test
    void login_WithMatriculationNumber_ShouldReturnAuthResponse() {
        // Given
        Long matNr = 10000001L;
        LoginRequest loginRequest = new LoginRequest(String.valueOf(matNr), "password123");
        
        Student student = new Student("Max", "Mustermann", "max@test.de", "Address", "hashedPassword", null, matNr ,"IT");
        student.setId(1L);

        when(userRepository.findByEmail(String.valueOf(matNr))).thenReturn(Optional.empty());
        when(studentRepository.findByMatriculationNumber(matNr)).thenReturn(Optional.of(student));
        // [Certain] Die neue Login-Kette abbilden
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);
        when(jwtService.generateToken(anyString(), anyLong(), anyList())).thenReturn("fake-jwt-token");

        // When
        AuthResponse response = userService.login(loginRequest);

        // Then
        assertEquals(Role.STUDENT, response.role());
        assertEquals("fake-jwt-token", response.accessToken());
        assertEquals("Max", response.firstName());
    }

    @Test
    void login_WithEmailForTeacher_ShouldReturnAuthResponse() {
        // Given
        String email = "prof@test.de";
        LoginRequest loginRequest = new LoginRequest(email, "password123");
        
        Teacher teacher = new Teacher("Prof.", "Lehrer", email, "Address", "hashedPassword", null, "", "");
        teacher.setId(1L);

        when(userRepository.findByEmail(email)).thenReturn(Optional.of(teacher));
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);
        when(jwtService.generateToken(anyString(), anyLong(), anyList())).thenReturn("fake-jwt-token");

        // When
        AuthResponse response = userService.login(loginRequest);

        // Then
        assertEquals(Role.INSTRUCTOR, response.role());
        assertEquals("fake-jwt-token", response.accessToken());
        assertEquals("Prof.", response.firstName());
    }

    @Test
    void getUserProfile_WithFullProfileTrue_ShouldIncludeAddress() {
        // Given
        long userId = 1L; // Long-IDs nutzen!
        long testMatriculationNumber = 10000001L; // Hart codiert für den Test!
        Student student = new Student("Max", "Mustermann", "max@test.de", "Musterweg 5", "pass", null, testMatriculationNumber, "IT");
        student.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(student));

        // When
        ProfileResponse response = userService.getUserProfile(userId, true);

        // Then
        assertEquals("Musterweg 5", response.privateAddress());
        assertEquals("Max", response.firstName());
        assertEquals(10000001, response.matriculationNumber());
    }

    @Test
    void getUserProfile_WithFullProfileFalse_ShouldMaskAddress() {
        // Given
        long userId = 1L;
        long matriculationNumber = studentRepository.getNextMatriculationNumber();
        Student student = new Student("Max", "Mustermann", "max@test.de", "Musterweg 5", "pass", null, matriculationNumber, "IT");
        student.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(student));

        // When
        ProfileResponse response = userService.getUserProfile(userId, false);

        // Then
        assertNull(response.privateAddress());
        assertEquals("Max", response.firstName());
    }
}