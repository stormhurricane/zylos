package com.zylos.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO für die Rückgabe von Termindaten (Antwort an das Frontend).
 * Interne Felder auf Englisch, JSON-Keys auf Deutsch.
 */
@Deprecated(since = "2024-06", forRemoval = true)
public record TerminResponse(
    int id,
    @JsonProperty("lvId") int courseId,
    @JsonProperty("jahr") String year,
    @JsonProperty("monat") String month,
    @JsonProperty("tag") String day,
    @JsonProperty("uhrzeit") String time,
    @JsonProperty("betreff") String subject
) {}
