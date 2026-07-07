package com.zylos.backend.features.course.dto;

import com.zylos.backend.features.course.Course;
import com.zylos.backend.features.course.CourseType;
import com.zylos.backend.features.course.SemesterTerm;

public record CourseResponse(
    Long id,
    String title,
    CourseType type,
    SemesterTerm term,
    String academicYear,
    int participantCount
) {
    public CourseResponse(Course course) {
        this(
            course.getId(),
            course.getTitle(),
            course.getType(),
            course.getTerm(),
            course.getAcademicYear(),
            course.getEnrollments() != null ? course.getEnrollments().size() : 0
        );
    }
}