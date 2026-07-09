package com.zylos.backend.features.projectgroup.dto;

import java.time.LocalDateTime;

public record ProjectGroupResponse(
    Long id,
    String title,
    Long courseId,
    Long createdBy,
    LocalDateTime createdAt
) {}