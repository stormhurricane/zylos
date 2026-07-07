package com.zylos.backend.features.course;

import com.zylos.backend.config.security.CourseSecurityEvaluator;
import com.zylos.backend.config.web.CurrentUserId;
import com.zylos.backend.features.course.dto.CourseRequest;
import com.zylos.backend.features.course.dto.CourseResponse;
import com.zylos.backend.features.course.dto.UserCoursesSummaryResponse;
import com.zylos.backend.features.course.enrollment.EnrollmentRole;
import com.zylos.backend.features.course.enrollment.EnrollmentService;
import com.zylos.backend.features.course.enrollment.dto.CourseParticipantsResponse;
import com.zylos.backend.features.course.enrollment.dto.EnrollmentRequest;
import com.zylos.backend.features.course.material.CourseMaterialService;
import com.zylos.backend.features.course.material.dto.MaterialDownloadResponse;
import com.zylos.backend.features.course.material.dto.MaterialResponse;
import com.zylos.backend.features.course.staff.CourseStaffService;
import com.zylos.backend.features.course.staff.StaffRole;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

import org.springframework.core.io.Resource;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final CourseMaterialService materialService;
    private final EnrollmentService enrollmentService;
    private final CourseStaffService courseStaffService;
    private final CourseCreationOrchestrator courseOrchestrator;
    private final CourseSecurityEvaluator courseSecurityEvaluator;

    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Long id) {
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CourseResponse>> search(@RequestParam String title) {
        return ResponseEntity.ok(courseService.searchByTitle(title));
    }

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<CourseResponse> create(@Valid @RequestBody CourseRequest request, @CurrentUserId long currentUserId) {
        CourseResponse course = courseOrchestrator.createCourseWithInstructor(request, currentUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(course);
    }

    @PostMapping("/import")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<List<CourseResponse>> importCsv(@RequestParam("file") MultipartFile file, @CurrentUserId long currentUserId) {
        List<CourseResponse> importedCourses = courseOrchestrator.importFromCsvWithInstructor(file, currentUserId);
        return ResponseEntity.status(HttpStatus.CREATED).body(importedCourses);
    }

    @GetMapping("/{id}/materials")
    public ResponseEntity<List<MaterialResponse>> getMaterials(@PathVariable Long id) {
        return ResponseEntity.ok(materialService.getMaterialsForCourse(id));
    }

    @PostMapping("/{id}/materials")
    @PreAuthorize("hasRole('INSTRUCTOR')") // Nur noch die Rollenprüfung deklarativ
    public ResponseEntity<MaterialResponse> uploadMaterial(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("file") MultipartFile file,
            @CurrentUserId long currentUserId) throws IOException { 
        
        return ResponseEntity.ok(materialService.uploadMaterialSecure(id, file, title, currentUserId));
    }

    @GetMapping("/materials/{materialId}/download")
    public ResponseEntity<Resource> downloadMaterial(@PathVariable Long materialId, @CurrentUserId long currentUserId) {
        MaterialDownloadResponse material = materialService.downloadMaterialSecure(materialId, currentUserId);

        ContentDisposition contentDisposition = ContentDisposition.attachment()
                .filename(material.fileName(), StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
                .contentType(MediaType.parseMediaType(material.contentType())) 
                .body(material.resource());
    }

    @GetMapping("/my-courses")
    public ResponseEntity<UserCoursesSummaryResponse> getMyCourses(@CurrentUserId long currentUserId) {
        return ResponseEntity.ok(courseService.getMyCoursesSummary(currentUserId));
    }

    @GetMapping("/{courseId}/participants")
    // @PreAuthorize("@courseSecurityEvaluator.hasReadAccess(#courseId, @currentUserId)")
    public ResponseEntity<CourseParticipantsResponse> getParticipants(@PathVariable("courseId") Long courseId, @CurrentUserId long currentUserId) {

        boolean hasAccess = courseSecurityEvaluator.hasReadAccess(courseId, currentUserId);
        
        if (!hasAccess) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        return ResponseEntity.ok(courseService.getCategorizedParticipants(courseId));
    }

   @PostMapping("/{courseId}/participants")
    public ResponseEntity<Void> addParticipant(
            @PathVariable Long courseId,
            @Valid @RequestBody EnrollmentRequest request) {
        
        long targetUserId = request.userId();
        
        if (request.role() == EnrollmentRole.STUDENT) {
            enrollmentService.addEnrollment(courseId, targetUserId);
        } else if (request.role() == EnrollmentRole.INSTRUCTOR) {
            courseStaffService.addStaff(courseId, targetUserId, StaffRole.EDITOR);
        } else {
            throw new IllegalArgumentException("Unsupportete Rolle für diesen Kurs-Endpunkt");
        }
        
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}