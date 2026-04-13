package com.zylos.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO für die Erstellung eines Versuchs.
 */
public record CreateTestAttemptRequest(
    @JsonProperty("nutzerId") int userId,
    int testId
) {}