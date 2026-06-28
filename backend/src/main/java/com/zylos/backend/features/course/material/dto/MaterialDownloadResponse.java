package com.zylos.backend.features.course.material.dto;

public record MaterialDownloadResponse(
    String fileName,
    String contentType,
    byte[] data
) {}