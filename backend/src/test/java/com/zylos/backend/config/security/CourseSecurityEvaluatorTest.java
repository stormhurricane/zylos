package com.zylos.backend.config.security;

import com.zylos.backend.features.course.enrollment.EnrollmentRepository;
import com.zylos.backend.features.course.staff.CourseStaff;
import com.zylos.backend.features.course.staff.CourseStaffRepository;
import com.zylos.backend.features.course.staff.StaffRole;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CourseSecurityEvaluatorTest {

    @Mock
    private CourseStaffRepository staffRepository;

    @Mock
    private EnrollmentRepository enrollmentRepository;

    @InjectMocks
    private CourseSecurityEvaluator courseSecurityEvaluator;

    @Test
    void hasWriteAccess_ShouldReturnTrue_WhenUserIsOwnerOrEditor() {
        Long courseId = 1L;
        long userId = 42L;
        
        CourseStaff ownerStaff = new CourseStaff();
        ownerStaff.setRole(StaffRole.OWNER); 
        
        when(staffRepository.findByCourseIdAndUserId(courseId, userId)).thenReturn(Optional.of(ownerStaff));
        assertTrue(courseSecurityEvaluator.hasWriteAccess(courseId, userId));

        CourseStaff editorStaff = new CourseStaff();
        editorStaff.setRole(StaffRole.EDITOR);
        
        when(staffRepository.findByCourseIdAndUserId(courseId, userId)).thenReturn(Optional.of(editorStaff));
        assertTrue(courseSecurityEvaluator.hasWriteAccess(courseId, userId));
    }

    @Test
    void hasWriteAccess_ShouldReturnFalse_WhenStaffMemberNotFound() {
        Long courseId = 1L;
        long userId = 99L;
        
        when(staffRepository.findByCourseIdAndUserId(courseId, userId)).thenReturn(Optional.empty());
        
        assertFalse(courseSecurityEvaluator.hasWriteAccess(courseId, userId));
    }

    @Test
    void hasReadAccess_ShouldReturnTrueAndShortCircuit_WhenUserIsStaff() {
        Long courseId = 1L;
        long userId = 42L;

        when(staffRepository.existsByCourseIdAndUserId(courseId, userId)).thenReturn(true);

        boolean result = courseSecurityEvaluator.hasReadAccess(courseId, userId);

        assertTrue(result);
        verifyNoInteractions(enrollmentRepository);
    }

    @Test
    void hasReadAccess_ShouldCheckEnrollment_WhenUserIsNotStaff() {
        Long courseId = 1L;
        long userId = 42L;

        when(staffRepository.existsByCourseIdAndUserId(courseId, userId)).thenReturn(false);
        when(enrollmentRepository.existsByCourseIdAndUserId(courseId, userId)).thenReturn(true);

        boolean result = courseSecurityEvaluator.hasReadAccess(courseId, userId);

        assertTrue(result);
        verify(enrollmentRepository).existsByCourseIdAndUserId(courseId, userId);
    }
}