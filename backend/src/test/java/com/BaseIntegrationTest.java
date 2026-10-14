package com;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest
@AutoConfigureMockMvc
public abstract class BaseIntegrationTest {
    
    @ServiceConnection 
    static PostgreSQLContainer<?> postgresContainer = 
            new PostgreSQLContainer<>("postgres:16-alpine");

    static {
        postgresContainer.start();
    }

}
