package com.zylos.backend.features.user.dto;

import java.util.UUID;

public record UserPublicResponse(
        UUID id,
        String firstName,
        String lastName,
        String username
) {}
