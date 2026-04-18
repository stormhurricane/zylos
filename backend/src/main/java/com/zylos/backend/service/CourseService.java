package com.zylos.backend.service;

import com.zylos.backend.model.dto.CourseRequest;
import com.zylos.backend.model.dto.CourseResponse;
import com.zylos.backend.model.entity.Course;
import com.zylos.backend.model.entity.CourseType;
import com.zylos.backend.model.entity.SemesterTerm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zylos.backend.repository.CourseRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private static final Logger logger = LoggerFactory.getLogger(CourseService.class);
    private final CourseRepository courseRepository;
    private final EnrollmentService enrollmentService;

    public CourseService(CourseRepository courseRepository, EnrollmentService enrollmentService) {
        this.courseRepository = courseRepository;
        this.enrollmentService = enrollmentService;
    }

    @Transactional
    public CourseResponse createCourse(CourseRequest request, int creatorId) {
        if (courseRepository.findByTitle(request.title()).isPresent()) {
            throw new RuntimeException("Course with title '" + request.title() + "' already exists.");
        }
        Course course = new Course(request.title(), request.type(), request.term(), request.academicYear());
        Course savedCourse = courseRepository.save(course);
        
        // Creator automatically enrolled
        enrollmentService.enrollUser(savedCourse.getId(), creatorId);
        
        return mapToResponse(savedCourse);
    }

    public CourseResponse getCourseById(Long id) {
        logger.info("Service: Fetching course details for ID: {}", id);
        return courseRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> {
                    logger.error("Service: Course with ID {} not found in database", id);
                    return new RuntimeException("Course not found");
                });
    }

    public List<CourseResponse> getAllCourses() {
        return courseRepository.findAll().stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    public List<CourseResponse> searchByTitle(String title) {
        return courseRepository.findByTitleContainingIgnoreCase(title).stream()
                .map(this::mapToResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public List<CourseResponse> importFromCsv(MultipartFile file, int creatorId) {
        List<CourseResponse> results = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String separator = line.contains(";") ? ";" : ",";
                String[] data = line.split(separator);
                
                if (data.length >= 4) {
                    // Expected format: title;type;term;year
                    // type: LECTURE/SEMINAR, term: SUMMER/WINTER
                    CourseRequest request = new CourseRequest(
                        data[0].trim(),
                        CourseType.valueOf(data[1].trim().toUpperCase()),
                        SemesterTerm.valueOf(data[2].trim().toUpperCase()),
                        data[3].trim()
                    );
                    try {
                        results.add(createCourse(request, creatorId));
                    } catch (Exception e) {
                        // Skip duplicates or errors in CSV, but continue processing
                    }
                }
            }
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
        }
        return results;
    }

    private CourseResponse mapToResponse(Course course) {
        return new CourseResponse(
            course.getId(),
            course.getTitle(),
            course.getType(),
            course.getTerm(),
            course.getAcademicYear()
        );
    }
}
