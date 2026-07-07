package com.zylos.backend.features.user.dto;

import com.zylos.backend.features.user.Student;
import com.zylos.backend.features.user.Teacher;
import com.zylos.backend.features.user.User;

public record UserResponse(
    long id,
    String firstName,
    String lastName,
    String email,
    String role
) {
    public UserResponse(User user) {
        this(
            user.getId(), 
            user.getFirstName(), 
            user.getLastName(), 
            user.getEmail(), 
            user instanceof Teacher ?  "INSTRUCTOR" : 
            (user instanceof Student ? "STUDENT" : "UKNOWN")
        );
    }
}
