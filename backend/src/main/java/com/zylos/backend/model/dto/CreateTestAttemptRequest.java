package com.zylos.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO für die Erstellung eines Versuchs.
 */
@Deprecated(since = "2024-06", forRemoval = true)
public record CreateTestAttemptRequest(
    @JsonProperty("nutzerId") int userId,
    int testId
) {}