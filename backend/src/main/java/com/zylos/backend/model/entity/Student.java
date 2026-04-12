package com.zylos.backend.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Size;

@Entity
@Table(name = "students")
public class Student extends User {

    @Column(unique = true, nullable = false, length = 7)
    @Size(min = 7, max = 7)
    private String matriculationNumber;

    @Column(nullable = false)
    private String studySubject;

    public Student() {
        super();
    }

    public Student(String firstName, String lastName, String email, String privateAddress,
                   String password, String profilePicture, String matriculationNumber, String studySubject) {
        super(firstName, lastName, email, privateAddress, password, profilePicture);
        this.matriculationNumber = matriculationNumber;
        this.studySubject = studySubject;
    }

    public String getMatriculationNumber() {
        return matriculationNumber;
    }

    public void setMatriculationNumber(String matriculationNumber) {
        this.matriculationNumber = matriculationNumber;
    }

    public String getStudySubject() {
        return studySubject;
    }

    public void setStudySubject(String studySubject) {
        this.studySubject = studySubject;
    }
}
