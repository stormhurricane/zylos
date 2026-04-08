package com.zylos.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO für die Erstellung von Bewertungs-Feedback.
 * Enthält die Daten, die für jedes einzelne Feedback-Element benötigt werden.
 */
public record CreateEvaluationFeedbackRequest(
    @JsonProperty("versuchId") int attemptId,
    @JsonProperty("frageId") int questionId,
    @JsonProperty("abgegebeneAntwort") boolean givenAnswerIsCorrect,
    @JsonProperty("antwort") char selectedAnswer
) {}
