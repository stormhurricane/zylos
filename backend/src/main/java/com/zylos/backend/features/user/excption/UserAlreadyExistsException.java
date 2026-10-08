package com.zylos.backend.features.user.excption;

import java.util.Map;

import com.zylos.backend.core.exception.ConflictException;

public class UserAlreadyExistsException extends ConflictException {

    public UserAlreadyExistsException(String fieldName, String message){
        super(fieldName, message);
    }

    public UserAlreadyExistsException(Map<String,String> errors, String message){
        super(errors, message);
    }
}
