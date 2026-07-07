package com.zylos.backend.features.course.material;

import com.zylos.backend.config.security.CourseSecurityEvaluator;
import com.zylos.backend.features.course.Course;
import com.zylos.backend.features.course.CourseRepository;
import com.zylos.backend.features.course.exceptions.CourseAccessDeniedException;
import com.zylos.backend.features.course.material.dto.MaterialResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class) // Best Practice: Aktiviert sauberes Mockito-Lifecycle-Management
class CourseMaterialServiceTest {

    @Mock
    private CourseMaterialRepository materialRepository;
    
    @Mock
    private CourseRepository courseRepository;
    
    @Mock
    private CourseSecurityEvaluator courseSecurityEvaluator;

    @InjectMocks
    private CourseMaterialService materialService;

    private final LocalDateTime FIXED_TIME = LocalDateTime.of(2026, 7, 6, 20, 0);

    @Test
    void shouldUploadMaterialSuccessfully() throws IOException {
        Long courseId = 1L;
        Long userId = 42L;
        Course course = Course.builder().id(courseId).title("Java").build();
        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "content".getBytes());
        
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(courseSecurityEvaluator.hasWriteAccess(courseId, userId)).thenReturn(true);
        when(materialRepository.save(any(CourseMaterial.class))).thenAnswer(inv -> {
            CourseMaterial m = inv.getArgument(0);
            m.setId(10L);
            return m;
        });

        MaterialResponse response = materialService.uploadMaterialSecure(courseId, file, "Script", userId);

        assertNotNull(response.id());
        assertEquals("Script", response.title());
        verify(materialRepository).save(any(CourseMaterial.class));
    }

    @Test
    void shouldThrowAccessDeniedExceptionWhenUserHasNoWriteAccessDuringUpload() {
        Long courseId = 1L;
        Long userId = 99L;
        Course course = Course.builder().id(courseId).build();
        MockMultipartFile file = new MockMultipartFile("file", "test.pdf", "application/pdf", "content".getBytes());
        
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(course));
        when(courseSecurityEvaluator.hasWriteAccess(courseId, userId)).thenReturn(false);

        assertThrows(CourseAccessDeniedException.class, () -> 
            materialService.uploadMaterialSecure(courseId, file, "Script", userId)
        );
        verify(materialRepository, never()).save(any(CourseMaterial.class));
    }

    @Test
    void shouldReturnMaterialsForCourse() {
        Long courseId = 1L;
        when(courseRepository.existsById(courseId)).thenReturn(true);

        List<MaterialResponse> projectedResponses = List.of(
            new MaterialResponse(1L, "M1", "f1.pdf", "application/pdf", 0L, FIXED_TIME)
        );
        when(materialRepository.findAllProjectedByCourseId(courseId)).thenReturn(projectedResponses);

        List<MaterialResponse> materials = materialService.getMaterialsForCourse(courseId);

        assertEquals(1, materials.size());
        assertEquals(FIXED_TIME, materials.get(0).createdAt()); 
    }

    @Test
    void shouldThrowAccessDeniedExceptionWhenUserHasNoReadAccessDuringDownload() {
        // Arrange
        Long materialId = 10L;
        Long userId = 99L;
        Long courseId = 1L;

        Course course = new Course();
        course.setId(courseId);
        CourseMaterial material = new CourseMaterial("Titel", "test.pdf", "application/pdf", new byte[0], course);
        material.setId(materialId);

        when(materialRepository.findById(materialId)).thenReturn(Optional.of(material));
        
        when(courseSecurityEvaluator.hasReadAccess(courseId, userId)).thenReturn(false);

        // Act & Assert
        assertThrows(CourseAccessDeniedException.class, () -> {
            materialService.downloadMaterialSecure(materialId, userId);
        });
    }
}