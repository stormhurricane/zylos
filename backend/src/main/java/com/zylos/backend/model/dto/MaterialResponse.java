package com.zylos.backend.model.dto;

public record MaterialResponse(
    Long id,
    String title,
    String fileName,
    String contentType
) {}
