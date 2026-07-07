package com.zylos.backend.features.course.enrollment;

import com.zylos.backend.features.course.Course;
import com.zylos.backend.features.course.CourseRepository;
import com.zylos.backend.features.course.CourseUserClient;
import com.zylos.backend.features.course.dto.CourseResponse;
import com.zylos.backend.features.course.exceptions.CourseNotFoundException;
import com.zylos.backend.features.course.enrollment.exceptions.EnrollmentUserNotFoundException;
import com.zylos.backend.features.course.enrollment.exceptions.InvalidRoleForEnrollmentException;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EnrollmentService {
    
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final CourseUserClient courseUserClient;

    @Transactional(rollbackFor = Exception.class)
    public void addEnrollment(Long courseId, long userId) {
        if (enrollmentRepository.findByCourseIdAndUserId(courseId, userId).isPresent()) {
            return;
        }
        
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));
        
        if (!courseUserClient.existsById(userId)) {
            throw new EnrollmentUserNotFoundException(userId);
        }

        if (!courseUserClient.isStudent(userId)) {
            throw new InvalidRoleForEnrollmentException(userId, "STUDENT");
        }

        enrollmentRepository.save(new Enrollment(userId, course));
    }

    @Transactional(rollbackFor = Exception.class)
    public void unenrollUser(Long courseId, long userId) {
        enrollmentRepository.deleteByCourseIdAndUserId(courseId, userId);
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getEnrolledCourses(long userId) {
        return enrollmentRepository.findByUserId(userId).stream()
                .map(e -> new CourseResponse(e.getCourse()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<Long> getStudentIdsForCourse(Long courseId) {
        return enrollmentRepository.findByCourseId(courseId).stream()
                .map(Enrollment::getUserId)
                .toList();
    }
}