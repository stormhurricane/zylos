package com.zylos.backend.features.user.dto;

import com.zylos.backend.config.security.Role;

public record AuthResponse(
    String accessToken,
    long userId,
    Role role,
    String firstName,
    String lastName
) {}
