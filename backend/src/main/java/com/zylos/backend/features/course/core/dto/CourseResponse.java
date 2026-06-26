package com.zylos.backend.features.course.core.dto;

import com.zylos.backend.features.course.core.CourseType;
import com.zylos.backend.features.course.core.SemesterTerm;

public record CourseResponse(
    Long id,
    String title,
    CourseType type,
    SemesterTerm term,
    String academicYear
) {}
