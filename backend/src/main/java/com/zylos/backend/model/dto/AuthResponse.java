package com.zylos.backend.model.dto;

public record AuthResponse(
    String accessToken,
    int userId,
    String role,
    String firstName,
    String lastName
) {}
