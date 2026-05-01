package com.zylos.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO für die Suche nach einer Lehrveranstaltung.
 */
@Deprecated(since = "2024-06", forRemoval = true)
public record CourseSearchRequest(
    @JsonProperty("titel") String title,
    @JsonProperty("semesterZeit") String semesterTime,
    @JsonProperty("semesterJahr") String semesterYear,
    @JsonProperty("typ") String type
) {}
