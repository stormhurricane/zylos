package com.zylos.backend.features.course.core.dto;

import com.zylos.backend.features.course.core.CourseType;
import com.zylos.backend.features.course.core.SemesterTerm;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CourseRequest(
    @NotBlank String title,
    @NotNull CourseType type,
    @NotNull SemesterTerm term,
    @NotBlank String academicYear
) {}
