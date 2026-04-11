package com.zylos.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zylos.backend.model.dto.LoginRequest;
import com.zylos.backend.model.dto.StudentRegistrationRequest;
import com.zylos.backend.model.entity.Student;
import com.zylos.backend.repository.StudentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void setup() {
        studentRepository.deleteAll();
    }

    @Test
    void fullAuthFlowTest() throws Exception {
        // 1. Register
        StudentRegistrationRequest regRequest = new StudentRegistrationRequest(
                "Max", "Mustermann", "password123", "max@uni.de",
                null, "Musterstraße 1", "Informatik"
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

        // 3. Access Protected Resource (/me)
        // Note: Currently getCurrentUserId() returns 0, so we expect the service to work with ID 0
        // This test will help us verify the JWT Filter once it is implemented.
        mockMvc.perform(get("/api/users/me")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("max@uni.de"))
                .andExpect(jsonPath("$.firstName").value("Max"));
    }

    @Test
    void loginWithMatriculationNumberTest() throws Exception {
        // 1. Registrierung
        StudentRegistrationRequest regRequest = new StudentRegistrationRequest(
                "Erika", "Musterfrau", "securePass", "erika@uni.de",
                null, "Musterstraße 2", "Physik"
        );

        mockMvc.perform(post("/api/users/register/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(regRequest)))
                .andExpect(status().isCreated());

        // 2. Wir müssen die generierte Matrikelnummer aus der DB holen
        Student student = studentRepository.findByEmail("erika@uni.de")
                .orElseThrow();
        String matNr = student.getMatriculationNumber();

        // 3. Login mit Matrikelnummer statt Email
        LoginRequest loginRequest = new LoginRequest(matNr, "securePass");

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("STUDENT"));
    }
}
