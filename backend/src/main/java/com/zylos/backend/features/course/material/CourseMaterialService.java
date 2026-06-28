package com.zylos.backend.features.course.material;

import com.zylos.backend.features.course.Course;
import com.zylos.backend.features.course.CourseRepository;
import com.zylos.backend.features.course.exceptions.CourseNotFoundException; // FIX: Importierte Exception
import com.zylos.backend.features.course.material.dto.MaterialDownloadResponse; // FIX: Neues DTO
import com.zylos.backend.features.course.material.dto.MaterialResponse;
import com.zylos.backend.features.course.material.exceptions.MaterialNotFoundException; // FIX: Neue Exception

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
        // FIX: Nutzt jetzt unsere typisierte CourseNotFoundException
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));

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

    @Transactional(readOnly = true)
    public List<MaterialResponse> getMaterialsByCourse(Long courseId) {
        return materialRepository.findByCourseId(courseId).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    // FIX: Methode umgebaut! Mappt die Entity direkt im Service auf das Download-DTO
    @Transactional(readOnly = true)
    public MaterialDownloadResponse getMaterialForDownload(Long materialId) {
        CourseMaterial material = materialRepository.findById(materialId)
                .orElseThrow(() -> new MaterialNotFoundException(materialId));
        
        return new MaterialDownloadResponse(
                material.getFileName(),
                material.getContentType(),
                material.getData()
        );
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