package com.zylos.backend.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record TeacherRegistrationRequest(
    @NotBlank String firstName,
    @NotBlank String lastName,
    @NotBlank String password,
    @NotBlank @Email String email,
    String profilePicture,
    String privateAddress,
    String chair,
    String researchArea
) {}
