package com;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public abstract class BaseIntegrationTest {
    
    @ServiceConnection 
    static PostgreSQLContainer<?> postgresContainer = 
            new PostgreSQLContainer<>("postgres:16-alpine");

    static {
        postgresContainer.start();
    }

    @LocalServerPort 
    protected int port;

    @BeforeEach 
    void setup() {
        RestAssured.port = port;
    }
}
