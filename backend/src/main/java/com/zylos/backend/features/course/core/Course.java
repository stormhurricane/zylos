package com.zylos.backend.features.course.core;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Table(name = "courses")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    public Course(String title, CourseType type, SemesterTerm term, String academicYear) {
        this.title = title;
        this.type = type;
        this.term = term;
        this.academicYear = academicYear;
    }

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank @Column(unique = true)
    private String title;

    @NotNull @Enumerated(EnumType.STRING)
    private CourseType type;

    @NotNull @Enumerated(EnumType.STRING)
    private SemesterTerm term;

    @NotBlank
    private String academicYear;
}