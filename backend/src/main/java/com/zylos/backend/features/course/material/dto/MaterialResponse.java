package com.zylos.backend.features.course.material.dto;

public record MaterialResponse(
    Long id,
    String title,
    String fileName,
    String contentType
) {}
