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

import com.zylos.backend.features.course.exceptions.CourseAlreadyExistsException;
import com.zylos.backend.features.course.exceptions.CourseNotFoundException;
import com.zylos.backend.features.user.exceptions.BadCredentialsException;
import com.zylos.backend.features.user.exceptions.EmailAlreadyExistsException;
import com.zylos.backend.features.user.exceptions.UserNotFoundException;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import javax.naming.AuthenticationException;

@ControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    // FIX: Matcht jetzt perfekt deinen ApiError-Record für 404 Fehler
    @ExceptionHandler({CourseNotFoundException.class/*, UserNotFoundException.class*/})
    public ResponseEntity<ApiError> handleNotFoundException(RuntimeException ex) {
        ApiError error = new ApiError(
                ex.getMessage(),
                null, // Keine spezifischen Feldfehler bei 404
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }

    // FIX: Matcht jetzt perfekt deinen ApiError-Record für 409 Konflikte
    @ExceptionHandler({CourseAlreadyExistsException.class/*, EmailAlreadyExistsException.class*/})
    public ResponseEntity<ApiError> handleConflictException(RuntimeException ex) {
        ApiError error = new ApiError(
                ex.getMessage(),
                null, // Keine spezifischen Feldfehler bei 409
                LocalDateTime.now()
        );
        return new ResponseEntity<>(error, HttpStatus.CONFLICT);
    }

    // 3. HTTP 401 Unauthorized - Für falsche Login-Daten
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ApiError> handleBadCredentials(BadCredentialsException ex) {
        ApiError error = new ApiError(
            ex.getMessage(),
            null,
            LocalDateTime.now()
        );
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(error);
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

    // catches errors from SecurityConfig.authenticationEntryPoint (no token or broken)
    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ApiError> handleAuthenticationException(AuthenticationException ex) {
        logger.warn("Authentication failed: {}", ex.getMessage());
        return createResponse("Authentification failed: invalid or missing token.", null, HttpStatus.UNAUTHORIZED);
    }

    // Catches Spring Security AccessDeniedException to return 403 Forbidden
    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ApiError> handleAccessDeniedException(AccessDeniedException ex) {
        logger.warn("Access denied: {}", ex.getMessage());
        return createResponse("Zugriff verweigert: Sie haben nicht die erforderlichen Rechte.", null, HttpStatus.FORBIDDEN);
    }

    // Special Case for @Valid annotated login endpoint, where we want to return 401 instead of 400 for invalid credentials
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiError> handleIllegalArgument(IllegalArgumentException ex) {
        logger.warn("Illegal argument encountered: {}", ex.getMessage());
        return createResponse(ex.getMessage(), null, HttpStatus.UNAUTHORIZED);
    }

    // Catches all general RuntimeExceptions (e.g. Course not found)
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<ApiError> handleRuntimeException(RuntimeException ex) {
        logger.warn("Business or runtime exception occurred: {}", ex.getMessage());
        return createResponse(ex.getMessage(), null, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiError> handleAllUncaughtExceptions(Exception ex) {
        logger.error("An unexpected server error occurred: ", ex);
        return createResponse("An internal server error occurred.", null, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ApiError> createResponse(String message, Map<String, String> errors, HttpStatus status) {
        ApiError apiError = new ApiError(message, errors, LocalDateTime.now());
        return new ResponseEntity<>(apiError, status);
    }


}
