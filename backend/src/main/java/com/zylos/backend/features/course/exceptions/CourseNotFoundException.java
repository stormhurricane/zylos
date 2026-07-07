package com.zylos.backend.features.course.exceptions;

public class CourseNotFoundException extends RuntimeException {
    public CourseNotFoundException(Long id) {
        super("Course with ID " + id + " not found.");
    }
}