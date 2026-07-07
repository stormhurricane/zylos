package com.zylos.backend.features.course.staff;

import com.zylos.backend.features.course.Course;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Table(
    name = "course_staff", 
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "course_id"}) 
    }
)
@Getter @Setter @NoArgsConstructor
public class CourseStaff {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "user_id")
    private long userId; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false, foreignKey = @ForeignKey(name = "fk_staff_course"))    
    @NotNull
    private Course course;

    @Column(name = "role", nullable = false)
    @Enumerated(EnumType.STRING)
    private StaffRole role;

    private LocalDateTime assignedAt = LocalDateTime.now();

    public CourseStaff(long userId, Course course, StaffRole role) {
        this.userId = userId;
        this.course = course;
        this.role = role;
    }
}