package com.zylos.backend.features.user;

import com.zylos.backend.config.security.JwtService;
import com.zylos.backend.config.security.Role;
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

@Service
@RequiredArgsConstructor
class UserService {

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

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

        userRepository.save(teacher);
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

        userRepository.save(student);
    }

    public AuthResponse login(LoginRequest request) {
        String identifier = request.identifier();

        User user = userRepository.findByEmail(identifier).orElse(null);

        if (user == null) {
            user = studentRepository.findByMatriculationNumber(identifier).orElse(null);
        }

        if (user != null && passwordEncoder.matches(request.password(), user.getPassword())) {
            Role role = Role.STUDENT;
            if (user instanceof Teacher) {
                role = Role.INSTRUCTOR;
            }

            String token = jwtService.generateToken(user.getEmail(), user.getId(), List.of(role));
            return new AuthResponse(token, user.getId(), role, user.getFirstName(), user.getLastName());
        }

        throw new IllegalArgumentException("Invalid credentials");
    }

    @Transactional
    public boolean updateProfile(long userId, ProfileUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        
        if (request.password() != null && !request.password().isBlank()) user.setPassword(passwordEncoder.encode(request.password()));
        if (request.privateAddress() != null) user.setPrivateAddress(request.privateAddress());
        if (request.profilePicture() != null) user.setProfilePicture(request.profilePicture());

        if (user instanceof Teacher teacher) {
            if (request.chair() != null) teacher.setChair(request.chair());
            if (request.researchArea() != null) teacher.setResearchArea(request.researchArea());
        } else if (user instanceof Student student) {
            if (request.studySubject() != null) student.setStudySubject(request.studySubject());
        }

        userRepository.save(user); 
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

        if (user instanceof Student s) {
            return new ProfileResponse(
                    userId, user.getFirstName(), user.getLastName(), user.getEmail(), address,
                    user.getProfilePicture(), s.getMatriculationNumber(), s.getStudySubject(),
                    null, null
            );
        }

        if (user instanceof Teacher t) {
            return new ProfileResponse(
                    userId, user.getFirstName(), user.getLastName(), user.getEmail(), address,
                    user.getProfilePicture(), null, null,
                    t.getResearchArea(), t.getChair()
            );
        }

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