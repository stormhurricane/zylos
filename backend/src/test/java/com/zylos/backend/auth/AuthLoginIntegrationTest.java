package com.zylos.backend.auth;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.BaseIntegrationTest;
import com.zylos.backend.auth.dto.UserResponse;
import com.zylos.backend.core.exception.ErrorResponse;
import com.zylos.backend.features.user.SystemRole;
import com.zylos.backend.features.user.UserRepository;
import com.zylos.backend.features.user.entity.User;
import jakarta.servlet.http.Cookie;
import org.assertj.core.api.SoftAssertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

@AutoConfigureMockMvc
public class AuthLoginIntegrationTest extends BaseIntegrationTest {
    
    @Autowired 
    protected MockMvc mockMvc;
    @Autowired 
    private UserRepository userRepository;
    @Autowired 
    private PasswordEncoder passwordEncoder;

    @BeforeEach 
    public void setupTestData() {
        userRepository.deleteAll();
    }

    private User createAndSaveUserToDb() {
        User user = User.create(
                "John", 
                "Doe", 
                "john.doe@example.com", 
                "johndoe", 
                passwordEncoder.encode("password"), 
                SystemRole.STUDENT
        );
        userRepository.save(user);

        return user;
    }

    @Test 
    public void shouldLoginSuccessfully_andReturnJwtCookieAndUserData() throws Exception {
        User user = createAndSaveUserToDb();

        String requestBody = """
            {
                "identifier": "johndoe",
                "password": "password"
            }
                """;
        
        String responseJson = mockMvc.perform(
                post("/api/v1/auth/login")
                    .contentType("application/json")
                    .content(requestBody)   
        )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();
        
        UserResponse userResponse = objectMapper.readValue(responseJson, UserResponse.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(userResponse.id()).isNotNull();
            softly.assertThat(userResponse.username()).isEqualTo(user.getUsername());
            softly.assertThat(userResponse.email()).isEqualTo(user.getEmail());
            softly.assertThat(userResponse.roles().contains(user.getRole().name()));
        });
    }

    @Test 
    public void shouldReturnUnauthorized_whenPasswordIsIncorrect() throws Exception {
        createAndSaveUserToDb();
        String requestBody = """
            {
                "identifier": "johndoe",
                "password": "pass"
            }
                """;

        String responseJson = mockMvc.perform(
                post("/api/v1/auth/login")
                    .contentType("application/json")
                    .content(requestBody)   
        )
                .andExpect(status().isUnauthorized())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ErrorResponse errorResponse = objectMapper.readValue(responseJson, ErrorResponse.class);
        assertThat(errorResponse.message())
                .containsIgnoringCase("Invalid login data or expired token");
    }

    @Test 
    public void shouldAccessProtectedEndpointMe_whenJwtCookieIsProvided() throws Exception {
        User user = createAndSaveUserToDb();
        String requestBody = """
            {
                "identifier": "johndoe",
                "password": "password"
            }
                """;

        MvcResult result = mockMvc.perform(
                post("/api/v1/auth/login")
                    .contentType("application/json")
                    .content(requestBody)   
        )
                .andExpect(status().isOk())
                .andReturn();

        // jwtCookie depends on cookie Naming
        Cookie cookie = result.getResponse().getCookie("jwt");
        assertNotNull(cookie, "Jwt cookie was not returned by login endpoint");

        String jsonResponse = mockMvc.perform(
                get("/api/v1/auth/me")
                    .cookie(cookie)   
        )
                .andExpect(status().isOk())
                .andReturn()
                .getResponse()
                .getContentAsString();

        UserResponse userResponse = objectMapper.readValue(jsonResponse, UserResponse.class);

        SoftAssertions.assertSoftly(softly -> {
            softly.assertThat(userResponse.id()).isNotNull();
            softly.assertThat(userResponse.username()).isEqualTo(user.getUsername());
            softly.assertThat(userResponse.email()).isEqualTo(user.getEmail());
            softly.assertThat(userResponse.roles()).contains(user.getRole().name());
        });
    }

    @Test 
    public void shouldReturnUnauthorized_whenAccessingMeWithoutCookie() throws Exception {
        String responseJson = mockMvc.perform(
                get("/api/v1/auth/me")
            )
                .andExpect(status().isUnauthorized())
                .andReturn()
                .getResponse()
                .getContentAsString();

        ErrorResponse response = objectMapper.readValue(responseJson, ErrorResponse.class);

        assertThat(response.message()).containsIgnoringCase("Invalid login data or expired token");
    }

    @Test 
    public void shouldLogoutSuccessfully_andClearJwtCookie() throws Exception {
        createAndSaveUserToDb();

        String requestBody = """
            {
                "identifier": "johndoe",
                "password": "password"
            }
                """;

        MvcResult result = mockMvc.perform(
                post("/api/v1/auth/login")
                    .contentType("application/json")
                    .content(requestBody)   
        )
                .andExpect(status().isOk())
                .andReturn();

        // jwtCookie depends on cookie Naming
        Cookie cookie = result.getResponse().getCookie("jwt");
        assertNotNull(cookie, "Jwt cookie was not returned by login endpoint");

        
        result = mockMvc.perform(
                post("/api/v1/auth/logout")
                    .cookie(cookie)
        )
                .andExpect(status().isOk())
                .andReturn();

        Cookie logoutCookie = result.getResponse().getCookie("jwt");
        
        assertThat(logoutCookie.getMaxAge()).isEqualTo(0);
    }


    // TODO Status check during login - shouldReturnForbiddenOrUnauthorized_whenUserIsPending

}
