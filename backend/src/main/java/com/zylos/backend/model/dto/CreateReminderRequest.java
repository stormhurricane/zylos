package com.zylos.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zylos.backend.database.Reminder.FormEnum;

/**
 * DTO für die Erstellung eines Reminders für einen Termin.
 */
@Deprecated(since = "2024-06", forRemoval = true)
public record CreateReminderRequest(
    @JsonProperty("terminId") int appointmentId,
    @JsonProperty("jahr") String year,
    @JsonProperty("monat") String month,
    @JsonProperty("tag") String day,
    @JsonProperty("uhrzeit") String time,
    @JsonProperty("form") FormEnum form
) {}
