package com.zylos.backend.features.user.dto;

import java.util.UUID;

public record UserPrivateResponse(
        UUID id,
        String firstName,
        String lastName,
        String username,
        String email
) {}
