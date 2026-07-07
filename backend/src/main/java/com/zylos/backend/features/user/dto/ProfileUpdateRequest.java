package com.zylos.backend.features.user.dto;

import jakarta.validation.constraints.Size;

public record ProfileUpdateRequest(
    @Size(min = 8, max = 127) String password,
    @Size(max = 10_000_000) String profilePicture,
    @Size(max = 127) String privateAddress,
    @Size(max = 127) String chair,
    @Size(max = 127) String researchArea,
    @Size(max = 127) String studySubject
) {}
