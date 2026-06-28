package com.zylos.backend.features.user;

import com.zylos.backend.config.security.JwtService;
import com.zylos.backend.config.security.Role;
import com.zylos.backend.exception.EmailAlreadyExistsException;
import com.zylos.backend.exception.UserNotFoundException; 
import com.zylos.backend.exception.BadCredentialsException;
import com.zylos.backend.features.user.dto.*;

import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService { // FIX: Sichtbarkeit auf public gesetzt, falls Controller in anderem Package

    private final StudentRepository studentRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public void registerTeacher(TeacherRegistrationRequest request) {
        validateEmailUniqueness(request.email());

        Teacher teacher = new Teacher(
            request.firstName(), request.lastName(), request.email(),
            request.privateAddress(), passwordEncoder.encode(request.password()),
            request.profilePicture(), request.researchArea(), request.chair()
        );
        userRepository.save(teacher);
    }

    @Transactional
    public void registerStudent(StudentRegistrationRequest request) {
        validateEmailUniqueness(request.email());
        Long nextMatriculationNumber = studentRepository.getNextMatriculationNumber();

        Student student = new Student(
            request.firstName(), request.lastName(), request.email(),
            request.privateAddress(), passwordEncoder.encode(request.password()),
            request.profilePicture(), nextMatriculationNumber, request.studySubject()
        );
        userRepository.save(student);
    }

    public AuthResponse login(LoginRequest request) {
        String identifier = request.identifier().trim();
        User user = userRepository.findByEmail(identifier).orElse(null);

        if (user == null) {
            try {
                long matNum = Long.parseLong(identifier);
                user = studentRepository.findByMatriculationNumber(matNum).orElse(null);
            } catch (NumberFormatException e) {
                // Ignore parsing errors
            }
        }

        if (user != null && passwordEncoder.matches(request.password(), user.getPassword())) {
            // FIX: "instanceof" schützt vor Hibernate-Proxy-Fehlern
            Role role = (user instanceof Teacher) ? Role.INSTRUCTOR : Role.STUDENT;

            String token = jwtService.generateToken(user.getEmail(), user.getId(), List.of(role));
            return new AuthResponse(token, user.getId(), role, user.getFirstName(), user.getLastName());
        }

        throw new BadCredentialsException("Invalid credentials"); // FIX: 401 statt 400/500
    }

    @Transactional
    public void updateProfile(long userId, ProfileUpdateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId)); // FIX: 404 Exception
        
        if (request.password() != null && !request.password().isBlank()) {
            user.setPassword(passwordEncoder.encode(request.password()));
        }
        if (request.privateAddress() != null) user.setPrivateAddress(request.privateAddress());
        if (request.profilePicture() != null) user.setProfilePicture(request.profilePicture());

        if (user instanceof Teacher teacher) {
            if (request.chair() != null) teacher.setChair(request.chair());
            if (request.researchArea() != null) teacher.setResearchArea(request.researchArea());
        } else if (user instanceof Student student) {
            if (request.studySubject() != null) student.setStudySubject(request.studySubject());
        }
        userRepository.save(user); 
    }

    public ProfileResponse getUserProfile(long userId, boolean isFullProfile) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(userId)); // FIX: 404 Exception
        return convertToResponse(user, isFullProfile);
    }

    public List<UserSearchResponse> searchUsers(String searchTerm) {
        return userRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(searchTerm, searchTerm)
            .stream()
            .map(user -> new UserSearchResponse(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getProfilePicture(),
                (user instanceof Student student) ? student.getStudySubject() : 
                (user instanceof Teacher teacher) ? teacher.getChair() : null
            ))
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
            throw new EmailAlreadyExistsException(email);
        }
    }

}