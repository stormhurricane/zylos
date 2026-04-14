package com.zylos.backend.model.dto;

import com.zylos.backend.model.entity.CourseType;
import com.zylos.backend.model.entity.SemesterTerm;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CourseRequest(
    @NotBlank String title,
    @NotNull CourseType type,
    @NotNull SemesterTerm term,
    @NotBlank String academicYear
) {}
