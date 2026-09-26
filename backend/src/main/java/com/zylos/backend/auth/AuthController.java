package com.zylos.backend.auth;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.zylos.backend.auth.dto.RegisterRequest;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor 
@Slf4j 
@RestController 
@RequestMapping ("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping ("/register")
    public void register(@RequestBody RegisterRequest request) {
        log.debug("Registering user: {}", request);
        authService.register(request);
    }
    
}