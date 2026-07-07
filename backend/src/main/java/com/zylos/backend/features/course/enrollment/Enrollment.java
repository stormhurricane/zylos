package com.zylos.backend.features.course.enrollment; 

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import java.time.LocalDateTime;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.zylos.backend.features.course.Course;

import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(
    name = "enrollments", 
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "course_id"}) 
    }
)
@Getter @Setter @NoArgsConstructor
public class Enrollment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "user_id")
    private long userId; 

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "course_id", 
        nullable = false, 
        foreignKey = @ForeignKey(name = "fk_enrollments_course") 
    )    
    @NotNull
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Course course;

    private LocalDateTime enrolledAt = LocalDateTime.now();

    public Enrollment(long userId, Course course) {
        this.userId = userId;
        this.course = course;
    }
}