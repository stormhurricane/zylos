package com.zylos.backend.features.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * DTO für die Antwort auf eine Freundschaftsanfrage (Annahme oder Ablehnung).
 */
@Deprecated(since = "2024-06", forRemoval = true)
public record FriendshipDecisionRequest(
    @JsonProperty("zuBearbeitendeAnfrage") int[] userIds,
    @JsonProperty("wirdAngenommen") boolean accepted
) {}