package com.zylos.backend.config.security;

public enum Role {
    STUDENT,
    INSTRUCTOR;

    public String getAuthority() {
        return "ROLE_" + this.name();
    }
}