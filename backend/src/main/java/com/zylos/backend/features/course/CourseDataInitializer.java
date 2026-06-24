package com.zylos.backend.features.course;

import com.zylos.backend.features.course.dto.CourseRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@Order(2) // [Certain] Läuft erst, wenn die User aus Teil 1 garantiert in der DB sind
@RequiredArgsConstructor
class CourseTestDataInitializer implements CommandLineRunner {

    private final CourseService courseService;
    private final EnrollmentService enrollmentService;

    private final jakarta.persistence.EntityManager entityManager;

    @Override
    public void run(String... args) throws Exception {
        if (courseService.isDatabaseEmpty()) {            
        System.out.println("======> [Course-Slice] Erstelle Kurs-Testdaten...");

        // [Certain] IDs dynamisch anhand der E-Mails holen, die vom User-Slice angelegt wurden!
        Long teacherId;
        Long student1Id;
        
        try {
            teacherId = entityManager.createQuery(
                "SELECT u.id FROM User u WHERE u.email = :email", Long.class)
                .setParameter("email", "teacher@zylos.com") // Hier die echte Test-E-Mail des Dozenten nutzen
                .getSingleResult();

            student1Id = entityManager.createQuery(
                "SELECT u.id FROM User u WHERE u.email = :email", Long.class)
                .setParameter("email", "anna@zylos.com") // Hier die echte Test-E-Mail des Studenten nutzen
                .getSingleResult();
        } catch (jakarta.persistence.NoResultException e) {
            System.err.println("======> [Course-Slice] FEHLER: User-Testdaten wurden noch nicht geladen!");
            return;
        }

        // Jetzt halten wir gültige IDs in den Händen!
        courseService.createCourse(new CourseRequest("Software Engineering 21", CourseType.LECTURE, SemesterTerm.SUMMER, "2026"), teacherId);
        courseService.createCourse(new CourseRequest("Advanced Database Systems", CourseType.SEMINAR, SemesterTerm.WINTER, "2026"), teacherId);

        // Einschreibungen vornehmen (Wir holen die Kurse dynamisch über den Titel, um ID-Raten zu vermeiden)
        // Angenommen, dein Service bietet eine Möglichkeit oder du nutzt wieder den EM:
        Long course1Id = entityManager.createQuery("SELECT c.id FROM Course c WHERE c.title = :title", Long.class)
                .setParameter("title", "Software Engineering 21")
                .getSingleResult();

        try {
            System.out.println("======> [Course-Slice] Starte Enrollment für Dozent (ID: " + teacherId + ")...");
            enrollmentService.enrollUser(course1Id, teacherId);
            System.out.println("======> [Course-Slice] Dozent erfolgreich eingeschrieben.");

            System.out.println("======> [Course-Slice] Starte Enrollment für Student (ID: " + student1Id + ")...");
            enrollmentService.enrollUser(course1Id, student1Id);
            System.out.println("======> [Course-Slice] Student erfolgreich eingeschrieben.");
        } catch (Exception e) {
            System.err.println("======> [Course-Slice] CRASH BEIM ENROLLMENT!");
            e.printStackTrace(); // [Certain] Das zwingt den Fehler ans Tageslicht!
            throw e;
        }
    }
    }
}