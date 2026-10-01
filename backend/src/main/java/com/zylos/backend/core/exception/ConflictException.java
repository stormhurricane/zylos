package com.zylos.backend.core.exception;

import java.util.Collections;
import java.util.Map;

public class ConflictException extends RuntimeException {

    private final Map<String, String> errors;

    public ConflictException(String message){
        super(message);
        this.errors = Collections.emptyMap();
    }

    public ConflictException(String fieldname, String message){
        super(message);
        this.errors = Map.of(fieldname, message); 
    }

    public ConflictException(Map<String, String> errors, String defaultMessage){
        super(defaultMessage);
        this.errors = UnmodifiableMapOrCopy(errors);
    }

    private static Map<String, String> UnmodifiableMapOrCopy(Map<String,String> map){
        return map != null ? Map.copyOf(map) : Collections.emptyMap();
    }

    public Map<String,String> getErrors(){
        return errors;
    }
}
