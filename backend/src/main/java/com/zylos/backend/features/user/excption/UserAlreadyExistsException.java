package com.zylos.backend.features.user.excption;

import com.zylos.backend.core.exception.ConflictException;

public class UserAlreadyExistsException extends ConflictException {

    public UserAlreadyExistsException(String message){
        super(message);
    }
    
}
