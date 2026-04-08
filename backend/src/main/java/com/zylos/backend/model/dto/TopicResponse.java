package com.zylos.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zylos.backend.database.ArbeitsThema;

/**
 * DTO für die Rückgabe eines Arbeitsthemas.
 */
public record TopicResponse(
    int id,
    @JsonProperty("lehrendenId") int teacherId,
    @JsonProperty("titel") String title,
    @JsonProperty("beschreibung") String description,
    @JsonProperty("literaturliste") String literatureList
) {
    // Optional: Convenience-Konstruktor zum Mappen von Entity zu DTO
    public TopicResponse(ArbeitsThema arbeitsThema) {
        this(arbeitsThema.getId(), arbeitsThema.getLehrendenId(), arbeitsThema.getTitel(), arbeitsThema.getBeschreibung(), arbeitsThema.getLiteraturliste());
    }
}