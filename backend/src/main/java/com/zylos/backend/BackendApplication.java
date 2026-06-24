package com.zylos.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
// @EnableScheduling
@ComponentScan(
    basePackages = {
        "com.zylos.backend.config",
        "com.zylos.backend.exception",
        "com.zylos.backend.features"
    }
)
@EnableJpaRepositories(
    basePackages = {
        "com.zylos.backend.features" 
    }
)
@EntityScan(
    basePackages = {
        "com.zylos.backend.features" 
    }
)
// @EnableAsync
public class BackendApplication {

    public static void main(String[] args) { SpringApplication.run(BackendApplication.class, args);
    }
}
