package com.zylos.backend.controller;

import com.zylos.backend.model.dto.CourseRequest;
import com.zylos.backend.model.dto.CourseResponse;
import com.zylos.backend.model.dto.MaterialResponse;
import com.zylos.backend.model.entity.CourseMaterial;
import com.zylos.backend.service.CourseService;
import com.zylos.backend.service.UserService;
import com.zylos.backend.service.CourseMaterialService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import jakarta.validation.Valid;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private static final Logger logger = LoggerFactory.getLogger(CourseController.class);
    private final CourseService courseService;
    private final CourseMaterialService materialService;
    private final UserService userService;


    public CourseController(CourseService courseService, CourseMaterialService materialService, UserService userService) {
        this.courseService = courseService;
        this.materialService = materialService;
        this.userService = userService;
    }

    @GetMapping
    public ResponseEntity<List<CourseResponse>> getAllCourses() {
        return ResponseEntity.ok(courseService.getAllCourses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CourseResponse> getCourseById(@PathVariable Long id) {
        logger.info("Fetching details for course ID: {}", id);
        return ResponseEntity.ok(courseService.getCourseById(id));
    }

    @GetMapping("/search")
    public ResponseEntity<List<CourseResponse>> search(@RequestParam String title) {
        return ResponseEntity.ok(courseService.searchByTitle(title));
    }

    @PostMapping
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<CourseResponse> create(@Valid @RequestBody CourseRequest request) {
        logger.info("Received request to create course: {}", request.title());
        int currentUserId = userService.getCurrentUserId();
        return ResponseEntity.ok(courseService.createCourse(request, currentUserId));
    }

    @PostMapping("/import")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<List<CourseResponse>> importCsv(@RequestParam("file") MultipartFile file) {
        int currentUserId = userService.getCurrentUserId();
        return ResponseEntity.ok(courseService.importFromCsv(file, currentUserId));
    }

     @GetMapping("/{id}/materials")
    public ResponseEntity<List<MaterialResponse>> getMaterials(@PathVariable Long id) {
        logger.info("Fetching materials for course ID: {}", id);
        return ResponseEntity.ok(materialService.getMaterialsByCourse(id));
    }

    @PostMapping("/{id}/materials")
    @PreAuthorize("hasRole('INSTRUCTOR')") // Stellt sicher, dass nur Lehrende hochladen können
    public ResponseEntity<MaterialResponse> uploadMaterial(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestParam("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(materialService.uploadMaterial(id, title, file));
    }

    @GetMapping("/materials/{materialId}/download")
    public ResponseEntity<byte[]> downloadMaterial(@PathVariable Long materialId) {
        CourseMaterial material = materialService.getMaterialEntity(materialId);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + material.getFileName() + "\"")
                .header(HttpHeaders.CONTENT_TYPE, material.getContentType())
                .body(material.getData());
    }
}
