package com.zylos.backend.controller;

import com.zylos.backend.model.dto.AuthResponse;
import com.zylos.backend.model.dto.LoginRequest;
import com.zylos.backend.model.dto.ProfileResponse;
import com.zylos.backend.model.dto.ProfileUpdateRequest;
import com.zylos.backend.model.dto.StudentRegistrationRequest;
import com.zylos.backend.model.dto.TeacherRegistrationRequest;
import com.zylos.backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register/teacher")
    public ResponseEntity<Void> registerTeacher(@Valid @RequestBody TeacherRegistrationRequest request) {
        userService.registerTeacher(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/register/student")
    public ResponseEntity<Void> registerStudent(@Valid @RequestBody StudentRegistrationRequest request) {
        userService.registerStudent(request);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<ProfileResponse> getProfile() {
        return ResponseEntity.ok(userService.getUserProfile(userService.getCurrentUserId(), true));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProfileResponse> getPublicProfile(@PathVariable("id") int id) {
        return ResponseEntity.ok(userService.getUserProfile(id, false));
    }

    @GetMapping("/search")
    public ResponseEntity<List<ProfileResponse>> searchUsers(@RequestParam("q") String q) {
        return ResponseEntity.ok(userService.searchUsers(q));
    }

    @PutMapping("/me")
    public ResponseEntity<Void> updateProfile(@RequestBody ProfileUpdateRequest request) {
        // Die ID wird hier nicht mehr vom Client geschickt, sondern 
        // sicher vom Service aus dem SecurityContext ermittelt.
        int currentUserId = userService.getCurrentUserId();
        boolean updated = userService.updateProfile(currentUserId, request);

        if (updated) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}
