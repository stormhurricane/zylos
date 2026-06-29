package com.zylos.backend.features.course;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.zylos.backend.features.course.dto.CourseRequest;
import com.zylos.backend.features.course.dto.CourseResponse;
import com.zylos.backend.features.course.exceptions.CourseAlreadyExistsException;
import com.zylos.backend.features.course.exceptions.CourseNotFoundException;

import org.springframework.stereotype.Service;
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

    public CourseService(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public CourseResponse createCourse(CourseRequest request) {
        if (courseRepository.findByTitle(request.title()).isPresent()) {
            // FIX: Typisierte Business-Exception statt unsauberer RuntimeException
            throw new CourseAlreadyExistsException(request.title());
        }
        Course course = new Course(request.title(), request.type(), request.term(), request.academicYear());
        Course savedCourse = courseRepository.save(course);
        
        return mapToResponse(savedCourse);
    }

    public CourseResponse getCourseById(Long id) {
        logger.info("Service: Fetching course details for ID: {}", id);
        return courseRepository.findById(id)
                .map(this::mapToResponse)
                .orElseThrow(() -> {
                    logger.error("Service: Course with ID {} not found in database", id);
                    // FIX: Typisierte Business-Exception statt unsauberer RuntimeException
                    return new CourseNotFoundException(id);
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

    public List<CourseResponse> importFromCsv(MultipartFile file) {
        List<CourseResponse> results = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = br.readLine()) != null) {
                String separator = line.contains(";") ? ";" : ",";
                String[] data = line.split(separator);
                
                if (data.length >= 4) {
                    String title = data[0].trim();
                    
                    if (courseRepository.findByTitle(title).isPresent()) {
                        logger.warn("CSV-Import: Course with title '{}' already exists. Skipping line.", title);
                        continue; 
                    }

                    // FIX: Defensives Enum-Parsing schützt vor korrupten CSV-Zeilen!
                    try {
                        CourseType type = CourseType.valueOf(data[1].trim().toUpperCase());
                        SemesterTerm term = SemesterTerm.valueOf(data[2].trim().toUpperCase());
                        String academicYear = data[3].trim();

                        CourseRequest request = new CourseRequest(title, type, term, academicYear);
                        results.add(createCourse(request));
                    } catch (IllegalArgumentException e) {
                        logger.error("CSV-Import: Invalid Enum value in line: '{}'. Skipping.", line);
                        // Ignoriert die fehlerhafte Zeile, bricht aber nicht den gesamten Import ab!
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
            course.getAcademicYear(),
            course.getEnrollments().size()
        );
    }
    // FIX: isDatabaseEmpty() restlos eliminiert
}