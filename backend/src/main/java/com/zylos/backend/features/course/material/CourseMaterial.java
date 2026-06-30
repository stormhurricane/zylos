package com.zylos.backend.features.course.material;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import com.zylos.backend.features.course.Course;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "course_materials")
@Getter 
@Setter 
@NoArgsConstructor 
public class CourseMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String title;

    @NotBlank
    private String fileName;

    private String contentType;

    @Lob
    @Column(columnDefinition = "LONGBLOB")
    private byte[] data;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id")
    @NotNull
    private Course course;

    @Setter(lombok.AccessLevel.NONE) 
    private long fileSize; 

    @CreationTimestamp 
    @Column(updatable = false)
    @Setter(lombok.AccessLevel.NONE) 
    private LocalDateTime createdAt;

    public CourseMaterial(String title, String fileName, String contentType, byte[] data, Course course) {
        this.title = title;
        this.fileName = fileName;
        this.contentType = contentType;
        this.data = data;
        this.course = course;
        this.fileSize = data != null ? data.length : 0; 
    }

    public void setData(byte[] data) {
        this.data = data;
        this.fileSize = data != null ? data.length : 0;
    }
}