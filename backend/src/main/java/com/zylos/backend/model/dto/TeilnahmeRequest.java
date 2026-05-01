package com.zylos.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO für das Hinzufügen eines Teilnehmers.
 */
@Deprecated(since = "2024-06", forRemoval = true)
public record TeilnahmeRequest(
    @JsonProperty("nutzerId") int userId,
    @JsonProperty("lehrveranstaltungsId") int courseId
) {}
