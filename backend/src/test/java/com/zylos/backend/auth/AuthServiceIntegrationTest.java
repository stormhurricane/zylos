package com.zylos.backend.auth;

import com.BaseIntegrationTest;
import com.zylos.backend.auth.dto.RegisterRequest;
import com.zylos.backend.features.user.SystemRole;
import com.zylos.backend.features.user.UserRepository;
import com.zylos.backend.features.user.UserStatus;
import com.zylos.backend.features.user.entity.User;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;


class AuthServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private AuthService authService; 
    
    @Autowired
    private UserRepository userRepository;

    @Autowired 
    private PasswordEncoder passwordEncoder;

    @BeforeEach 
    public void setup() {
        userRepository.deleteAll();
    }

    @Test
    void shouldRegisterStudentUserSuccessfully() {
        RegisterRequest request = new RegisterRequest(
                "John", 
                "Doe", 
                "john.doe_student@example.com", 
                "SecurePassword123!", 
                "johndoe_student", 
                SystemRole.STUDENT
        );

        authService.register(request);

        User user = userRepository.findByEmail("john.doe_student@example.com").orElseThrow();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(user.getFirstName()).isEqualTo("John");
            softly.assertThat(user.getLastName()).isEqualTo("Doe");
            softly.assertThat(user.getEmail()).isEqualTo("john.doe_student@example.com");
            softly.assertThat(user.getUsername()).isEqualTo("johndoe_student");
            softly.assertThat(user.getPassword()).isNotEqualTo("SecurePassword123!");
            softly.assertThat(
                    passwordEncoder.matches("SecurePassword123!", 
                    user.getPassword())
            ).isTrue();
            softly.assertThat(user.getStatus()).isEqualTo(UserStatus.APPROVED);
            softly.assertThat(user.getRole()).isEqualTo(SystemRole.STUDENT);
            softly.assertThat(user.getCreatedAt()).isNotNull();
            softly.assertThat(user.getUpdatedAt()).isNotNull();
            softly.assertThat(user.getId()).isNotNull();
        });
    }

    @Test 
    public void shouldRegisterTeacherSuccessfully() {
        RegisterRequest request = new RegisterRequest(
                "John", 
                "Doe", 
                "john.doe_teacher@example.com", 
                "SecurePassword123!", 
                "johndoe_teacher", 
                SystemRole.TEACHER
        );

        authService.register(request);

        User user = userRepository.findByEmail("john.doe_teacher@example.com").orElseThrow();

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(user.getRole()).isEqualTo(SystemRole.TEACHER);
            softly.assertThat(user.getStatus()).isEqualTo(UserStatus.PENDING);
        });
    }
}