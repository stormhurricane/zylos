package com.zylos.backend.features.course.staff;

import com.zylos.backend.config.web.CurrentUserId;
import com.zylos.backend.features.course.CourseService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/courses/{courseId}/staff")
@RequiredArgsConstructor
public class CourseStaffController {

    private final CourseStaffService courseStaffService;
    private final CourseService courseService;

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR') and @courseSecurityEvaluator.hasWriteAccess(#courseId, @currentUserId)")
    public ResponseEntity<Void> addStaffMember(
            @PathVariable("courseId") Long courseId, 
            @Valid @RequestBody StaffRequest request,
            @CurrentUserId long currentUserId) {

        courseService.validateCourseExists(courseId); 
        courseStaffService.addStaff(courseId, request.userId(), request.role());       
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/{userId}")
    @PreAuthorize("hasRole('INSTRUCTOR') and @courseSecurityEvaluator.hasWriteAccess(#courseId, @currentUserId)")
    public ResponseEntity<Void> removeStaffMember(
            @PathVariable("courseId") Long courseId, 
            @PathVariable Long userId,
            @CurrentUserId long currentUserId) {
        
        courseService.validateCourseExists(courseId); 
        courseStaffService.removeStaff(courseId, userId);
        return ResponseEntity.noContent().build();
    }
}