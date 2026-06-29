package com.zylos.backend.features.course.material.dto;

import java.time.LocalDateTime;

public record MaterialResponse(
    Long id,
    String title,
    String fileName,
    String contentType,
    long fileSize,          
    LocalDateTime createdAt 
) {}