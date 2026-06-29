package com.zylos.backend.features.course.enrollment.exceptions;

public class EnrollmentUserNotFoundException extends RuntimeException {
    public EnrollmentUserNotFoundException(long userId) {
        super("Cannot enroll user. User with ID " + userId + " does not exist.");
    }
}