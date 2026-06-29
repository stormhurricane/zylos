package com.zylos.backend.features.course.enrollment.exceptions;

public class InvalidRoleForEnrollmentException extends RuntimeException {
    public InvalidRoleForEnrollmentException(long userId, String expectedRole) {
        super("User with ID " + userId + " cannot be processed. Expected role: " + expectedRole);
    }
}