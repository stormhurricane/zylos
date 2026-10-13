package com.zylos.backend.features.user.excption;

import com.zylos.backend.core.exception.ConflictException;
import java.util.Map;

public class UserAlreadyExistsException extends ConflictException {

    public UserAlreadyExistsException(String fieldName, String message) {
        super(fieldName, message);
    }

    public UserAlreadyExistsException(Map<String, String> errors, String message) {
        super(errors, message);
    }
}
