package com.zylos.backend.model.dto;

import com.zylos.backend.model.entity.CourseType;
import com.zylos.backend.model.entity.SemesterTerm;

public record CourseResponse(
    Long id,
    String title,
    CourseType type,
    SemesterTerm term,
    String academicYear
) {}
