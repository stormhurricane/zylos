package com.zylos.backend.features.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zylos.backend.BaseIntegrationTest;
import com.zylos.backend.features.user.dto.LoginRequest;
import com.zylos.backend.features.user.dto.StudentRegistrationRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class UserControllerIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;


    @Autowired
    private StudentRepository studentRepository;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    void fullAuthFlowTest() throws Exception {
        // 1. Register (FIX: "" statt null für das Profilbild übergeben, um Validation-Glitches zu vermeiden)
        StudentRegistrationRequest regRequest = new StudentRegistrationRequest(
                "Max", "Mustermann", "password123", "max@uni.de",
                "", "Musterstraße 1", "Informatik"
        );

        mockMvc.perform(post("/api/users/register/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(regRequest)))
                .andDo(print())
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
        mockMvc.perform(get("/api/users/me")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("max@uni.de"))
                .andExpect(jsonPath("$.firstName").value("Max"));
    }

    @Test
    void loginWithMatriculationNumberTest() throws Exception {
        // 1. Registration (FIX: "" statt null)
        StudentRegistrationRequest regRequest = new StudentRegistrationRequest(
                "Erika", "Musterfrau", "securePass", "erika@uni.de",
                "", "Musterstraße 2", "Physik"
        );

        mockMvc.perform(post("/api/users/register/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(regRequest)))
                .andExpect(status().isCreated());

        // 2. Get the generated matriculation number from the database
        User user91 = userRepository.findByEmail("erika@uni.de").orElseThrow();          
        Student student = studentRepository.findById(user91.getId()).orElseThrow();        
        Long matNr = student.getMatriculationNumber();

        // 3. Login with matriculation number instead of email
        LoginRequest loginRequest = new LoginRequest(String.valueOf(matNr), "securePass");

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("STUDENT"));
    }

    @Test
    void searchUsersEndpointTest() throws Exception {
        // 1. Create user (FIX: "" statt null)
        StudentRegistrationRequest regRequest = new StudentRegistrationRequest(
                "Bob", "Builder", "password", "bob@uni.de",
                "", "Bauplatz 7", "Architektur"
        );
        mockMvc.perform(post("/api/users/register/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(regRequest)))
                .andExpect(status().isCreated()); // Sicherstellen, dass der User wirklich da ist!

        // Login to obtain token
        LoginRequest loginRequest = new LoginRequest("bob@uni.de", "password");
        String response = mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
        String token = objectMapper.readTree(response).get("accessToken").asText();

        // 2. Search via 'q' parameter
        mockMvc.perform(get("/api/users/search")
                .header("Authorization", "Bearer " + token)
                .param("q", "Builder"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Bob"));

        // 3. Check public profile via ID
        User user133 = userRepository.findByEmail("bob@uni.de").orElseThrow();
        Student student = studentRepository.findById(user133.getId()).orElseThrow();

        mockMvc.perform(get("/api/users/" + student.getId())
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Bob"));
    }
}