package com.zylos.backend.database;


import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "BEWERTUNGSFEEDBACK")
@Deprecated(since = "2024-06", forRemoval = true)
public class BewertungsFeedback extends Feedback {

    private char antwort;

    public BewertungsFeedback(int versuchId, int frageId, boolean abgegebeneAntwort, char antwort) {
        super(versuchId, frageId, abgegebeneAntwort);
        this.antwort = antwort;
    }

    public BewertungsFeedback() {
    }

    public char getAntwort() {
        return antwort;
    }

    public void setAntwort(char antwort) {
        this.antwort = antwort;
    }
}
