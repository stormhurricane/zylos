package com.zylos.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO für die Suche nach einer Lehrveranstaltung.
 */
public record CourseSearchRequest(
    @JsonProperty("titel") String title,
    @JsonProperty("semesterZeit") String semesterTime,
    @JsonProperty("semesterJahr") String semesterYear,
    @JsonProperty("typ") String type
) {}
