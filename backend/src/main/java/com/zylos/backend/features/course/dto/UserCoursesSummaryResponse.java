package com.zylos.backend.features.course.dto;

import java.util.List;

public record UserCoursesSummaryResponse(
    List<CourseResponse> teachingCourses, 
    List<CourseResponse> enrolledCourses  
) {}