package com.zylos.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO für die Anforderung eines neuen Bewertungs-Versuchs.
 */
public record CreateEvaluationAttemptRequest(
    @JsonProperty("lvId") int courseId
) {}