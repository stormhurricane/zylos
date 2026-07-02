package com.zylos.backend.features.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zylos.backend.features.user.dto.LoginRequest;
import com.zylos.backend.features.user.dto.StudentRegistrationRequest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional; // NEU

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional 
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void fullAuthFlowTest() throws Exception {
        // 1. Register
        StudentRegistrationRequest regRequest = new StudentRegistrationRequest(
                "Max", "Mustermann", "password123", "max@uni.de",
                "", "Musterstraße 1", "Informatik"
        );

        mockMvc.perform(post("/api/users/register/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(regRequest)))
                .andExpect(status().isCreated());

        // 2. Login
        LoginRequest loginRequest = new LoginRequest("max@uni.de", "password123");
        MvcResult loginResult = mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.role").value("STUDENT"))
                .andReturn();

        String responseContent = loginResult.getResponse().getContentAsString();
        String token = objectMapper.readTree(responseContent).get("accessToken").asText();

        // 3. Access Protected Resource
        mockMvc.perform(get("/api/users/me")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("max@uni.de"))
                .andExpect(jsonPath("$.firstName").value("Max"));
    }

    @Test 
    void registerStudent_WithDuplicateEmail_ShouldReturnBadRequest() throws Exception {
        // GIVEN
        StudentRegistrationRequest regRequest = new StudentRegistrationRequest(
                "Max", "Mustermann", "password123", "duplicate@uni.de",
                "", "Musterstraße 1", "Informatik"
        );

        mockMvc.perform(post("/api/users/register/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(regRequest)))
                .andExpect(status().isCreated());

        mockMvc.perform(post("/api/users/register/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(regRequest)))
                .andDo(print())
                .andExpect(status().isConflict()); 
    }

    @Test
    void getMe_WithoutToken_ShouldReturnUnauthorized() throws Exception {
        mockMvc.perform(get("/api/users/me"))
                .andDo(print())
                .andExpect(status().isUnauthorized()); 
    }

    @Test
    void registerStudent_WithInvalidEmail_ShouldReturnBadRequest() throws Exception {
        StudentRegistrationRequest invalidRequest = new StudentRegistrationRequest(
                "", "Mustermann", "", "keine-email-adresse",
                "", "Musterstraße 1", "Informatik"
        );

        // WHEN & THEN
        mockMvc.perform(post("/api/users/register/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest()); 
    }

    @Test
    void login_WithWrongPassword_ShouldReturnUnauthorized() throws Exception {
        StudentRegistrationRequest regRequest = new StudentRegistrationRequest(
                "Max", "Mustermann", "password123", "wrongpass@uni.de",
                "", "Musterstraße 1", "Informatik"
        );
        mockMvc.perform(post("/api/users/register/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(regRequest)))
                .andExpect(status().isCreated());

        LoginRequest wrongLogin = new LoginRequest("wrongpass@uni.de", "FALSCHES_PASSWORT");
        
        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(wrongLogin)))
                .andDo(print())
                .andExpect(status().isUnauthorized()); 
    }
}