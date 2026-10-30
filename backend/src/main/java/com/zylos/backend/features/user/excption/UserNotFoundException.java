package com.zylos.backend.features.user.excption;

import com.zylos.backend.core.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(String message) {
        super(message);
    }
}
