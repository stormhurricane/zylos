package com.zylos.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zylos.backend.database.Nutzer;

/**
 * DTO für Nutzerinformationen, das den alten NutzerWrapper ersetzt.
 */
@Deprecated(since = "2024-06", forRemoval = true)
public record NutzerResponse(
    int id,
    @JsonProperty("vorname") String firstName,
    @JsonProperty("nachname") String lastName,
    String email
) {
        /**
     * Convenience-Konstruktor zum Mapping von der Nutzer-Entität.
     */
    public NutzerResponse(Nutzer nutzer) {
        this(nutzer.getId(), nutzer.getVorname(), nutzer.getNachname(), nutzer.getEmail());
    }
}
