package com.zylos.backend.controller;

import com.zylos.backend.model.dto.CourseParticipantsResponse;
import com.zylos.backend.model.dto.EnrollmentRequest;
import com.zylos.backend.service.EnrollmentService;
import com.zylos.backend.service.UserService;
import com.zylos.backend.model.dto.CourseResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class EnrollmentController {

    private static final Logger logger = LoggerFactory.getLogger(EnrollmentController.class);
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

    @PostMapping("/{courseId}/participants")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Void> addParticipant(@PathVariable Long courseId, @RequestBody EnrollmentRequest request) {
        enrollmentService.enrollUser(courseId, request.userId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{courseId}/participants")
    public ResponseEntity<CourseParticipantsResponse> getParticipants(@PathVariable Long courseId) {
        logger.info("Fetching participants for course ID: {}", courseId);
        return ResponseEntity.ok(enrollmentService.getCategorizedParticipants(courseId));
    }

    @GetMapping("/my-enrollments")
    public ResponseEntity<List<CourseResponse>> getMyCourses() {
        int currentUserId = userService.getCurrentUserId();
        return ResponseEntity.ok(enrollmentService.getEnrolledCourses(currentUserId));
    }
}
