package com.zylos.backend.service;

import com.zylos.backend.model.dto.ProfileResponse;
import com.zylos.backend.model.dto.StudentRegistrationRequest;
import com.zylos.backend.model.entity.Student;
import com.zylos.backend.repository.StudentRepository;
import com.zylos.backend.repository.TeacherRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

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
        
        when(studentRepository.existsByEmail(anyString())).thenReturn(false);
        when(teacherRepository.existsByEmail(anyString())).thenReturn(false);
        when(studentRepository.findMaxMatriculationNumber()).thenReturn(Optional.of("1000005"));
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");

        // When
        userService.registerStudent(request);

        // Then
        ArgumentCaptor<Student> studentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(studentCaptor.capture());
        
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

        when(studentRepository.existsByEmail(anyString())).thenReturn(false);
        when(teacherRepository.existsByEmail(anyString())).thenReturn(false);
        // Simulieren einer leeren Tabelle
        when(studentRepository.findMaxMatriculationNumber()).thenReturn(Optional.empty());
        when(passwordEncoder.encode(anyString())).thenReturn("hashedPassword");

        // When
        userService.registerStudent(request);

        // Then
        ArgumentCaptor<Student> studentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentRepository).save(studentCaptor.capture());
        assertEquals("1000000", studentCaptor.getValue().getMatriculationNumber());
    }

    @Test
    void registerStudent_WhenEmailAlreadyInUse_ShouldThrowException() {
        // Given
        StudentRegistrationRequest request = new StudentRegistrationRequest(
                "Max", "Mustermann", "password", "duplicate@test.de", null, "Address", "IT"
        );

        // Email existiert bereits im TeacherRepository
        when(studentRepository.existsByEmail("duplicate@test.de")).thenReturn(false);
        when(teacherRepository.existsByEmail("duplicate@test.de")).thenReturn(true);

        // When & Then
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

        when(studentRepository.existsByEmail(anyString())).thenReturn(false);
        when(teacherRepository.existsByEmail(anyString())).thenReturn(false);
        // Höchste Nummer ist am Limit
        when(studentRepository.findMaxMatriculationNumber()).thenReturn(Optional.of("9999999"));

        // When & Then
        assertThrows(IllegalStateException.class, () -> userService.registerStudent(request));
        verify(studentRepository, never()).save(any());
    }
}
