package com.zylos.backend.features.course;

import com.zylos.backend.features.course.dto.CourseRequest;
import com.zylos.backend.features.course.dto.CourseResponse;
import com.zylos.backend.features.course.staff.CourseStaffService;
import com.zylos.backend.features.course.staff.StaffRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseCreationOrchestratorTest {

    @Mock
    private CourseService courseService;

    @Mock
    private CourseStaffService courseStaffService;

    @InjectMocks
    private CourseCreationOrchestrator orchestrator;

    @Test
    void createCourseWithInstructor_ShouldSucceed_WhenServicesRespondNormally() {
        // Arrange
        CourseRequest request = new CourseRequest("Software Engineering", CourseType.SEMINAR, SemesterTerm.WINTER, "2024");
        CourseResponse mockResponse = new CourseResponse(123L, "Software Engineering", CourseType.SEMINAR, SemesterTerm.WINTER, "2024", 2);
        long instructorId = 42L;

        when(courseService.createCourse(request)).thenReturn(mockResponse);

        // Act
        CourseResponse result = orchestrator.createCourseWithInstructor(request, instructorId);

        // Assert
        assertNotNull(result);
        assertEquals(123L, result.id());
        
        // Verify
        verify(courseService).createCourse(request);
        verify(courseStaffService).addStaff(123L, instructorId, StaffRole.OWNER);
    }

    @Test
    void createCourseWithInstructor_ShouldPropagateException_WhenStaffAssignmentFails() {
        // Arrange
        CourseRequest request = new CourseRequest("Software Engineering", CourseType.LECTURE, SemesterTerm.SUMMER, "2025/2026");
        CourseResponse mockResponse = new CourseResponse(123L, "Software Engineering", CourseType.LECTURE, SemesterTerm.SUMMER, "2025/2026", 2);
        long instructorId = 42L;

        when(courseService.createCourse(request)).thenReturn(mockResponse);
        // Simulate: Course was created, but staff assignment fails (e.g., user not found)
        doThrow(new RuntimeException("Staff assignment failed"))
                .when(courseStaffService).addStaff(123L, instructorId, StaffRole.OWNER);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> 
            orchestrator.createCourseWithInstructor(request, instructorId)
        );
    }

    @Test
    void importFromCsvWithInstructor_ShouldAssignOwnerToAllImportedCourses() {
        // Arrange
        MockMultipartFile mockFile = new MockMultipartFile("file", "courses.csv", "text/csv", "data".getBytes());
        long instructorId = 42L;

        CourseResponse course1 = new CourseResponse(1L, "Math I", CourseType.LECTURE, SemesterTerm.SUMMER, "2025/2026", 2);
        CourseResponse course2 = new CourseResponse(2L, "Math II", CourseType.LECTURE, SemesterTerm.SUMMER, "2025/2026", 2);

        when(courseService.importFromCsv(mockFile)).thenReturn(List.of(course1, course2));

        // Act
        List<CourseResponse> result = orchestrator.importFromCsvWithInstructor(mockFile, instructorId);

        // Assert
        assertEquals(2, result.size());
        // Verify that EACH imported course was assigned to the instructor
        verify(courseStaffService).addStaff(1L, instructorId, StaffRole.OWNER);
        verify(courseStaffService).addStaff(2L, instructorId, StaffRole.OWNER);
    }
}