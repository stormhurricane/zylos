package com.zylos.backend.features.course;

import com.zylos.backend.config.web.CurrentUserId;
import com.zylos.backend.features.course.dto.CourseRequest;
import com.zylos.backend.features.course.dto.CourseResponse;
import com.zylos.backend.features.course.material.CourseMaterialService;
import com.zylos.backend.features.course.material.dto.MaterialDownloadResponse;
import com.zylos.backend.features.course.material.dto.MaterialResponse;

import jakarta.validation.Valid;

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
public class CourseController {

    private final CourseService courseService;
    private final CourseMaterialService materialService;
    private final CourseCreationOrchestrator courseOrchestrator;

    public CourseController(
            CourseService courseService, 
            CourseMaterialService materialService, 
            CourseCreationOrchestrator courseOrchestrator) {
        this.courseService = courseService;
        this.materialService = materialService;
        this.courseOrchestrator = courseOrchestrator;
    }

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
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<MaterialResponse> uploadMaterial(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("file") MultipartFile file) throws IOException { 
        return ResponseEntity.ok(materialService.uploadMaterial(id, file, title));
    }

    @GetMapping("/materials/{materialId}/download")
    public ResponseEntity<Resource> downloadMaterial(@PathVariable Long materialId) {
        MaterialDownloadResponse material = materialService.downloadMaterial(materialId);
        
        ContentDisposition contentDisposition = ContentDisposition.attachment()
                .filename(material.fileName(), StandardCharsets.UTF_8)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
                .contentType(MediaType.parseMediaType(material.contentType())) 
                .body(material.resource());
    }
}