package com.zylos.backend.features.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Inheritance(strategy = InheritanceType.JOINED) 
@DiscriminatorColumn(name = "user_type") 
@Getter
@Setter
@NoArgsConstructor
public abstract class User { 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false, unique = true)
    private String email;

    private String privateAddress;
    
    @Column(nullable = false)
    private String password;
    
    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String profilePicture;

    public User(String firstName, String lastName, String email, String privateAddress, String password, String profilePicture) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.privateAddress = privateAddress;
        this.password = password;
        this.profilePicture = profilePicture;
    }
}