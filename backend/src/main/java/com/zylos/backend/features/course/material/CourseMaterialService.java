package com.zylos.backend.features.course.material;

import com.zylos.backend.features.course.Course;
import com.zylos.backend.features.course.CourseRepository;
import com.zylos.backend.features.course.exceptions.CourseNotFoundException;
import com.zylos.backend.features.course.material.dto.MaterialDownloadResponse;
import com.zylos.backend.features.course.material.dto.MaterialResponse;
import com.zylos.backend.features.course.material.exceptions.MaterialNotFoundException; // Pfad ggf. anpassen

import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseMaterialService {

    private final CourseMaterialRepository materialRepository;
    private final CourseRepository courseRepository;

    @Transactional(rollbackFor = Exception.class)
    public MaterialResponse uploadMaterial(Long courseId, MultipartFile file, String title) throws IOException {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));

        // Metadaten und Bytes extrahieren
        String fileName = file.getOriginalFilename();
        String contentType = file.getContentType();
        byte[] data = file.getBytes();

        CourseMaterial material = new CourseMaterial(title, fileName, contentType, data, course);
        CourseMaterial saved = materialRepository.save(material);

        return mapToResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<MaterialResponse> getMaterialsForCourse(Long courseId) {
        if (!courseRepository.existsById(courseId)) {
            throw new CourseNotFoundException(courseId);
        }

        return materialRepository.findAllProjectedByCourseId(courseId);
    }

    @Transactional(readOnly = true)
    public MaterialDownloadResponse downloadMaterial(Long materialId) {
        CourseMaterial material = materialRepository.findById(materialId)
                .orElseThrow(() -> new MaterialNotFoundException(materialId));

        return new MaterialDownloadResponse(
                material.getFileName(),
                material.getContentType(),
                new ByteArrayResource(material.getData()) 
        );
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteMaterial(Long materialId) {
        if (!materialRepository.existsById(materialId)) {
            throw new MaterialNotFoundException(materialId);
        }
        materialRepository.deleteById(materialId);
    }

    private MaterialResponse mapToResponse(CourseMaterial material) {
        return new MaterialResponse(
                material.getId(),
                material.getTitle(),
                material.getFileName(),
                material.getContentType(),
                material.getFileSize(),
                material.getCreatedAt()
        );
    }
}