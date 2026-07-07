package com.zylos.backend.features.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TeacherRegistrationRequest(
    @NotBlank @Size(min = 2, max = 63) String firstName,
    @NotBlank @Size(min = 2, max = 63) String lastName,
    @NotBlank @Size(min = 8, max = 127) String password,
    @NotBlank @Email @Size(max = 127) String email,
    @Size(max = 10_000_000) String profilePicture,
    @Size(max = 127) String privateAddress,
    @Size(max = 127) String chair,
    @Size(max = 127) String researchArea
) {}
