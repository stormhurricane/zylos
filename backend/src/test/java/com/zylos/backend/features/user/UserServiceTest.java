package com.zylos.backend.features.user;

import com.zylos.backend.config.security.JwtService;
import com.zylos.backend.features.user.dto.AuthResponse;
import com.zylos.backend.features.user.dto.LoginRequest;
import com.zylos.backend.features.user.dto.ProfileResponse;
import com.zylos.backend.features.user.dto.StudentRegistrationRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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
                "Max", "Mustermann", "max@test.de", "password", "Address", null, "IT"
        );
        
        when(userRepository.findByEmail(request.email())).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");
        
        // [Certain] Wir müssen simulieren, dass das UserRepository eine ID generiert!
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(42L); // Shared Primary Key simulieren
            return u;
        });
        
        when(studentRepository.findMaxMatriculationNumber()).thenReturn(Optional.of("1000005"));

        // When
        userService.registerStudent(request);

        // Then
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        assertEquals("hashedPassword", userCaptor.getValue().getPassword());

        ArgumentCaptor<Student> studentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(studentCaptor.capture());
        
        Student savedStudent = studentCaptor.getValue();
        assertEquals(42L, savedStudent.getUserId());
        assertEquals("1000006", savedStudent.getMatriculationNumber());
    }

    @Test
    void registerStudent_WhenEmailAlreadyInUse_ShouldThrowException() {
        // Given
        StudentRegistrationRequest request = new StudentRegistrationRequest(
                "Max", "Mustermann", "duplicate@test.de", "password", "Address", null, "IT"
        );

        when(userRepository.findByEmail(request.email())).thenReturn(Optional.of(new User()));

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            userService.registerStudent(request);
        });

        assertEquals("Email already in use", exception.getMessage());
        verify(userRepository, never()).save(any());
        verify(studentRepository, never()).save(any());
    }

    @Test
    void login_WithMatriculationNumber_ShouldReturnAuthResponse() {
        // Given
        String matNr = "1234567";
        LoginRequest loginRequest = new LoginRequest(matNr, "password123");
        
        Student student = new Student(1L, matNr, "IT");
        User user = new User("Max", "Mustermann", "max@test.de", "Address", "hashedPassword", null);
        user.setId(1L);

        // [Certain] Die neue Login-Kette abbilden
        when(studentRepository.findByMatriculationNumber(matNr)).thenReturn(Optional.of(student));
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);
        when(teacherRepository.existsByUserId(1L)).thenReturn(false); // Ergo: Ist Student
        when(jwtService.generateToken(anyString(), anyMap())).thenReturn("fake-jwt-token");

        // When
        AuthResponse response = userService.login(loginRequest);

        // Then
        assertEquals("STUDENT", response.role());
        assertEquals("fake-jwt-token", response.accessToken());
        assertEquals("Max", response.firstName());
    }

    @Test
    void login_WithEmailForTeacher_ShouldReturnAuthResponse() {
        // Given
        String email = "prof@test.de";
        LoginRequest loginRequest = new LoginRequest(email, "password123");
        
        User user = new User("Prof.", "Lehrer", email, "Address", "hashedPassword", null);
        user.setId(5L);

        when(studentRepository.findByMatriculationNumber(email)).thenReturn(Optional.empty());
        when(userRepository.findByEmail(email)).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("password123", "hashedPassword")).thenReturn(true);
        when(teacherRepository.existsByUserId(5L)).thenReturn(true); // Ergo: Ist Teacher
        when(jwtService.generateToken(anyString(), anyMap())).thenReturn("fake-jwt-token");

        // When
        AuthResponse response = userService.login(loginRequest);

        // Then
        assertEquals("TEACHER", response.role());
        assertEquals("fake-jwt-token", response.accessToken());
        assertEquals("Prof.", response.firstName());
    }

    @Test
    void getUserProfile_WithFullProfileTrue_ShouldIncludeAddress() {
        // Given
        long userId = 1L; // Long-IDs nutzen!
        User user = new User("Max", "Mustermann", "max@test.de", "Musterweg 5", "pass", null);
        user.setId(userId);
        Student student = new Student(userId, "1234567", "IT");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(studentRepository.findByUserId(userId)).thenReturn(Optional.of(student));

        // When
        ProfileResponse response = userService.getUserProfile(userId, true);

        // Then
        assertEquals("Musterweg 5", response.privateAddress());
        assertEquals("Max", response.firstName());
        assertEquals("1234567", response.matriculationNumber());
    }

    @Test
    void getUserProfile_WithFullProfileFalse_ShouldMaskAddress() {
        // Given
        long userId = 1L;
        User user = new User("Max", "Mustermann", "max@test.de", "Musterweg 5", "pass", null);
        user.setId(userId);
        Student student = new Student(userId, "1234567", "IT");

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(studentRepository.findByUserId(userId)).thenReturn(Optional.of(student));

        // When
        ProfileResponse response = userService.getUserProfile(userId, false);

        // Then
        assertNull(response.privateAddress());
        assertEquals("Max", response.firstName());
    }
}