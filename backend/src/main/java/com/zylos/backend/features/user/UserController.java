package com.zylos.backend.features.user;

import com.zylos.backend.core.security.CustomUserDetails;
import com.zylos.backend.features.user.dto.UserPrivateResponse;
import com.zylos.backend.features.user.dto.UserPublicResponse;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController  
@AllArgsConstructor 
@Slf4j 
@RequestMapping ("/api/v1/users")
public class UserController {
    
    private final UserService userService;

    @GetMapping ("/me")
    public ResponseEntity<UserPrivateResponse> getPrivateProfile(
            @AuthenticationPrincipal CustomUserDetails customUserDetails) {
        UUID id = customUserDetails.getId();
        log.info("Loading private profile for %s", id);
        UserPrivateResponse profile = userService.getPrivateProfile(id);
        return ResponseEntity.ok(profile);
    }

    @GetMapping ("/{id}")
    public ResponseEntity<UserPublicResponse> getPublicProfile(@PathVariable UUID id) {
        UserPublicResponse profile = userService.getPublicProfile(id);
        return ResponseEntity.ok(profile);
    }
}
