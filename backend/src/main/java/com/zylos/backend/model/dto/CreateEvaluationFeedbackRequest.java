package com.zylos.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO für die Erstellung von Bewertungs-Feedback.
 * Enthält die Daten, die für jedes einzelne Feedback-Element benötigt werden.
 */
@Deprecated(since = "2024-06", forRemoval = true)
public record CreateEvaluationFeedbackRequest(
    @JsonProperty("versuchId") int attemptId,
    @JsonProperty("frageId") int questionId,
    @JsonProperty("abgegebeneAntwort") boolean givenAnswerIsCorrect,
    @JsonProperty("antwort") char selectedAnswer
) {}
