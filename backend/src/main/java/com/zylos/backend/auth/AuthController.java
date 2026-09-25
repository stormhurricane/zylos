package com.zylos.backend.auth;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zylos.backend.auth.dto.RegisterRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j 
@RestController 
@RequestMapping ("api/v1/auth")
public class AuthController {

    @PostMapping ("/register")
    public void register(@RequestBody RegisterRequest request) {
        log.info("Registering user: {}", request);
    }
    
}