package com.zylos.backend.features.course.dto;

import com.zylos.backend.features.course.CourseType;
import com.zylos.backend.features.course.SemesterTerm;

public record CourseResponse(
    Long id,
    String title,
    CourseType type,
    SemesterTerm term,
    String academicYear
) {}
