package com.zylos.backend.features.user.dto;

public record AuthResponse(
    String accessToken,
    long userId,
    String role,
    String firstName,
    String lastName
) {}
