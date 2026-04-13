package com.zylos.backend.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO für die Antwort auf eine Freundschaftsanfrage (Annahme oder Ablehnung).
 */
public record FriendshipDecisionRequest(
    @JsonProperty("zuBearbeitendeAnfrage") int[] userIds,
    @JsonProperty("wirdAngenommen") boolean accepted
) {}