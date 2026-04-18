package com.zylos.backend.service;

import com.zylos.backend.model.dto.MaterialResponse;
import com.zylos.backend.model.entity.Course;
import com.zylos.backend.model.entity.CourseMaterial;
import com.zylos.backend.repository.CourseMaterialRepository;
import com.zylos.backend.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseMaterialService {

    private final CourseMaterialRepository materialRepository;
    private final CourseRepository courseRepository;

    public CourseMaterialService(CourseMaterialRepository materialRepository, CourseRepository courseRepository) {
        this.materialRepository = materialRepository;
        this.courseRepository = courseRepository;
    }

    @Transactional
    public MaterialResponse uploadMaterial(Long courseId, String title, MultipartFile file) throws IOException {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        CourseMaterial material = new CourseMaterial(
                title,
                file.getOriginalFilename(),
                file.getContentType(),
                file.getBytes(),
                course
        );

        CourseMaterial saved = materialRepository.save(material);
        return mapToResponse(saved);
    }

    public List<MaterialResponse> getMaterialsByCourse(Long courseId) {
        return materialRepository.findByCourseId(courseId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public CourseMaterial getMaterialEntity(Long materialId) {
        return materialRepository.findById(materialId)
                .orElseThrow(() -> new RuntimeException("Material not found"));
    }

    private MaterialResponse mapToResponse(CourseMaterial material) {
        return new MaterialResponse(
                material.getId(),
                material.getTitle(),
                material.getFileName(),
                material.getContentType()
        );
    }
}
