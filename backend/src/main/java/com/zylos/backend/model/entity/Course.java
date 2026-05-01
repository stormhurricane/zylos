package com.zylos.backend.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "courses")
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(unique = true)
    private String title;

    @NotNull
    @Enumerated(EnumType.STRING)
    private CourseType type;

    @NotNull
    @Enumerated(EnumType.STRING)
    private SemesterTerm term;

    @NotBlank
    private String academicYear; // e.g., "2024" or "2024/25"

    public Course() {
    }

    public Course(Long id, String title, CourseType type, SemesterTerm term, String academicYear) {
        this(title, type, term, academicYear);
        this.id = id;
    }

    public Course(String title, CourseType type, SemesterTerm term, String academicYear) {
        this.title = title;
        this.type = type;
        this.term = term;
        this.academicYear = academicYear;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public CourseType getType() {
        return type;
    }

    public void setType(CourseType type) {
        this.type = type;
    }

    public SemesterTerm getTerm() {
        return term;
    }

    public void setTerm(SemesterTerm term) {
        this.term = term;
    }

    public String getAcademicYear() {
        return academicYear;
    }

    public void setAcademicYear(String academicYear) {
        this.academicYear = academicYear;
    }
}