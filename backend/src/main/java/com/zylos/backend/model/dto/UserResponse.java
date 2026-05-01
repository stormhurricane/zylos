package com.zylos.backend.model.dto;

import com.zylos.backend.model.entity.User;

public record UserResponse(
    int id,
    String firstName,
    String lastName,
    String email
) {
    public UserResponse(User user) {
        this(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail());
    }
}
