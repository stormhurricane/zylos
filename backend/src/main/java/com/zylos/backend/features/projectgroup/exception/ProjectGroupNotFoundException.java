package com.zylos.backend.features.projectgroup.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class ProjectGroupNotFoundException extends RuntimeException {
    
    public ProjectGroupNotFoundException(Long groupId) {
        super(String.format("Project group with ID %d could not be found.", groupId));
    }
}