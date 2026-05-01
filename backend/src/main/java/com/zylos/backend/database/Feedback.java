package com.zylos.backend.database;


import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Entity
@Table(name = "FEEDBACK")
@Deprecated(since = "2024-06", forRemoval = true)
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @NotNull
    @Column
    private int id;

    private int versuchId;

    private int frageId;

    private boolean abgegebeneAntwort;

    public Feedback(int versuchId, int frageId, boolean abgegebeneAntwort) {
        this.versuchId = versuchId;
        this.frageId = frageId;
        this.abgegebeneAntwort = abgegebeneAntwort;
    }

    public Feedback() {
    }

    public int getId() {
        return id;
    }

    public int getVersuchId() {
        return versuchId;
    }

    public void setVersuchId(int versuchId) {
        this.versuchId = versuchId;
    }

    public int getFrageId() {
        return frageId;
    }

    public void setFrageId(int frageId) {
        this.frageId = frageId;
    }

    public boolean isAbgegebeneAntwort() {
        return abgegebeneAntwort;
    }

    public void setAbgegebeneAntwort(boolean abgegebeneAntwort) {
        this.abgegebeneAntwort = abgegebeneAntwort;
    }
}
