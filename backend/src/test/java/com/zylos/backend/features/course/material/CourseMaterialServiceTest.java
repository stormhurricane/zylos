package com.zylos.backend.features.course.material;

import com.zylos.backend.features.course.Course;
import com.zylos.backend.features.course.CourseRepository;
import com.zylos.backend.features.course.CourseType;
import com.zylos.backend.features.course.SemesterTerm;
import com.zylos.backend.features.course.material.CourseMaterial;
import com.zylos.backend.features.course.material.CourseMaterialRepository;
import com.zylos.backend.features.course.material.CourseMaterialService;
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

        // Act
        MaterialResponse response = materialService.uploadMaterial(courseId, file, "Script");

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
        assertThrows(RuntimeException.class, () -> materialService.uploadMaterial(1L, file, "Title"));
    }

   @Test
    void shouldReturnMaterialsForCourse() {
        // Arrange
        // FIX 1: Der Service prüft jetzt zuerst, ob der Kurs existiert. Das müssen wir mocken!
        when(courseRepository.existsById(1L)).thenReturn(true);

        // FIX 2: Der Service ruft NICHT mehr findByCourseId auf, sondern die neue Projektions-Query!
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
        // FIX: Nutze den echten Konstruktor mit validen Dummy-Bytes (nicht null!)
        byte[] dummyBytes = "pdf-content".getBytes();
        CourseMaterial material = new CourseMaterial("Titel", "test.pdf", "application/pdf", dummyBytes, course);
        
        when(materialRepository.findById(10L)).thenReturn(Optional.of(material));

        // Act
        MaterialDownloadResponse result = materialService.downloadMaterial(10L);

        // Assert
        assertNotNull(result);
        assertEquals("test.pdf", result.fileName());
        assertEquals("application/pdf", result.contentType());
    }
}