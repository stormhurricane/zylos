package com.zylos.backend.service;

import com.zylos.backend.model.dto.AuthResponse;
import com.zylos.backend.model.dto.LoginRequest;
import com.zylos.backend.model.dto.ProfileResponse;
import com.zylos.backend.model.dto.ProfileUpdateRequest;
import com.zylos.backend.model.dto.StudentRegistrationRequest;
import com.zylos.backend.model.dto.TeacherRegistrationRequest;
import com.zylos.backend.model.entity.Student;
import com.zylos.backend.model.entity.Teacher;
import com.zylos.backend.model.entity.User;
import com.zylos.backend.repository.StudentRepository;
import com.zylos.backend.repository.TeacherRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class UserService {

    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Autowired
    public UserService(StudentRepository studentRepository, 
                       TeacherRepository teacherRepository, 
                       UserRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService) {
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @Transactional
    public void registerTeacher(TeacherRegistrationRequest request) {
        validateEmailUniqueness(request.email());

        Teacher teacher = new Teacher(
            request.firstName(),
            request.lastName(),
            request.email(),
            request.privateAddress(),
            passwordEncoder.encode(request.password()),
            request.profilePicture(),
            request.researchArea(),
            request.chair()
        );

        teacherRepository.save(teacher);
    }

    public AuthResponse login(LoginRequest request) {
        String identifier = request.identifier();

        // 1. Versuch: Student via Matrikelnummer
        Optional<Student> studentOpt = studentRepository.findByMatriculationNumber(identifier);

        // 2. Versuch: Student via Email (falls 1. nicht erfolgreich)
        if (studentOpt.isEmpty()) {
            studentOpt = studentRepository.findByEmail(identifier);
        }

        if (studentOpt.isPresent()) {
            Student student = studentOpt.get();
            if (passwordEncoder.matches(request.password(), student.getPassword())) {
                String token = jwtService.generateToken(student.getEmail(), Map.of("role", "STUDENT", "userId", student.getId()));
                return new AuthResponse(token, "STUDENT", student.getFirstName(), student.getLastName());
            }
        }

        // 3. Versuch: Lehrender via Email
        Optional<Teacher> teacherOpt = teacherRepository.findByEmail(identifier);
        if (teacherOpt.isPresent()) {
            Teacher teacher = teacherOpt.get();
            if (passwordEncoder.matches(request.password(), teacher.getPassword())) {
                String token = jwtService.generateToken(teacher.getEmail(), Map.of("role", "TEACHER", "userId", teacher.getId()));
                return new AuthResponse(token, "TEACHER", teacher.getFirstName(), teacher.getLastName());
            }
        }

        throw new IllegalArgumentException("Invalid email or password");
    }

    @Transactional
    public void registerStudent(StudentRegistrationRequest request) {
        validateEmailUniqueness(request.email());

        String matriculationNumber = generateUniqueMatriculationNumber();

        Student student = new Student(
            request.firstName(),
            request.lastName(),
            request.email(),
            request.privateAddress(),
            passwordEncoder.encode(request.password()),
            request.profilePicture(),
            matriculationNumber,
            request.studySubject()
        );

        studentRepository.save(student);
    }

    @Transactional
    public boolean updateProfile(int userId, ProfileUpdateRequest request) {
        Optional<? extends User> userOpt = findUserById(userId);
        if (userOpt.isEmpty()) return false;

        User user = userOpt.get();
        
        // Update common fields
        if (request.password() != null) user.setPassword(passwordEncoder.encode(request.password()));
        if (request.privateAddress() != null) user.setPrivateAddress(request.privateAddress());
        if (request.profilePicture() != null) user.setProfilePicture(request.profilePicture());

        // Update role-specific fields
        if (user instanceof Teacher teacher) {
            if (request.chair() != null) teacher.setChair(request.chair());
            if (request.researchArea() != null) teacher.setResearchArea(request.researchArea());
            teacherRepository.save(teacher);
        } else if (user instanceof Student student) {
            if (request.studySubject() != null) student.setStudySubject(request.studySubject());
            studentRepository.save(student);
        }

        return true;
    }

    public ProfileResponse getUserProfile(int userId, boolean isFullProfile) {
        return findUserById(userId)
                .map(user -> convertToResponse(user, isFullProfile))
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    public List<ProfileResponse> searchUsers(String searchTerm) {
        List<ProfileResponse> results = new ArrayList<>();

        studentRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(searchTerm, searchTerm)
                .forEach(s -> results.add(convertToResponse(s, false)));

        teacherRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(searchTerm, searchTerm)
                .forEach(t -> results.add(convertToResponse(t, false)));

        return results;
    }

    private ProfileResponse convertToResponse(User user, boolean includeSensitiveData) {
        String address = includeSensitiveData ? user.getPrivateAddress() : null;

        if (user instanceof Student s) {
            return new ProfileResponse(
                    s.getId(),
                    s.getFirstName(), s.getLastName(), s.getEmail(), address,
                    s.getProfilePicture(), s.getMatriculationNumber(), s.getStudySubject(),
                    null, null
            );
        } else if (user instanceof Teacher t) {
            return new ProfileResponse(
                    t.getId(),
                    t.getFirstName(), t.getLastName(), t.getEmail(), address,
                    t.getProfilePicture(), null, null,
                    t.getResearchArea(), t.getChair()
            );
        }
        throw new IllegalStateException("Unknown user type");
    }

    public int getCurrentUserId() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return findUserByEmail(email)
                .map(User::getId)
                .orElseThrow(() -> new IllegalArgumentException("User not found in context"));
    }

    private void validateEmailUniqueness(String email) {
        if (studentRepository.existsByEmail(email) || teacherRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use");
        }
    }

    private synchronized String generateUniqueMatriculationNumber() {
        // Findet die höchste Nummer und zählt hoch, startet bei 1000000
        String nextNumber = studentRepository.findMaxMatriculationNumber()
                .map(max -> String.valueOf(Long.parseLong(max) + 1))
                .orElse("1000000");

        if (nextNumber.length() > 7) {
            throw new IllegalStateException("Matriculation number limit reached (max 7 digits)");
        }
        return nextNumber;
    }

    private Optional<? extends User> findUserById(int id) {
        return userRepository.findById(id);
    }

    private Optional<? extends User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
