package com.zylos.backend.features.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "students")
@Getter @Setter @NoArgsConstructor
public class Student {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(name = "user_id", unique = true)
    private long userId;

    @Column(unique = true, nullable = false, length = 7)
    @Size(min = 7, max = 7)
    private String matriculationNumber;

    @Column(nullable = false)
    private String studySubject;
    

    public Student(long userId, String matriculationNumber, String studySubject) {
        this.userId = userId;
        this.matriculationNumber = matriculationNumber;
        this.studySubject = studySubject;
    }


}
