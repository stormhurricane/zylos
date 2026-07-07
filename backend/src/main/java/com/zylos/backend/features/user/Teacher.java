package com.zylos.backend.features.user;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "teachers")
@DiscriminatorValue("TEACHER")
@Getter
@Setter
@NoArgsConstructor
public class Teacher extends User {

    private String researchArea;
    private String chair;

    public Teacher(String firstName, String lastName, String email, String privateAddress, String password, String profilePicture, String researchArea, String chair) {
        super(firstName, lastName, email, privateAddress, password, profilePicture);
        this.researchArea = researchArea;
        this.chair = chair;
    }
}