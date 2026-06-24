package com.zylos.backend.features.user;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.zylos.backend.features.user.dto.LoginRequest;
import com.zylos.backend.features.user.dto.StudentRegistrationRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional // Ensures that each test runs in a transaction and rolls back changes
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

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
        // Verifies that the JWT Filter correctly identifies the user from the token.
        mockMvc.perform(get("/api/users/me")
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("max@uni.de"))
                .andExpect(jsonPath("$.firstName").value("Max"));
    }

    @Test
    void loginWithMatriculationNumberTest() throws Exception {
        // 1. Registration
        StudentRegistrationRequest regRequest = new StudentRegistrationRequest(
                "Erika", "Musterfrau", "securePass", "erika@uni.de",
                null, "Musterstraße 2", "Physik"
        );

        mockMvc.perform(post("/api/users/register/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(regRequest)))
                .andExpect(status().isCreated());

        // 2. Get the generated matriculation number from the database
        User user91 = userRepository.findByEmail("erika@uni.de").orElseThrow();          
        Student student = studentRepository.findById(user91.getId()).orElseThrow();        
        String matNr = student.getMatriculationNumber();

        // 3. Login with matriculation number instead of email
        LoginRequest loginRequest = new LoginRequest(matNr, "securePass");

        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.role").value("STUDENT"));
    }

    @Test
    void searchUsersEndpointTest() throws Exception {
        // 1. Create user
        StudentRegistrationRequest regRequest = new StudentRegistrationRequest(
                "Bob", "Builder", "pass", "bob@uni.de",
                null, "Bauplatz 7", "Architektur"
        );
        mockMvc.perform(post("/api/users/register/student")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(regRequest)));

        // Login to obtain token (search is protected)
        LoginRequest loginRequest = new LoginRequest("bob@uni.de", "pass");
        String response = mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(loginRequest)))
                .andReturn().getResponse().getContentAsString();
        String token = objectMapper.readTree(response).get("accessToken").asText();

        // 2. Search via 'q' parameter
        mockMvc.perform(get("/api/users/search")
                .header("Authorization", "Bearer " + token)
                .param("q", "Builder"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Bob"))
                .andExpect(jsonPath("$[0].privateAddress").isEmpty()); // Verify masking

        // 3. Check public profile via ID
        User user133 = userRepository.findByEmail("bob@uni.de").orElseThrow();
        Student student = studentRepository.findById(user133.getId()).orElseThrow();

        mockMvc.perform(get("/api/users/" + student.getId())
                .header("Authorization", "Bearer " + token))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Bob"))
                .andExpect(jsonPath("$.privateAddress").isEmpty()); // Verify masking
    }
}
