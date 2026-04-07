package com.zylos.backend.database;


import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;

@Entity
@Table(name = "vERSUCH")
public class Versuch {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @NotNull
    @Column
    private int id;

    private int nutzerId;

    private int testId;

    private boolean bestanden;

    public Versuch(int nutzerId, int testId, boolean bestanden) {
        this.nutzerId = nutzerId;
        this.testId = testId;
        this.bestanden = bestanden;
    }

    public Versuch() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id;}

    public int getNutzerId() {
        return nutzerId;
    }

    public void setNutzerId(int nutzerId) {
        this.nutzerId = nutzerId;
    }

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public boolean isBestanden() {
        return bestanden;
    }

    public void setBestanden(boolean bestanden) {
        this.bestanden = bestanden;
    }
}
