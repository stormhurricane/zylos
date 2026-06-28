package com.zylos.backend.features.course.exceptions;

public class CourseAlreadyExistsException extends RuntimeException {
    public CourseAlreadyExistsException(String title) {
        super("Course with title '" + title + "' already exists.");
    }
}