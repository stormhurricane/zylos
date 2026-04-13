package com.zylos.backend.model.dto;

/**
 * Contains only editable fields according to requirements.
 */
public record ProfileUpdateRequest(
    String password,
    String profilePicture,
    String privateAddress,
    String chair,
    String researchArea,
    String studySubject
) {}
