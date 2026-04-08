package com.zylos.backend.model.dto;

/**
 * DTO für die statistische Auswertung einer einzelnen Bewertungsfrage.
 * Ersetzt das bisherige int-Array.
 */
public record EvaluationStatisticResponse(
    int questionId,
    int countA,
    int countB,
    int countC,
    int countD
) {}
