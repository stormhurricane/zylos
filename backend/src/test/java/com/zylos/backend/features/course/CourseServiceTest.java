package com.zylos.backend.features.course;

import com.zylos.backend.features.course.dto.CourseRequest;
import com.zylos.backend.features.course.dto.CourseResponse;
import com.zylos.backend.features.course.exceptions.CourseNotFoundException;
import com.zylos.backend.features.course.enrollment.EnrollmentService;
import com.zylos.backend.features.course.staff.CourseStaffService;

import jakarta.validation.Validator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseServiceTest {

    @Mock
    private CourseRepository courseRepository;
    @Mock
    private EnrollmentService enrollmentService;
    @Mock
    private CourseStaffService courseStaffService;
    @Mock
    private CourseUserClient courseUserClient;
    @Mock
    private Validator validator;

    @InjectMocks
    private CourseService courseService;

    @Test
    void shouldCreateCourseSuccessfully() {
        // Arrange
        CourseRequest request = new CourseRequest("Software Engineering", CourseType.LECTURE, SemesterTerm.SUMMER, "2026");
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
        verify(courseRepository).save(any(Course.class));
    }

    @Test
    void shouldThrowExceptionWhenCourseTitleAlreadyExists() {
        // Arrange
        CourseRequest request = new CourseRequest("Existing Course", CourseType.LECTURE, SemesterTerm.SUMMER, "2026");
        when(courseRepository.findByTitle(request.title())).thenReturn(Optional.of(new Course()));

        // Act & Assert 
        assertThrows(RuntimeException.class, () -> courseService.createCourse(request));
        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void shouldImportCoursesFromCsv() throws Exception {
        // Arrange
        String csvContent = "Advanced Java;LECTURE;WINTER;2026/27\nClean Code;SEMINAR;SUMMER;2026";
        MockMultipartFile file = new MockMultipartFile("file", "courses.csv", "text/csv", csvContent.getBytes());

        when(courseRepository.findByTitle(anyString())).thenReturn(Optional.empty());
        when(courseRepository.save(any(Course.class))).thenAnswer(invocation -> {
            Course c = invocation.getArgument(0);
            c.setId(10L);
            return c;
        });

        // Act
        List<CourseResponse> imported = courseService.importFromCsv(file);

        // Assert
        assertNotNull(imported);
        assertEquals(2, imported.size());
        assertEquals("Advanced Java", imported.get(0).title());
        assertEquals("Clean Code", imported.get(1).title());
        verify(courseRepository, times(2)).save(any(Course.class));
    }

    @Test
    void shouldHandleEmptyCsvGracefully() throws Exception {
        // Arrange
        MockMultipartFile file = new MockMultipartFile("file", "empty.csv", "text/csv", "".getBytes());

        // Act
        List<CourseResponse> imported = courseService.importFromCsv(file);

        // Assert
        assertTrue(imported.isEmpty());
        verify(courseRepository, never()).save(any(Course.class));
    }

    @Test
    void getCourseById_ShouldReturnCourse_WhenCourseExists() {
        // Arrange
        Course course = new Course();
        course.setId(1L);
        course.setTitle("Database Systems");
        when(courseRepository.findById(1L)).thenReturn(Optional.of(course));

        // Act
        CourseResponse response = courseService.getCourseById(1L);

        // Assert
        assertEquals(1L, response.id());
        assertEquals("Database Systems", response.title());
    }

    @Test
    void getCourseById_ShouldThrowNotFoundException_WhenCourseDoesNotExist() {
        // Arrange
        when(courseRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(CourseNotFoundException.class, () -> courseService.getCourseById(999L));
    }
}