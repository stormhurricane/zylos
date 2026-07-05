package com.zylos.backend.features.course.material;

import com.zylos.backend.config.security.CourseSecurityEvaluator;
import com.zylos.backend.features.course.Course;
import com.zylos.backend.features.course.CourseRepository;
import com.zylos.backend.features.course.CourseType;
import com.zylos.backend.features.course.SemesterTerm;
import com.zylos.backend.features.course.material.dto.MaterialDownloadResponse;
import com.zylos.backend.features.course.material.dto.MaterialResponse;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class CourseMaterialServiceTest {

    private CourseMaterialRepository materialRepository;
    private CourseRepository courseRepository;
    private CourseMaterialService materialService;
    private CourseSecurityEvaluator courseSecurityEvaluator;

    @BeforeEach
    void setUp() {
        materialRepository = Mockito.mock(CourseMaterialRepository.class);
        courseRepository = Mockito.mock(CourseRepository.class);
        courseSecurityEvaluator = Mockito.mock(CourseSecurityEvaluator.class);

        materialService = new CourseMaterialService(materialRepository, courseRepository,courseSecurityEvaluator);
    }

    @Test
    void shouldUploadMaterialSuccessfully() throws IOException {
        // Arrange
        Long courseId = 1L;
        Course course = Course.builder()
            .id(courseId)
            .title("Java")
            .type(CourseType.LECTURE)
            .term(SemesterTerm.SUMMER)
            .academicYear("2024")
            .build();
        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "content".getBytes());
        
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(materialRepository.save(any(CourseMaterial.class))).thenAnswer(invocation -> {
            CourseMaterial m = invocation.getArgument(0);
            m.setId(10L);
            return m;
        });
        Mockito.when(courseSecurityEvaluator.hasWriteAccess(1L, 1L)).thenReturn(true);

        // Act
        MaterialResponse response = materialService.uploadMaterialSecure(courseId, file, "Script", 1L);

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
        assertThrows(RuntimeException.class, () -> materialService.uploadMaterialSecure(1L, file, "Title", 1L));
    }

   @Test
    void shouldReturnMaterialsForCourse() {
        // Arrange
        when(courseRepository.existsById(1L)).thenReturn(true);

        List<MaterialResponse> projectedResponses = List.of(
            new MaterialResponse(1L, "M1", "f1.pdf", "application/pdf", 0L, java.time.LocalDateTime.now())
        );
        when(materialRepository.findAllProjectedByCourseId(1L)).thenReturn(projectedResponses);

        // Act
        List<MaterialResponse> materials = materialService.getMaterialsForCourse(1L);

        // Assert
        assertNotNull(materials);
        assertEquals(1, materials.size());
        assertEquals("M1", materials.get(0).title());
    }

    @Test
    void shouldGetMaterialEntity() {
        // Arrange
        Course course = new Course();
        course.setId(1L);

        byte[] dummyBytes = "pdf-content".getBytes();
        CourseMaterial material = new CourseMaterial("Titel", "test.pdf", "application/pdf", dummyBytes, course);
        
        when(materialRepository.findById(10L)).thenReturn(Optional.of(material));

        Mockito.when(courseSecurityEvaluator.hasReadAccess(1L, 1L)).thenReturn(true);
        Mockito.when(courseSecurityEvaluator.hasWriteAccess(1L, 1L)).thenReturn(true);

        // Act
        MaterialDownloadResponse result = materialService.downloadMaterialSecure(10L, 1L);

        // Assert
        assertNotNull(result);
        assertEquals("test.pdf", result.fileName());
        assertEquals("application/pdf", result.contentType());
    }
}