package com.zylos.backend.features.user;

import com.zylos.backend.config.security.JwtService;
import com.zylos.backend.features.user.dto.AuthResponse;
import com.zylos.backend.features.user.dto.LoginRequest;
import com.zylos.backend.features.user.dto.ProfileResponse;
import com.zylos.backend.features.user.dto.ProfileUpdateRequest;
import com.zylos.backend.features.user.dto.StudentRegistrationRequest;
import com.zylos.backend.features.user.dto.TeacherRegistrationRequest;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor // Macht den fetten @Autowired-Konstruktor überflüssig!
class UserService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public void registerTeacher(TeacherRegistrationRequest request) {
        validateEmailUniqueness(request.email());

        // 1. Basis-User anlegen
        User user = new User(
            request.firstName(),
            request.lastName(),
            request.email(),
            request.privateAddress(),
            passwordEncoder.encode(request.password()),
            request.profilePicture()
        );
        user = userRepository.save(user);

        // 2. Teacher-spezifische Erweiterung speichern
        Teacher teacher = new Teacher(
            user.getId(), // Nutzt die generierte Long-ID als Shared Primary Key
            request.researchArea(),
            request.chair()
        );
        teacherRepository.save(teacher);
    }

    @Transactional
    public void registerStudent(StudentRegistrationRequest request) {
        validateEmailUniqueness(request.email());

        // 1. Basis-User anlegen
        User user = new User(
            request.firstName(),
            request.lastName(),
            request.email(),
            request.privateAddress(),
            passwordEncoder.encode(request.password()),
            request.profilePicture()
        );
        user = userRepository.save(user);

        String matriculationNumber = generateUniqueMatriculationNumber();

        // 2. Student-spezifische Erweiterung speichern
        Student student = new Student(
            user.getId(), // Nutzt die generierte Long-ID als Shared Primary Key
            matriculationNumber,
            request.studySubject()
        );
        studentRepository.save(student);
    }

    public AuthResponse login(LoginRequest request) {
        String identifier = request.identifier();

        // [Certain] Strategie-Wechsel: Erst nach Matrikelnummer suchen, sonst nach Email
        User user = studentRepository.findByMatriculationNumber(identifier)
                .flatMap(student -> userRepository.findById(student.getUserId()))
                .orElseGet(() -> userRepository.findByEmail(identifier).orElse(null));

        if (user != null && passwordEncoder.matches(request.password(), user.getPassword())) {
            // Rolle über die Existenz in den Sub-Tabellen ermitteln
            String role = "STUDENT";
            if (teacherRepository.existsByUserId(user.getId())) {
                role = "TEACHER";
            }
            
            String token = jwtService.generateToken(user.getEmail(), Map.of("role", role, "userId", user.getId()));
            return new AuthResponse(token, user.getId(), role, user.getFirstName(), user.getLastName());
        }

        throw new IllegalArgumentException("Invalid credentials");
    }

    @Transactional
    public boolean updateProfile(long userId, ProfileUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        // Gemeinsame Felder aktualisieren
        if (request.password() != null && !request.password().isBlank()) user.setPassword(passwordEncoder.encode(request.password()));
        if (request.privateAddress() != null) user.setPrivateAddress(request.privateAddress());
        if (request.profilePicture() != null) user.setProfilePicture(request.profilePicture());
        userRepository.save(user);

        // Rollenspezifische Felder über getrennte Repositories aktualisieren
        Optional<Teacher> teacherOpt = teacherRepository.findByUserId(userId);
        if (teacherOpt.isPresent()) {
            Teacher teacher = teacherOpt.get();
            if (request.chair() != null) teacher.setChair(request.chair());
            if (request.researchArea() != null) teacher.setResearchArea(request.researchArea());
            teacherRepository.save(teacher);
            return true;
        }

        Optional<Student> studentOpt = studentRepository.findByUserId(userId);
        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            if (request.studySubject() != null) student.setStudySubject(request.studySubject());
            studentRepository.save(student);
            return true;
        }

        return true;
    }

    public ProfileResponse getUserProfile(long userId, boolean isFullProfile) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        return convertToResponse(user, isFullProfile);
    }

    public List<ProfileResponse> searchUsers(String searchTerm) {
        return userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(searchTerm, searchTerm)
                .stream()
                .map(user -> convertToResponse(user, false))
                .toList();
    }

    private ProfileResponse convertToResponse(User user, boolean includeSensitiveData) {
        String address = includeSensitiveData ? user.getPrivateAddress() : null;
        long userId = user.getId();

        // Versuche die Sub-Profile zu laden, um das DTO flach zusammenzubauen
        Optional<Student> studentOpt = studentRepository.findByUserId(userId);
        if (studentOpt.isPresent()) {
            Student s = studentOpt.get();
            return new ProfileResponse(
                    userId, user.getFirstName(), user.getLastName(), user.getEmail(), address,
                    user.getProfilePicture(), s.getMatriculationNumber(), s.getStudySubject(),
                    null, null
            );
        }

        Optional<Teacher> teacherOpt = teacherRepository.findByUserId(userId);
        if (teacherOpt.isPresent()) {
            Teacher t = teacherOpt.get();
            return new ProfileResponse(
                    userId, user.getFirstName(), user.getLastName(), user.getEmail(), address,
                    user.getProfilePicture(), null, null,
                    t.getResearchArea(), t.getChair()
                );
        }

        // Fallback für unkategorisierte Basis-User
        return new ProfileResponse(
                userId, user.getFirstName(), user.getLastName(), user.getEmail(), address,
                user.getProfilePicture(), null, null, null, null
        );
    }

    private void validateEmailUniqueness(String email) {
        if (userRepository.findByEmail(email).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }
    }

    private synchronized String generateUniqueMatriculationNumber() {
        String nextNumber = studentRepository.findMaxMatriculationNumber()
                .map(max -> String.valueOf(Long.parseLong(max) + 1))
                .orElse("1000000");

        if (nextNumber.length() > 7) {
            throw new IllegalStateException("Matriculation number limit reached (max 7 digits)");
        }
        return nextNumber;
    }

    boolean isDatabaseEmpty() {
        return userRepository.count() == 0;
    }
}