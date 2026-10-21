package com;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import tools.jackson.databind.ObjectMapper;

@SpringBootTest
public abstract class BaseIntegrationTest {

    @Autowired
    protected ObjectMapper objectMapper;
    
    @ServiceConnection 
    static PostgreSQLContainer<?> postgresContainer = 
            new PostgreSQLContainer<>("postgres:16-alpine");

    static {
        postgresContainer.start();
    }

}
