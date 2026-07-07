package com.zylos.backend.features.course.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class CourseAccessDeniedException extends RuntimeException {
    
    public CourseAccessDeniedException(Long courseId, long userId) {
        super(String.format("User with ID %d does not have administrative write access to course with ID %d.", userId, courseId));
    }
}