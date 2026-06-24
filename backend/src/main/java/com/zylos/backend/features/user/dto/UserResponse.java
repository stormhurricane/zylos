package com.zylos.backend.features.user.dto;

import com.zylos.backend.features.user.User;

public record UserResponse(
    long id,
    String firstName,
    String lastName,
    String email
) {
    public UserResponse(User user) {
        this(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }
}
