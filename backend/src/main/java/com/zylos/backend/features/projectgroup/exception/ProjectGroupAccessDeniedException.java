package com.zylos.backend.features.projectgroup.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN) // Mappt automatisch auf HTTP 403 Forbidden
public class ProjectGroupAccessDeniedException extends RuntimeException {
    
    public ProjectGroupAccessDeniedException(Long groupId, Long userId) {
        super(String.format("User with ID %d does not have access to project group with ID %d.", userId, groupId));
    }
}