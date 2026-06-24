package com.zylos.backend.features.user.dto;

public record ProfileUpdateRequest(
    String password,
    String profilePicture,
    String privateAddress,
    String chair,
    String researchArea,
    String studySubject
) {}
