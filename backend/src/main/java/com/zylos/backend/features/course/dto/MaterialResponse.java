package com.zylos.backend.features.course.dto;

public record MaterialResponse(
    Long id,
    String title,
    String fileName,
    String contentType
) {}
