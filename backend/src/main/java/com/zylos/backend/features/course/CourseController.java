package com.zylos.backend.features.course;

import com.zylos.backend.config.web.CurrentUserId;
import com.zylos.backend.features.course.dto.CourseRequest;
import com.zylos.backend.features.course.dto.CourseResponse;
import com.zylos.backend.features.course.dto.MaterialResponse;
import jakarta.validation.Valid;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/courses")
public class CourseController {

    private final CourseService courseService;
    private final CourseMaterialService materialService;

    public CourseController(CourseService courseService, CourseMaterialService materialService) {
        this.courseService = courseService;
        this.materialService = materialService;
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
    public ResponseEntity<CourseResponse> create(@Valid @RequestBody CourseRequest request, Authentication authentication, @CurrentUserId long currentUserId) {
        return ResponseEntity.ok(courseService.createCourse(request, currentUserId));
    }

    @PostMapping("/import")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<List<CourseResponse>> importCsv(@RequestParam("file") MultipartFile file, Authentication authentication, @CurrentUserId long currentUserId) {
        return ResponseEntity.ok(courseService.importFromCsv(file, currentUserId));
    }

    @GetMapping("/{id}/materials")
    public ResponseEntity<List<MaterialResponse>> getMaterials(@PathVariable Long id) {
        return ResponseEntity.ok(materialService.getMaterialsByCourse(id));
    }

    @PostMapping("/{id}/materials")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<MaterialResponse> uploadMaterial(
            @PathVariable Long id,
            @RequestParam("title") String title,
            @RequestPart("file") MultipartFile file) throws IOException {
        return ResponseEntity.ok(materialService.uploadMaterial(id, title, file));
    }

    @GetMapping("/materials/{materialId}/download")
    public ResponseEntity<byte[]> downloadMaterial(@PathVariable Long materialId) {
        CourseMaterial material = materialService.getMaterialEntity(materialId);
        
        ContentDisposition contentDisposition = ContentDisposition.attachment()
                .filename(material.getFileName())
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition.toString())
                .header(HttpHeaders.CONTENT_TYPE, material.getContentType())
                .header(HttpHeaders.CONTENT_LENGTH, String.valueOf(material.getData().length))
                .body(material.getData());
    }

}