package com.zylos.backend.exception;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record ApiError(
    String message,
    Map<String, String> errors,
    LocalDateTime timestamp
) {}
