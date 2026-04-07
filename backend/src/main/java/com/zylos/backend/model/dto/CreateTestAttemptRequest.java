package com.zylos.backend.model.dto;

/**
 * DTO für die Erstellung eines Versuchs.
 */
public record CreateTestAttemptRequest(int nutzerId, int testId) {}