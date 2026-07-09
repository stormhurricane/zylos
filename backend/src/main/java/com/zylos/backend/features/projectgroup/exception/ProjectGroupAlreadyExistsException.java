package com.zylos.backend.features.projectgroup.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ProjectGroupAlreadyExistsException extends RuntimeException {
    
    public ProjectGroupAlreadyExistsException(String title) {
        super(String.format("A project group with the title '%s' already exists.", title));
    }
}