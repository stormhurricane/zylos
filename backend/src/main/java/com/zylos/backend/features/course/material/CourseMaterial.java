package com.zylos.backend.features.course.material;

import java.time.LocalDateTime;
import org.hibernate.annotations.CreationTimestamp;
import com.zylos.backend.features.course.Course;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "course_materials")
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

    private long fileSize; 

    @CreationTimestamp 
    @Column(updatable = false)
    private LocalDateTime createdAt;

    public CourseMaterial() {
    }

    public CourseMaterial(String title, String fileName, String contentType, byte[] data, Course course) {
        this.title = title;
        this.fileName = fileName;
        this.contentType = contentType;
        this.data = data;
        this.course = course;
        this.fileSize = data != null ? data.length : 0; 
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

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getContentType() {
        return contentType;
    }

    public void setContentType(String contentType) {
        this.contentType = contentType;
    }

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
        this.fileSize = data != null ? data.length : 0; // FIX: Synchronisiert die Größe bei manuellem Tausch
    }

    public Course getCourse() {
        return course;
    }

    public void setCourse(Course course) {
        this.course = course;
    }

    // FIX: Fehlender Getter für fileSize
    public long getFileSize() {
        return fileSize;
    }

    // FIX: Fehlender Getter für createdAt (Kein Setter nötig, da @CreationTimestamp)
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
}