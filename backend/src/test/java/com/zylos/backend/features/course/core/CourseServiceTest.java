package com.zylos.backend.features.course.core;

import com.zylos.backend.features.course.core.Course;
import com.zylos.backend.features.course.core.CourseRepository;
import com.zylos.backend.features.course.core.CourseService;
import com.zylos.backend.features.course.core.CourseType;
import com.zylos.backend.features.course.core.SemesterTerm;
import com.zylos.backend.features.course.core.dto.CourseRequest;
import com.zylos.backend.features.course.core.dto.CourseResponse;
import com.zylos.backend.features.course.enrollment.EnrollmentService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class CourseServiceTest {

    private CourseRepository courseRepository;
    private EnrollmentService enrollmentService;
    private CourseService courseService;

    @BeforeEach
    void setUp() {
        courseRepository = Mockito.mock(CourseRepository.class);
        courseService = new CourseService(courseRepository);
    }

    @Test
    void shouldCreateCourseSuccessfully() {
        // Arrange
        CourseRequest request = new CourseRequest("Software Engineering", CourseType.LECTURE, SemesterTerm.SUMMER, "2024");
        when(courseRepository.findByTitle(request.title())).thenReturn(Optional.empty());
        when(courseRepository.save(any(Course.class))).thenAnswer(invocation -> {
            Course c = invocation.getArgument(0);
            c.setId(1L);
            return c;
        });

        // Act
        CourseResponse response = courseService.createCourse(request);

        // Assert
        assertNotNull(response.id());
        assertEquals("Software Engineering", response.title());
        verify(courseRepository, times(1)).save(any(Course.class));
    }

    @Test
    void shouldThrowExceptionWhenCourseTitleAlreadyExists() {
        // Arrange
        CourseRequest request = new CourseRequest("Existing Course", CourseType.LECTURE, SemesterTerm.SUMMER, "2024");
        when(courseRepository.findByTitle(anyString())).thenReturn(Optional.of(new Course()));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> courseService.createCourse(request));
    }

    @Test
    void shouldImportCoursesFromCsv() throws Exception {
        // Arrange
        String csvContent = "Advanced Java;LECTURE;WINTER;2024/25\nClean Code;SEMINAR;SUMMER;2024";
        MockMultipartFile file = new MockMultipartFile("file", "courses.csv", "text/csv", csvContent.getBytes());

        when(courseRepository.findByTitle(anyString())).thenReturn(Optional.empty());
        when(courseRepository.save(any(Course.class))).thenAnswer(invocation -> {
            Course c = invocation.getArgument(0);
            c.setId((long) (Math.random() * 100));
            return c;
        });

        // Act
        List<CourseResponse> imported = courseService.importFromCsv(file);

        // Assert
        assertEquals(2, imported.size());
        assertEquals("Advanced Java", imported.get(0).title());
        assertEquals("Clean Code", imported.get(1).title());
    }

    @Test
    void shouldHandleEmptyCsvGracefully() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile("file", "empty.csv", "text/csv", "".getBytes());

        // Act
        List<CourseResponse> imported = courseService.importFromCsv(file);

        // Assert
        assertTrue(imported.isEmpty());
    }
}
