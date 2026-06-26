package com.zylos.backend.features.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "students")
@DiscriminatorValue("STUDENT")
@Getter
@Setter
@NoArgsConstructor
public class Student extends User {

    @Column(unique = true)
    private String matriculationNumber;

    private String studySubject;

    public Student(String firstName, String lastName, String email, String privateAddress, String password, String profilePicture, String matriculationNumber, String studySubject) {
        super(firstName, lastName, email, privateAddress, password, profilePicture);
        this.matriculationNumber = matriculationNumber;
        this.studySubject = studySubject;
    }
}