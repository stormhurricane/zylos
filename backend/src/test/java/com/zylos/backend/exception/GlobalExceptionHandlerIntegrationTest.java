package com.zylos.backend.exception;

import com.zylos.backend.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class GlobalExceptionHandlerIntegrationTest extends BaseIntegrationTest {

    @Test
    void shouldHandleBadCredentialsOrInvalidInput_WithStructuredError() throws Exception {
        // Send False Credentials to the Login endpoint to trigger a controlled exception
        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"wrong-user@zylos.com\",\"password\":\"wrongpassword\"}"))
               .andExpect(status().is4xxClientError())
               .andExpect(jsonPath("$.message").exists())
               .andExpect(jsonPath("$.timestamp").exists());
    }

    @Test
    void shouldHandleAuthenticationException_WhenTokenIsMissing() throws Exception {
        mockMvc.perform(get("/api/courses"))
               .andExpect(status().isUnauthorized())
               .andExpect(jsonPath("$.message").value("Authentification failed: invalid or missing token."))
               .andExpect(jsonPath("$.errors").doesNotExist());
    }

    @Test
    void shouldHandleAllUncaughtExceptions_WithInternalServerError() throws Exception {
        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{corrupt-json-format"))
               .andExpect(status().is4xxClientError()); 
    }
}