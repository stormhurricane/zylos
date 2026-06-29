package com.zylos.backend.features.course;

import com.zylos.backend.features.course.dto.CourseRequest;
import com.zylos.backend.features.course.dto.CourseResponse;
import com.zylos.backend.features.course.enrollment.EnrollmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CourseCreationOrchestrator {

    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    /**
     * Erstellt einen Kurs und schreibt den Ersteller atomar als Dozent ein.
     */
    @Transactional(rollbackFor = Exception.class)
    public CourseResponse createCourseWithInstructor(CourseRequest request, long instructorId) {
        CourseResponse course = courseService.createCourse(request);
        enrollmentService.assignInstructor(course.id(), instructorId);        
        return course;
    }

    /**
     * Importiert Kurse aus einer CSV-Datei und schreibt den Dozenten für jeden Kurs transaktionssicher ein.
     */
    @Transactional(rollbackFor = Exception.class)
    public List<CourseResponse> importFromCsvWithInstructor(MultipartFile file, long instructorId) {
        List<CourseResponse> importedCourses = courseService.importFromCsv(file);
        
        for (CourseResponse course : importedCourses) {
            enrollmentService.assignInstructor(course.id(), instructorId);
        }
        
        return importedCourses;
    }
}