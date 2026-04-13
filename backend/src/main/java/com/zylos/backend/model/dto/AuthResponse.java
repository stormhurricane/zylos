package com.zylos.backend.model.dto;

public record AuthResponse(
    String accessToken,
    String role,
    String firstName,
    String lastName
) {}
