package com.zylos.backend.service;

import com.zylos.backend.model.dto.MaterialResponse;
import com.zylos.backend.model.entity.Course;
import com.zylos.backend.model.entity.CourseMaterial;
import com.zylos.backend.model.entity.CourseType;
import com.zylos.backend.model.entity.SemesterTerm;
import com.zylos.backend.repository.CourseMaterialRepository;
import com.zylos.backend.repository.CourseRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CourseMaterialServiceTest {

    private CourseMaterialRepository materialRepository;
    private CourseRepository courseRepository;
    private CourseMaterialService materialService;

    @BeforeEach
    void setUp() {
        materialRepository = Mockito.mock(CourseMaterialRepository.class);
        courseRepository = Mockito.mock(CourseRepository.class);
        materialService = new CourseMaterialService(materialRepository, courseRepository);
    }

    @Test
    void shouldUploadMaterialSuccessfully() throws IOException {
        // Arrange
        Long courseId = 1L;
        Course course = new Course(courseId, "Java", CourseType.LECTURE, SemesterTerm.SUMMER, "2024");
        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "content".getBytes());
        
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(materialRepository.save(any(CourseMaterial.class))).thenAnswer(invocation -> {
            CourseMaterial m = invocation.getArgument(0);
            m.setId(10L);
            return m;
        });

        // Act
        MaterialResponse response = materialService.uploadMaterial(courseId, "Script", file);

        // Assert
        assertNotNull(response.id());
        assertEquals("Script", response.title());
        assertEquals("test.pdf", response.fileName());
        verify(materialRepository).save(any(CourseMaterial.class));
    }

    @Test
    void shouldThrowExceptionWhenCourseNotFoundDuringUpload() {
        // Arrange
        when(courseRepository.findById(anyLong())).thenReturn(Optional.empty());
        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", "text/plain", "data".getBytes());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> materialService.uploadMaterial(1L, "Title", file));
    }

    @Test
    void shouldReturnMaterialsForCourse() {
        // Arrange
        Course course = new Course();
        course.setId(1L);
        CourseMaterial m1 = new CourseMaterial("M1", "f1.pdf", "pdf", new byte[0], course);
        when(materialRepository.findByCourseId(1L)).thenReturn(List.of(m1));

        // Act
        List<MaterialResponse> materials = materialService.getMaterialsByCourse(1L);

        // Assert
        assertEquals(1, materials.size());
        assertEquals("M1", materials.get(0).title());
    }

    @Test
    void shouldGetMaterialEntity() {
        // Arrange
        CourseMaterial material = new CourseMaterial();
        when(materialRepository.findById(10L)).thenReturn(Optional.of(material));

        // Act
        CourseMaterial result = materialService.getMaterialEntity(10L);

        // Assert
        assertNotNull(result);
    }
}