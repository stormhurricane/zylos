package com.zylos.backend.service;

import com.zylos.backend.model.dto.CourseParticipantsResponse;
import com.zylos.backend.model.dto.CourseResponse;
import com.zylos.backend.model.dto.UserResponse;
import com.zylos.backend.model.entity.*;
import com.zylos.backend.repository.CourseRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zylos.backend.repository.EnrollmentRepository;
import com.zylos.backend.repository.UserRepository;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class EnrollmentService {

    private static final Logger logger = LoggerFactory.getLogger(EnrollmentService.class);
    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
    private final UserRepository userRepository;

    public EnrollmentService(EnrollmentRepository enrollmentRepository, 
                             CourseRepository courseRepository, 
                             UserRepository userRepository) {
        this.enrollmentRepository = enrollmentRepository;
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void enrollUser(Long courseId, int userId) {
        if (enrollmentRepository.findByCourseIdAndUserId(courseId, userId).isPresent()) {
            return; // Already enrolled
        }
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        enrollmentRepository.save(new Enrollment(user, course));
    }

    @Transactional
    public void unenrollUser(Long courseId, int userId) {
        enrollmentRepository.deleteByCourseIdAndUserId(courseId, userId);
    }

    public CourseParticipantsResponse getCategorizedParticipants(Long courseId) {
        logger.info("Processing categorized participants for course: {}", courseId);
        List<Enrollment> enrollments = enrollmentRepository.findByCourseId(courseId);
        logger.info("Found {} raw enrollments in database", enrollments.size());

        List<UserResponse> instructors = new ArrayList<>();
        List<UserResponse> students = new ArrayList<>();

        for (Enrollment enrollment : enrollments) {
             // Hibernate Proxy auflösen, um instanceof Prüfung zu ermöglichen
            User user = (User) Hibernate.unproxy(enrollment.getUser());
            UserResponse res = new UserResponse(user);
            
            if (user instanceof Teacher) {
                instructors.add(res);
            } else if (user instanceof Student) {
                students.add(res);
            }
        }
        logger.info("Found {} instructors and {} students", instructors.size(), students.size());
        return new CourseParticipantsResponse(instructors, students);
    }

    public List<CourseResponse> getEnrolledCourses(int userId) {
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
