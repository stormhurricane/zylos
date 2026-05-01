package com.zylos.backend.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zylos.backend.model.dto.LoginRequest;
import com.zylos.backend.model.dto.StudentRegistrationRequest;
import com.zylos.backend.model.entity.Student;
import com.zylos.backend.repository.StudentRepository;
import com.zylos.backend.repository.UserRepository;

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
    private UserRepository userRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
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
        Student student = userRepository.findByEmail("erika@uni.de")
                .map(Student.class::cast)
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

    @Test
    void searchUsersEndpointTest() throws Exception {
        // 1. Nutzer anlegen
        StudentRegistrationRequest regRequest = new StudentRegistrationRequest(
                "Bob", "Builder", "pass", "bob@uni.de",
                null, "Bauplatz 7", "Architektur"
        );
        mockMvc.perform(post("/api/users/register/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(regRequest)));

        // Login um Token zu erhalten (Suche ist geschützt)
        LoginRequest loginRequest = new LoginRequest("bob@uni.de", "pass");
        String response = mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andReturn().getResponse().getContentAsString();
        String token = objectMapper.readTree(response).get("accessToken").asText();

        // 2. Suche via 'q' Parameter
        mockMvc.perform(get("/api/users/search")
                .header("Authorization", "Bearer " + token)
                .param("q", "Builder"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Bob"))
                .andExpect(jsonPath("$[0].privateAddress").isEmpty()); // Maskierung prüfen

        // 3. Öffentliches Profil via ID prüfen
        Student student = userRepository.findByEmail("bob@uni.de").map(Student.class::cast).orElseThrow();
        mockMvc.perform(get("/api/users/" + student.getId())
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Bob"))
                .andExpect(jsonPath("$.privateAddress").isEmpty()); // Maskierung prüfen
    }
}
