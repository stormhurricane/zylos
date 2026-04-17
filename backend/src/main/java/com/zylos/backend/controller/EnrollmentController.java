package com.zylos.backend.controller;

import com.zylos.backend.model.dto.CourseParticipantsResponse;
import com.zylos.backend.service.EnrollmentService;
import com.zylos.backend.service.UserService;
import com.zylos.backend.model.dto.CourseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final UserService userService;

    public EnrollmentController(EnrollmentService enrollmentService, UserService userService) {
        this.enrollmentService = enrollmentService;
        this.userService = userService;
    }

    @PostMapping("/{courseId}/enroll")
    public ResponseEntity<Void> enrollCurrentUser(@PathVariable Long courseId) {
        int currentUserId = userService.getCurrentUserId();
        enrollmentService.enrollUser(courseId, currentUserId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{courseId}/unenroll")
    public ResponseEntity<Void> unenrollCurrentUser(@PathVariable Long courseId) {
        int currentUserId = userService.getCurrentUserId();
        enrollmentService.unenrollUser(courseId, currentUserId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{courseId}/participants")
    public ResponseEntity<CourseParticipantsResponse> getParticipants(@PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.getCategorizedParticipants(courseId));
    }

    @GetMapping("/my-enrollments")
    public ResponseEntity<List<CourseResponse>> getMyCourses() {
        int currentUserId = userService.getCurrentUserId();
        return ResponseEntity.ok(enrollmentService.getEnrolledCourses(currentUserId));
    }
}
