package com.zylos.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zylos.backend.database.Frage; // Annahme: Dies ist die Entitätsklasse

/**
 * DTO für die Rückgabe von Fragen-Details.
 */
@Deprecated(since = "2024-06", forRemoval = true)
public record QuestionResponse(
    int id,
    @JsonProperty("frageText") String questionText,
    @JsonProperty("antwortA") String answerA,
    @JsonProperty("antwortB") String answerB,
    @JsonProperty("antwortC") String answerC,
    @JsonProperty("antwortD") String answerD,
    @JsonProperty("korrekteAntwort") char correctAnswer
) {
    // Convenience-Konstruktor zum Mappen von Frage-Entität zu DTO
    public QuestionResponse(Frage frage) {
        this(frage.getId(), frage.getFrage(), frage.getAntwortA(), frage.getAntwortB(), frage.getAntwortC(), frage.getAntwortD(), frage.getLoesung());
    }
}
