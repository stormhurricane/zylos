package com.zylos.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zylos.backend.database.Frage;
import java.util.List;

/**
 * DTO für die Erstellung einer Lehrveranstaltungs-Bewertung.
 */
@Deprecated(since = "2024-06", forRemoval = true)
public record CreateCourseEvaluationRequest(
    @JsonProperty("lvId") int courseId,
    String name,
    @JsonProperty("fragen") List<Frage> questions
) {}