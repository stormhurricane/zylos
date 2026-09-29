package com.zylos.backend.features.user.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import com.zylos.backend.features.user.SystemRole;
import com.zylos.backend.features.user.UserStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users") 
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter @Setter
public class User {

    @Id 
    @GeneratedValue (strategy = GenerationType.UUID)
    private UUID id;

    @Column (nullable = false)
    private String firstName;

    @Column (nullable = false)
    private String lastName;
    
    @Column (nullable = false, unique = true)
    private String email;
    
    @Column (nullable = false, unique = true, length = 100)
    private String username;
    
    @Column (nullable = false, length = 60)
    private String password;

    @Enumerated (EnumType.STRING)
    @Column (nullable = false)
    private SystemRole role = SystemRole.STUDENT;
    
    @Enumerated (EnumType.STRING)
    @Column (nullable = false)
    private UserStatus status = UserStatus.PENDING;

    @CreationTimestamp 
    @Column (nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp  
    @Column (nullable = false)
    private LocalDateTime updatedAt;

    private User(String firstName, String lastName, String email, String username, String password, SystemRole role, UserStatus status) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.username = username;
        this.password = password;
        this.role = role;
        this.status = status;
    }

    public static User create(String firstName, String lastName, String email, String username, String password, SystemRole role) {
        if (role == SystemRole.ADMIN) {
            throw new IllegalArgumentException("Cannot create a user with ADMIN role");
        }

        UserStatus initialStatus = (role == SystemRole.TEACHER) ? UserStatus.PENDING : UserStatus.APPROVED;
        return new User(firstName, lastName, email, username, password, role, initialStatus);
    }
    
}
