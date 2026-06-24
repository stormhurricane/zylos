package com.zylos.backend.features.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "teachers")
@Getter @Setter @NoArgsConstructor
public class Teacher {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "user_id", unique = true)
    private long userId;

    @Column(nullable = false)
    private String researchArea;
    @Column(nullable = false)
    private String chair;

    public Teacher(long userId, String researchArea, String chair) {
        this.userId = userId;
        this.researchArea = researchArea;
        this.chair = chair;
    }

}
