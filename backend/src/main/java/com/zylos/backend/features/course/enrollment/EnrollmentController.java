package com.zylos.backend.features.course.enrollment;

import com.zylos.backend.config.web.CurrentUserId;
import com.zylos.backend.features.course.dto.CourseResponse;
import com.zylos.backend.features.course.enrollment.dto.CourseParticipantsResponse;
import com.zylos.backend.features.course.enrollment.dto.EnrollmentRequest;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/{courseId}/enroll")
    public ResponseEntity<Void> enrollCurrentUser(@PathVariable Long courseId, @CurrentUserId long currentUserId) { // FIX: Authentication gelöscht
        enrollmentService.enrollStudent(courseId, currentUserId);
        return ResponseEntity.status(HttpStatus.CREATED).build(); // FIX: Status 201 für Erstellung
    }

    @DeleteMapping("/{courseId}/unenroll")
    public ResponseEntity<Void> unenrollCurrentUser(@PathVariable Long courseId, @CurrentUserId long currentUserId) { // FIX: Authentication gelöscht
        enrollmentService.unenrollUser(courseId, currentUserId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{courseId}/participants")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Void> addParticipant(@PathVariable Long courseId, @Valid @RequestBody EnrollmentRequest request) {
        if (request.role().toUpperCase().contains("INSTRUCTOR")) {
            enrollmentService.assignInstructor(courseId, request.userId());
        } else {
            enrollmentService.enrollStudent(courseId, request.userId());
        }        
        return ResponseEntity.status(HttpStatus.CREATED).build(); // FIX: Status 201 für Erstellung
    }

    @GetMapping("/{courseId}/participants")
    public ResponseEntity<CourseParticipantsResponse> getParticipants(@PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.getCategorizedParticipants(courseId));
    }

    @GetMapping("/my-enrollments")
    public ResponseEntity<List<CourseResponse>> getMyCourses(@CurrentUserId long currentUserId) { // FIX: Authentication gelöscht
        return ResponseEntity.ok(enrollmentService.getEnrolledCourses(currentUserId));
    }
}