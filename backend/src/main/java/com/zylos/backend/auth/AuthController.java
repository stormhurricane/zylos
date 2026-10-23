package com.zylos.backend.auth;

import com.zylos.backend.auth.dto.AuthResult;
import com.zylos.backend.auth.dto.LoginRequest;
import com.zylos.backend.auth.dto.RegisterRequest;
import com.zylos.backend.auth.dto.UserResponse;
import com.zylos.backend.core.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor 
@Slf4j 
@RestController 
@RequestMapping ("api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping ("/register")
    public ResponseEntity<Void> register(@Valid @RequestBody RegisterRequest request) {
        log.debug("Registering user: {}", request);
        authService.register(request);
        return ResponseEntity.noContent().build(); // 204 instead of 201, because no URI returns
    }

    @PostMapping ("/login")
    public ResponseEntity<UserResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthResult authResult = authService.login(request);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, authResult.cookie().toString())
                .body(authResult.userresponse());
    }

    @PostMapping ("/logout")
    public ResponseEntity<Void> logout() {
        ResponseCookie cleanCookie = authService.logout();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cleanCookie.toString())
                .build();
    }

    @GetMapping ("/me")
    public ResponseEntity<UserResponse> 
            getCurrentUser(@AuthenticationPrincipal CustomUserDetails userDetails) {
        return ResponseEntity.ok(new UserResponse(
            userDetails.getId(),
            userDetails.getUsername(),
            userDetails.getEmail(),
            userDetails.getRoles()
        ));
    }
    
}