package com.zylos.backend.auth.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginRequest(

    @NotBlank 
    String identifier,

    @NotBlank 
    String password
) {}
