package com.zylos.backend.database;

import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;


@Entity
@Table(name = "Student")
public class Student extends Nutzer{

    private int matrikelnr;

    @Column
    @NotNull
    private String studienfach;


    public Student() {

    }

    public Student(String vorname, String nachname, String email, String adresse,
                   String passwort,String profilbild, int matrikelnr, String studienfach) {
        super(vorname, nachname, email, adresse, passwort, profilbild);
        this.matrikelnr = matrikelnr;
        this.studienfach = studienfach;
    }

    public Student(String vorname, String nachname, String email,
                   String adresse, String passwort,String profilbild, String studienfach) {
        super(vorname, nachname, email, adresse, passwort, profilbild);
        this.studienfach = studienfach;
    }

    public int getMatrikelnr() {
        return matrikelnr;
    }

    public void setMatrikelnr(int matrikelnr) {
        this.matrikelnr = matrikelnr;
    }

    public String getStudienfach() {
        return studienfach;
    }

    public void setStudienfach(String studienfach) {
        this.studienfach = studienfach;
    }
}
