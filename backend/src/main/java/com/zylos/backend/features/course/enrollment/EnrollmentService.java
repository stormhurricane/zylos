package com.zylos.backend.features.course.enrollment;

import com.zylos.backend.features.course.Course;
import com.zylos.backend.features.course.CourseRepository;
import com.zylos.backend.features.course.CourseUserClient;
import com.zylos.backend.features.course.dto.CourseResponse;
import com.zylos.backend.features.course.exceptions.CourseNotFoundException;
import com.zylos.backend.features.course.enrollment.dto.CourseParticipantsResponse;
import com.zylos.backend.features.course.enrollment.exceptions.EnrollmentUserNotFoundException;
import com.zylos.backend.features.course.enrollment.exceptions.InvalidRoleForEnrollmentException; // NEU
import com.zylos.backend.features.user.dto.UserResponse;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class EnrollmentService {

    private static final Logger logger = LoggerFactory.getLogger(EnrollmentService.class);
    
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final CourseUserClient courseUserClient;

    /**
     * Schreibt einen Studenten in einen Kurs ein.
     */
    @Transactional(rollbackFor = Exception.class)
    public void enrollStudent(Long courseId, long studentId) {
        if (enrollmentRepository.findByCourseIdAndUserId(courseId, studentId).isPresent()) {
            return;
        }
        
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));
        
        if (!courseUserClient.existsById(studentId)) {
            throw new EnrollmentUserNotFoundException(studentId);
        }

        // FIX: Strikte Rollenprüfung für Studenten
        if (!courseUserClient.isStudent(studentId)) {
            throw new InvalidRoleForEnrollmentException(studentId, "STUDENT");
        }

        enrollmentRepository.save(new Enrollment(studentId, course));
    }

    /**
     * Ordnet einem Kurs einen Lehrenden (Instructor) zu.
     */
    @Transactional(rollbackFor = Exception.class)
    public void assignInstructor(Long courseId, long instructorId) {
        if (enrollmentRepository.findByCourseIdAndUserId(courseId, instructorId).isPresent()) {
            return;
        }
        
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new CourseNotFoundException(courseId));
        
        if (!courseUserClient.existsById(instructorId)) {
            throw new EnrollmentUserNotFoundException(instructorId);
        }

        // FIX: Strikte Rollenprüfung für Instructors
        if (!courseUserClient.isInstructor(instructorId)) {
            throw new InvalidRoleForEnrollmentException(instructorId, "INSTRUCTOR");
        }

        enrollmentRepository.save(new Enrollment(instructorId, course));
    }

    @Transactional(rollbackFor = Exception.class)
    public void unenrollUser(Long courseId, long userId) {
        enrollmentRepository.deleteByCourseIdAndUserId(courseId, userId);
    }

    @Transactional(readOnly = true)
    public CourseParticipantsResponse getCategorizedParticipants(Long courseId) {
        logger.info("Processing categorized participants for course: {}", courseId);
        
        if (!courseRepository.existsById(courseId)) {
            throw new CourseNotFoundException(courseId);
        }

        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);
        
        List<Long> userIds = enrollments.stream()
                .map(Enrollment::getUserId)
                .collect(Collectors.toList());

        Map<String, List<UserResponse>> categorized = courseUserClient.categorizeUsersByIds(userIds);

        return new CourseParticipantsResponse(
                categorized.getOrDefault("instructors", List.of()),
                categorized.getOrDefault("students", List.of())
        );
    }

    @Transactional(readOnly = true)
    public List<CourseResponse> getEnrolledCourses(long userId) {
        return enrollmentRepository.findByUserId(userId).stream()
                .map(e -> mapToCourseResponse(e.getCourse()))
                .collect(Collectors.toList());
    }

    private CourseResponse mapToCourseResponse(Course course) {
        return new CourseResponse(
            course.getId(),
            course.getTitle(),
            course.getType(),
            course.getTerm(),
            course.getAcademicYear()
        );
    }
}