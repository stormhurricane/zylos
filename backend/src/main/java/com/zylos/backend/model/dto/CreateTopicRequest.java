package com.zylos.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO für die Erstellung eines Arbeitsthemas.
 */
public record CreateTopicRequest(
    @JsonProperty("lehrendenId") int teacherId,
    @JsonProperty("titel") String title,
    @JsonProperty("beschreibung") String description,
    @JsonProperty("literaturliste") String literatureList
) {}
