package com.zylos.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO für die Erstellung eines neuen Termins.
 */
public record CreateAppointmentRequest(
    @JsonProperty("lvId") int courseId,
    @JsonProperty("jahr") String year,
    @JsonProperty("monat") String month,
    @JsonProperty("tag") String day,
    @JsonProperty("uhrzeit") String time,
    @JsonProperty("betreff") String subject
) {}
