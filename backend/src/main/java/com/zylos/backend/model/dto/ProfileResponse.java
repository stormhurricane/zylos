package com.zylos.backend.model.dto;

public record ProfileResponse(
    int id,
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
