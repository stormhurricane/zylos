package com.zylos.backend.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // Catches all general RuntimeExceptions (e.g. Course not found)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntimeException(RuntimeException ex) {
        logger.error("Unexpected runtime exception occurred: ", ex);
        return createResponse(ex.getMessage(), null, HttpStatus.BAD_REQUEST);
    }

    // Catches validation errors that occur due to @Valid
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiError> handleValidationExceptions(MethodArgumentNotValidException ex) {
        logger.warn("Validation failed for request: {}", ex.getBindingResult().getObjectName());
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> 
            errors.put(error.getField(), error.getDefaultMessage())
        );
        return createResponse("Validation failed", errors, HttpStatus.BAD_REQUEST);
    }

    // Special Case for @Valid annotated login endpoint, where we want to return 401 instead of 400 for invalid credentials
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex) {
        logger.warn("Illegal argument encountered: {}", ex.getMessage());
        return createResponse(ex.getMessage(), null, HttpStatus.UNAUTHORIZED);
    }

    // Catches Spring Security AccessDeniedException to return 403 Forbidden
    // Catches Spring Security AccessDeniedException to return 403 Forbidden
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException(AccessDeniedException ex) {
        logger.warn("Access denied: {}", ex.getMessage());
        return createResponse("Zugriff verweigert: Sie haben nicht die erforderlichen Rechte.", null, HttpStatus.FORBIDDEN);
    }

    private ResponseEntity<ApiError> createResponse(String message, Map<String, String> errors, HttpStatus status) {
        ApiError apiError = new ApiError(message, errors, LocalDateTime.now());
        return new ResponseEntity<>(apiError, status);
    }


}
