package com.zylos.backend.auth;

import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.BaseIntegrationTest;
import com.zylos.backend.features.user.SystemRole;
import com.zylos.backend.features.user.UserRepository;
import com.zylos.backend.features.user.UserStatus;
import com.zylos.backend.features.user.entity.User;

public class AuthRegistrationIntegrationTest extends BaseIntegrationTest {

    @Autowired 
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test 
    public void shouldRegisterStudentUser_withStatusApproved() {
        User user = registerUserAndFetchFromDb("jane.doe@example.com", SystemRole.STUDENT);
        
        assertEquals("Test", user.getFirstName());
        assertEquals("User", user.getLastName());
        assertEquals("janedoe", user.getUsername());
        assertEquals(SystemRole.STUDENT, user.getRole());
        
        assertEquals(UserStatus.APPROVED, user.getStatus(), "STUDENT role must be initialized as APPROVED");

        assertNotEquals("SecurePassword123!", user.getPassword(), "Raw password must NEVER be stored in DB");
        assertTrue(passwordEncoder.matches("SecurePassword123!", user.getPassword()), "Hashed password must match input");

        assertNotNull(user.getCreatedAt());
        assertNotNull(user.getUpdatedAt());
    }


    @Test
    public void shouldRegisterTeacherUser_withStatusPending(){
        User user = registerUserAndFetchFromDb("john.smith@example.com", SystemRole.TEACHER);
        
        assertEquals(SystemRole.TEACHER, user.getRole());
        assertEquals(UserStatus.PENDING, user.getStatus(), "TEACHER role must be initialized as PENDING");
    }

    @Test
    public void shouldReturnConflict_whenEmailAlreadyExists(){
        User user = User.create(
            "Test", 
            "User", 
            "duplicate@example.com", 
            "duplicateUser", 
            passwordEncoder.encode("SecurePassword123!"),
            SystemRole.STUDENT
        );
        
        userRepository.save(user);

        String requestBody = """
            {
                "firstName": "John",
                "lastName": "Smith",
                "email": "duplicate@example.com",
                "username": "johnsmith",
                "password": "SecurePassword123!",
                "role": "%s"
            }        
        """.formatted(SystemRole.STUDENT.name());

        given()
            .contentType("application/json")
            .body(requestBody)
        .when()
            .post("/api/v1/auth/register")
        .then()
            // .log().all()
            .statusCode(HttpStatus.CONFLICT.value())
            .body("message", org.hamcrest.Matchers.containsStringIgnoringCase("Registration failed due to conflicting user data"))
            .body("errors", org.hamcrest.Matchers.hasKey("email"));

    }

    @Test
    public void shouldReturnBadRequest_whenValidationFails(){
        String requestBody = """
            {
                "firstName": "John",
                "lastName": "Smith",
                "email": "invalid-email-format",
                "password": "short"
            }        
        """;

        given()
            .contentType("application/json")
            .body(requestBody)
        .when()
            .post("/api/v1/auth/register")
        .then()
            // .log().all()
            .statusCode(HttpStatus.BAD_REQUEST.value())
            .body("message", org.hamcrest.Matchers.containsStringIgnoringCase("Validation failed"))
            .body("errors", org.hamcrest.Matchers.hasKey("email"))
            .body("errors", org.hamcrest.Matchers.hasKey("password"))
            .body("errors", org.hamcrest.Matchers.hasKey("username"));
    }


     private User registerUserAndFetchFromDb(String email, SystemRole role){
        String username = email.split("@")[0].replace(".", "");
        String requestBody = """
            {
                "firstName": "Test",
                "lastName": "User",
                "email": "%s",
                "username": "%s",
                "password": "SecurePassword123!",
                "role": "%s"
            }        
        """.formatted(email, username, role.name());

        given()
            .contentType("application/json")
            .body(requestBody)
        .when()
            .post("/api/v1/auth/register")
        .then()
            //.log().all()
            .statusCode(HttpStatus.NO_CONTENT.value());

        return userRepository.findByEmail(email)
            .orElseThrow(() -> new AssertionError("User should exist in database"));
    }
    
}
