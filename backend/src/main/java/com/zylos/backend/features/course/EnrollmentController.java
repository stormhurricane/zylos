package com.zylos.backend.features.course;

import com.zylos.backend.config.web.CurrentUserId;
import com.zylos.backend.features.course.dto.CourseParticipantsResponse;
import com.zylos.backend.features.course.dto.CourseResponse;
import com.zylos.backend.features.course.dto.EnrollmentRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    // Der UserService wurde restlos eliminiert!
    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    @PostMapping("/{courseId}/enroll")
    public ResponseEntity<Void> enrollCurrentUser(@PathVariable Long courseId, Authentication authentication, @CurrentUserId long currentUserId) {
        enrollmentService.enrollUser(courseId, currentUserId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{courseId}/unenroll")
    public ResponseEntity<Void> unenrollCurrentUser(@PathVariable Long courseId, Authentication authentication, @CurrentUserId long currentUserId) {
        enrollmentService.unenrollUser(courseId, currentUserId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{courseId}/participants")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Void> addParticipant(@PathVariable Long courseId, @RequestBody EnrollmentRequest request) {
        // [Certain] Achtung: Das EnrollmentRequest-DTO muss in seinem Record ebenfalls userId auf 'long' typisiert haben!
        enrollmentService.enrollUser(courseId, request.userId());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{courseId}/participants")
    public ResponseEntity<CourseParticipantsResponse> getParticipants(@PathVariable Long courseId) {
        return ResponseEntity.ok(enrollmentService.getCategorizedParticipants(courseId));
    }

    @GetMapping("/my-enrollments")
    public ResponseEntity<List<CourseResponse>> getMyCourses(Authentication authentication, @CurrentUserId long currentUserId) {
        return ResponseEntity.ok(enrollmentService.getEnrolledCourses(currentUserId));
    }

}