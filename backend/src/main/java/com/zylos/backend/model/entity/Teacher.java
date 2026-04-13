package com.zylos.backend.model.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "teachers")
public class Teacher extends User {

    private String researchArea;
    private String chair;

    public Teacher() {
        super();
    }

    public Teacher(String firstName, String lastName, String email, String privateAddress,
                   String password, String profilePicture, String researchArea, String chair) {
        super(firstName, lastName, email, privateAddress, password, profilePicture);
        this.researchArea = researchArea;
        this.chair = chair;
    }

    public String getResearchArea() {
        return researchArea;
    }

    public void setResearchArea(String researchArea) {
        this.researchArea = researchArea;
    }

    public String getChair() {
        return chair;
    }

    public void setChair(String chair) {
        this.chair = chair;
    }
}
