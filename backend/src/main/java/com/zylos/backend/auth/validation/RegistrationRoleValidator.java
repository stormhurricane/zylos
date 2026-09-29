package com.zylos.backend.auth.validation;

import java.util.Set;

import com.zylos.backend.features.user.SystemRole;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class RegistrationRoleValidator implements ConstraintValidator<ValidRegistrationRole, SystemRole> {

    private static final Set<SystemRole> ALLOWED_ROLES = Set.of(
            SystemRole.STUDENT,
            SystemRole.TEACHER
    );

    @Override 
    public boolean isValid(SystemRole role, ConstraintValidatorContext context){
        if(role == null){
            return true;
        }

        return ALLOWED_ROLES.contains(role);
    }
    
}
