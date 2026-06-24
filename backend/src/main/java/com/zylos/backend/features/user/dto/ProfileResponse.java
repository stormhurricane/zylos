package com.zylos.backend.features.user.dto;

public record ProfileResponse(
    long id,
    String firstName,
    String lastName,
    String email,
    String privateAddress,
    String profilePicture,
    String matriculationNumber,
    String studySubject,
    String researchArea,
    String chair
) {}
