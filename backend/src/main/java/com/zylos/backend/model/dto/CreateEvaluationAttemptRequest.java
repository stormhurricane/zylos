package com.zylos.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO für die Anforderung eines neuen Bewertungs-Versuchs.
 */
@Deprecated(since = "2024-06", forRemoval = true)
public record CreateEvaluationAttemptRequest(
    @JsonProperty("lvId") int courseId
) {}