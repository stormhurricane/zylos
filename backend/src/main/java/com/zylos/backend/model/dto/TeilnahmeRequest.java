package com.zylos.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO für das Hinzufügen eines Teilnehmers.
 */
public record TeilnahmeRequest(
    @JsonProperty("nutzerId") int userId,
    @JsonProperty("lehrveranstaltungsId") int courseId
) {}
