package com.zylos.backend.features.course.enrollment;

import com.zylos.backend.features.course.Course;
import com.zylos.backend.features.course.CourseRepository;
import com.zylos.backend.features.course.CourseUserClient;
import com.zylos.backend.features.course.exceptions.CourseNotFoundException;
import com.zylos.backend.features.course.enrollment.exceptions.EnrollmentUserNotFoundException;
import com.zylos.backend.features.course.enrollment.exceptions.InvalidRoleForEnrollmentException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EnrollmentServiceTest {

    @Mock
    private EnrollmentRepository enrollmentRepository;
    @Mock
    private CourseRepository courseRepository;
    @Mock
    private CourseUserClient courseUserClient;

    @InjectMocks
    private EnrollmentService enrollmentService;

    @Test
    void addEnrollment_ShouldReturnEarly_WhenEnrollmentAlreadyExists() {
        Long courseId = 1L;
        long userId = 42L;

        when(enrollmentRepository.findByCourseIdAndUserId(courseId, userId))
                .thenReturn(Optional.of(new Enrollment()));

        enrollmentService.addEnrollment(courseId, userId);

        verify(courseRepository, never()).findById(any());
        verify(enrollmentRepository, never()).save(any());
    }

    @Test
    void addEnrollment_ShouldThrowCourseNotFoundException_WhenCourseDoesNotExist() {
        Long courseId = 999L;
        long userId = 42L;

        when(enrollmentRepository.findByCourseIdAndUserId(courseId, userId)).thenReturn(Optional.empty());
        when(courseRepository.findById(courseId)).thenReturn(Optional.empty());

        assertThrows(CourseNotFoundException.class, () -> enrollmentService.addEnrollment(courseId, userId));
    }

    @Test
    void addEnrollment_ShouldThrowEnrollmentUserNotFoundException_WhenUserDoesNotExist() {
        Long courseId = 1L;
        long userId = 999L;
        Course mockCourse = new Course();

        when(enrollmentRepository.findByCourseIdAndUserId(courseId, userId)).thenReturn(Optional.empty());
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(mockCourse));
        when(courseUserClient.existsById(userId)).thenReturn(false);

        assertThrows(EnrollmentUserNotFoundException.class, () -> enrollmentService.addEnrollment(courseId, userId));
    }

    @Test
    void addEnrollment_ShouldThrowInvalidRoleException_WhenUserIsNotAStudent() {
        Long courseId = 1L;
        long userId = 42L;
        Course mockCourse = new Course();

        when(enrollmentRepository.findByCourseIdAndUserId(courseId, userId)).thenReturn(Optional.empty());
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(mockCourse));
        when(courseUserClient.existsById(userId)).thenReturn(true);
        when(courseUserClient.isStudent(userId)).thenReturn(false); 

        assertThrows(InvalidRoleForEnrollmentException.class, () -> enrollmentService.addEnrollment(courseId, userId));
    }

    @Test
    void addEnrollment_ShouldSaveEnrollment_WhenAllValidationsPass() {
        Long courseId = 1L;
        long userId = 42L;
        Course mockCourse = new Course();

        when(enrollmentRepository.findByCourseIdAndUserId(courseId, userId)).thenReturn(Optional.empty());
        when(courseRepository.findById(courseId)).thenReturn(Optional.of(mockCourse));
        when(courseUserClient.existsById(userId)).thenReturn(true);
        when(courseUserClient.isStudent(userId)).thenReturn(true);

        enrollmentService.addEnrollment(courseId, userId);

        verify(enrollmentRepository).save(any(Enrollment.class));
    }
}