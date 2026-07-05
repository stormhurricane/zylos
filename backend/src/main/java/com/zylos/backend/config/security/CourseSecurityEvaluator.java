package com.zylos.backend.config.security;

import com.zylos.backend.features.course.enrollment.EnrollmentRepository;
import com.zylos.backend.features.course.staff.CourseStaffRepository;
import com.zylos.backend.features.course.staff.StaffRole;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component("courseSecurityEvaluator")
@RequiredArgsConstructor
public class CourseSecurityEvaluator {

    private final CourseStaffRepository staffRepository;
    private final EnrollmentRepository enrollmentRepository;

    @Transactional(readOnly = true)
    public boolean hasWriteAccess(Long courseId, long userId) {
        return staffRepository.findByCourseIdAndUserId(courseId, userId)
                .map(staff -> staff.getRole() == StaffRole.OWNER || staff.getRole() == StaffRole.EDITOR)
                .orElse(false);
    }

    public boolean hasReadAccess(Long courseId, long userId) {
        return staffRepository.existsByCourseIdAndUserId(courseId, userId)
            || enrollmentRepository.existsByCourseIdAndUserId(courseId, userId);
    }
}