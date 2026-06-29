package com.zylos.backend.features.course;

import java.util.ArrayList;
import java.util.List;

import com.zylos.backend.features.course.enrollment.Enrollment;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;

@Entity
@Table(name = "courses")
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
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

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private List<Enrollment> enrollments = new ArrayList<>();
}