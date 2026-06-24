package com.zylos.backend.features.user;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Getter @Setter @NoArgsConstructor
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotBlank
    @Column(nullable = false)
    private String firstName;

    @NotBlank
    @Column(nullable = false)
    private String lastName;

    @NotBlank
    @Email
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String privateAddress;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @Lob
    @Column(columnDefinition = "LONGTEXT")
    private String profilePicture;

    protected User(String firstName, String lastName, String email, String privateAddress, String password, String profilePicture) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.privateAddress = privateAddress;
        this.password = password;
        this.profilePicture = profilePicture;
    }

}
