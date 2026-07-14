package com.zylos.backend.features.projectgroup.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class TodoNotFoundException extends RuntimeException {
    public TodoNotFoundException(Long todoId) {
        super("Todo with ID " + todoId + " not found.");
    }

    public TodoNotFoundException(String message) {
        super(message);
    }
}