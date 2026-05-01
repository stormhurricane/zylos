package com.zylos.backend.model.dto;

public record ProfileUpdateRequest(
    String password,
    String profilePicture,
    String privateAddress,
    String chair,
    String researchArea,
    String studySubject
) {}
