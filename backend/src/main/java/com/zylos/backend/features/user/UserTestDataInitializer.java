package com.zylos.backend.features.user;

import com.zylos.backend.features.user.dto.StudentRegistrationRequest;
import com.zylos.backend.features.user.dto.TeacherRegistrationRequest;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Profile("dev")
@Order(1) // [Certain] Muss zuerst laufen, damit die User vor den Kursen existieren!
@RequiredArgsConstructor
class UserTestDataInitializer implements CommandLineRunner {

    private final UserService userService; // Sieht den Service, da im selben Paket!
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        if (userService.isDatabaseEmpty()) { // Funktioniert jetzt perfekt!            
            System.out.println("======> [User-Slice] Erstelle User-Testdaten...");

            userService.registerTeacher(new TeacherRegistrationRequest(
                "Dr. Max", "Mustermann", "password", "teacher@zylos.com", null, "Musterstraße 1", "Software Engineering", "Chair of AI"
            ));

            userService.registerStudent(new StudentRegistrationRequest(
                "Anna", "Schmidt", "password", "anna@zylos.com", null, "Studentenallee 5", "Informatik"
            ));

            userService.registerStudent(new StudentRegistrationRequest(
                "Bob", "DerBaumeister", "password", "bob@zylos.com", null, "Bauplatz 4", "Bauingenieurwesen"
            ));

            userRepository.flush();
        }
    }
}