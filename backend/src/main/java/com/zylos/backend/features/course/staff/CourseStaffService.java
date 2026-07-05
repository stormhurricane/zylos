package com.zylos.backend.features.course.staff;

import com.zylos.backend.features.course.Course;
import com.zylos.backend.features.course.CourseRepository;
import com.zylos.backend.features.course.CourseUserClient;
import com.zylos.backend.features.course.dto.CourseResponse;
import com.zylos.backend.features.course.exceptions.CourseNotFoundException;
import com.zylos.backend.features.course.enrollment.exceptions.EnrollmentUserNotFoundException;
import com.zylos.backend.features.course.enrollment.exceptions.InvalidRoleForEnrollmentException;
import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CourseStaffService {

    private final CourseStaffRepository courseStaffRepository;
    private final CourseRepository courseRepository;
    private final CourseUserClient courseUserClient;

    @Transactional(rollbackFor = Exception.class)
    public void addStaff(Long courseId, long userId, StaffRole role) {
        if (courseStaffRepository.findByCourseIdAndUserId(courseId, userId).isPresent()) {
            return;
        }

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));

        if (!courseUserClient.existsById(userId)) {
            throw new EnrollmentUserNotFoundException(userId);
        }

        if (!courseUserClient.isInstructor(userId)) {
            throw new InvalidRoleForEnrollmentException(userId, "INSTRUCTOR");
        }

        courseStaffRepository.save(new CourseStaff(userId, course, role));
    }

    @Transactional(rollbackFor = Exception.class)
    public void removeStaff(Long courseId, long userId) {
        courseStaffRepository.deleteByCourseIdAndUserId(courseId, userId);
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getCoursesForStaff(long userId) {
        return courseStaffRepository.findByUserId(userId).stream()
                .map(staff -> new CourseResponse(staff.getCourse()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Long> getStaffIdsForCourse(Long courseId) {
        return courseStaffRepository.findByUserId(courseId).stream() // Korrigiert auf Domänen-Feld
                .map(CourseStaff::getUserId)
                .toList();
    }
}