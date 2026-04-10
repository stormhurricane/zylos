package com.zylos.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO für die Prüfung der Beziehung zwischen Student und Lehrendem.
 */
public record CheckTeilnahmeRequest(
    @JsonProperty("studentenId") int studentId,
    @JsonProperty("lehrendenId") int teacherId
) {}
