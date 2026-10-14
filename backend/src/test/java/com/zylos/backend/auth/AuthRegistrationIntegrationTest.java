package com.zylos.backend.auth;

import static org.hamcrest.Matchers.containsStringIgnoringCase;
import static org.hamcrest.Matchers.hasKey;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.BaseIntegrationTest;
import com.zylos.backend.features.user.SystemRole;
import com.zylos.backend.features.user.UserRepository;
import com.zylos.backend.features.user.UserStatus;
import com.zylos.backend.features.user.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;



public class AuthRegistrationIntegrationTest extends BaseIntegrationTest {

    @Autowired 
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired 
    private MockMvc mockMvc;

    @Test 
    public void shouldRegisterStudentUser_withStatusApproved() throws Exception {
        User user = registerUserAndFetchFromDb("jane.doe@example.com", SystemRole.STUDENT);
        
        assertEquals("Test", user.getFirstName());
        assertEquals("User", user.getLastName());
        assertEquals("janedoe", user.getUsername());
        assertEquals(SystemRole.STUDENT, user.getRole());
        
        assertEquals(
                UserStatus.APPROVED, user.getStatus(), 
                "STUDENT role must be initialized as APPROVED"
        );

        assertNotEquals(
                "SecurePassword123!", user.getPassword(), 
                "Raw password must NEVER be stored in DB"
        );
        assertTrue(
                passwordEncoder.matches("SecurePassword123!", 
                user.getPassword()), "Hashed password must match input"
        );

        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
    }


    @Test
    public void shouldRegisterTeacherUser_withStatusPending() throws Exception {
        User user = registerUserAndFetchFromDb("john.smith@example.com", SystemRole.TEACHER);
        
        assertEquals(SystemRole.TEACHER, user.getRole());
        assertEquals(
                UserStatus.PENDING, user.getStatus(),  
                "TEACHER role must be initialized as PENDING"
        );
    }

    @Test
    public void shouldReturnConflict_whenEmailAlreadyExists() throws Exception {
        User user = User.create(
                "Test", 
                "User", 
                "duplicate@example.com", 
                "duplicateUser", 
                passwordEncoder.encode("SecurePassword123!"),
                SystemRole.STUDENT
        );
        
        userRepository.save(user);

        String requestBody = """
            {
                "firstName": "John",
                "lastName": "Smith",
                "email": "duplicate@example.com",
                "username": "johnsmith",
                "password": "SecurePassword123!",
                "role": "%s"
            }        
                """.formatted(SystemRole.STUDENT.name());

        mockMvc.perform(
                post("/api/v1/auth/register")
                        .contentType("application/json")
                        .content(requestBody)       
        )
                .andExpect(status().isConflict())
                .andExpect(
                        jsonPath("$.message", 
                        containsStringIgnoringCase(
                                "Registration failed due to conflicting user data"
                                )
                        )
                )
                .andExpect(jsonPath("$.errors", hasKey("email")));
    }

    @Test
    public void shouldReturnBadRequest_whenValidationFails() throws Exception {
        String requestBody = """
            {
                "firstName": "John",
                "lastName": "Smith",
                "email": "invalid-email-format",
                "password": "short"
            }        
                """;
        
        mockMvc.perform(
                post("/api/v1/auth/register")
                        .contentType("application/json")
                        .content(requestBody)       
        )
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", containsStringIgnoringCase("Validation failed")))
                .andExpect(jsonPath("$.errors", hasKey("email")))
                .andExpect(jsonPath("$.errors", hasKey("password")))
                .andExpect(jsonPath("$.errors", hasKey("username")));
    }


    private User registerUserAndFetchFromDb(String email, SystemRole role) throws Exception {
        String username = email.split("@")[0].replace(".", "");
        String requestBody = """
            {
                "firstName": "Test",
                "lastName": "User",
                "email": "%s",
                "username": "%s",
                "password": "SecurePassword123!",
                "role": "%s"
            }        
                """.formatted(email, username, role.name());
        
        mockMvc.perform(
                post("/api/v1/auth/register")
                        .contentType("application/json")
                        .content(requestBody)
        )
                        .andExpect(status().isNoContent());


        return userRepository.findByEmail(email)
            .orElseThrow(() -> new AssertionError("User should exist in database"));
    }
    
}
