package com.zylos.backend.features.course.enrollment.dto;

import com.zylos.backend.features.course.enrollment.EnrollmentRole;

import jakarta.validation.constraints.NotNull;

public record EnrollmentRequest(
    @NotNull(message = "User ID must not be null")
    Long userId,
    @NotNull EnrollmentRole role
) {}
