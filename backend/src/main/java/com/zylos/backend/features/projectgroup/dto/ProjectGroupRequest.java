package com.zylos.backend.features.projectgroup.dto;

import jakarta.validation.constraints.NotBlank;

public record ProjectGroupRequest(
    @NotBlank(message = "Der Titel darf nicht leer sein.") 
    String title,
    
    Long courseId // Optiona: Can be null, if unrelated to course 
) {}