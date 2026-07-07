package com.zylos.backend.features.user.dto;

public record UserSearchResponse(
    long id,
    String firstName,
    String lastName,
    String profilePicture,
    String subInfo,
    String role
) {}