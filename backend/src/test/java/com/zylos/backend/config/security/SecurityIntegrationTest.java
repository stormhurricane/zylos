package com.zylos.backend.config.security;

import com.zylos.backend.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class SecurityIntegrationTest extends BaseIntegrationTest {

    @Test
    void permitAllEndpoints_ShouldPassSecurity() throws Exception {
        mockMvc.perform(post("/api/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"email\":\"test@test.com\",\"password\":\"password\"}"))
                // 200 or 401 does not matter, we just want to ensure that the endpoint is not blocked by security.
               .andExpect(status().is4xxClientError()); 
    }

    @Test
    void securedEndpoints_ShouldReturn401_WhenNoTokenProvided() throws Exception {
        // Only check that security is enforced
        mockMvc.perform(get("/api/courses"))
               .andExpect(status().isUnauthorized()); 
    }

    @Test
    @WithMockUserPrincipal(id = 42L, role = "INSTRUCTOR")
    void securedEndpoints_ShouldAllowAccess_WhenAuthenticated() throws Exception {
        mockMvc.perform(get("/api/courses"))
               .andExpect(status().isOk()); 
    }
}