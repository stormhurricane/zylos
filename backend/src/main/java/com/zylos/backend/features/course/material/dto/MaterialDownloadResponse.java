package com.zylos.backend.features.course.material.dto;

import org.springframework.core.io.Resource;

public record MaterialDownloadResponse(
    String fileName,
    String contentType,
    Resource resource
) {}