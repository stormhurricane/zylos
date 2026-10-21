package com.zylos.backend.auth;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.BaseIntegrationTest;
import com.zylos.backend.core.exception.ErrorResponse;
import com.zylos.backend.features.user.SystemRole;
import com.zylos.backend.features.user.UserRepository;
import com.zylos.backend.features.user.entity.User;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;



@AutoConfigureMockMvc
public class AuthRegistrationIntegrationTest extends BaseIntegrationTest {

    @Autowired 
    private MockMvc mockMvc;

    @Autowired 
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach 
    public void setup() {
        userRepository.deleteAll();
    }

    @Test 
    public void shouldRegisterUserSuccessfully() throws Exception {
        String requestBody = """
            {
                "firstName": "Simple",
                "lastName": "User",
                "email": "simple.user@example.com",
                "username": "simpleUser_1",
                "password": "SecurePassword123!",
                "role": "STUDENT"
            }        
                """;
        
        mockMvc.perform(
                post("/api/v1/auth/register")
                        .contentType("application/json")
                        .content(requestBody)
        )
                        .andExpect(status().isNoContent());
    }

    @Test
    public void shouldReturnConflict_whenEmailAlreadyExists() throws Exception {
        User user = User.create(
                "Conflict", 
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

        String jsonResponse = mockMvc.perform(
                                post("/api/v1/auth/register")
                                        .contentType("application/json")
                                        .content(requestBody)       
                        )
                        .andExpect(status().isConflict())       
                        .andReturn()
                        .getResponse()
                        .getContentAsString();

        ErrorResponse response = objectMapper.readValue(jsonResponse, ErrorResponse.class);
        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.message())
                    .containsIgnoringCase("Registration failed due to conflicting user data");
            softly.assertThat(response.errors()).containsKey("email");
        });
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
        
        String jsonRespone = mockMvc.perform(
                post("/api/v1/auth/register")
                        .contentType("application/json")
                        .content(requestBody)       
        )
                .andExpect(status().isBadRequest())
                .andReturn()
                .getResponse()
                .getContentAsString();
        
        ErrorResponse response = objectMapper.readValue(jsonRespone, ErrorResponse.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(response.message()).containsIgnoringCase("Validation failed");
            softly.assertThat(response.errors())
                    .containsKey("email")
                    .containsKey("password")
                    .containsKey("username");
        });
    }

}
