package com.zylos.backend.features.projectgroup.todo.dto;

import jakarta.validation.constraints.NotBlank;

public record TodoRequest(
    @NotBlank(message = "Todo-Titel darf nicht leer sein.")
    String title,
    Long assignedToUserId
) {}