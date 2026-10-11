package com.zylos.backend.auth.dto;

import org.springframework.http.ResponseCookie;

public record AuthResult(
    ResponseCookie cookie,
    UserResponse userresponse
) {}
