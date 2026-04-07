package com.zylos.backend.database;

import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;

@Entity
@Table(name = "LERNKARTE")
public class Lernkarte {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @NotNull
    @Column
    private int id;

    private int lernkartenThemaId;

    private String frage;

    private String antwort;

    public Lernkarte(int lernkartenThemaId, String frage, String antwort) {
        this.lernkartenThemaId = lernkartenThemaId;
        this.frage = frage;
        this.antwort = antwort;
    }

    public Lernkarte() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLernkartenThemaId() {
        return lernkartenThemaId;
    }

    public void setLernkartenThemaId(int lernkartenThemaId) {
        this.lernkartenThemaId = lernkartenThemaId;
    }

    public String getFrage() {
        return frage;
    }

    public void setFrage(String frage) {
        this.frage = frage;
    }

    public String getAntwort() {
        return antwort;
    }

    public void setAntwort(String antwort) {
        this.antwort = antwort;
    }
}
