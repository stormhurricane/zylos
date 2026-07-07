package com.zylos.backend.features.course.dto;

import com.zylos.backend.features.course.CourseType;
import com.zylos.backend.features.course.SemesterTerm;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public record CourseRequest(
    @NotBlank String title,
    @NotNull CourseType type,
    @NotNull SemesterTerm term,

    @NotBlank 
    @Pattern(regexp = "^\\d{4}(/\\d{4})?$", message = "Academic year must be in format YYYY or YYYY/YYYY")
    String academicYear
) {}
