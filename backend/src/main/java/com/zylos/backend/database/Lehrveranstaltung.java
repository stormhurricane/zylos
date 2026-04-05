package com.zylos.backend.database;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sun.istack.NotNull;

import javax.persistence.*;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Entity
@Table(name = "Lehrveranstaltung")
public class Lehrveranstaltung {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @NotNull
    @JsonProperty("lehrveranstaltungsID")
    private int lehrveranstaltungsID;
    @JsonProperty("titel")
    private String titel;
    @JsonProperty("semesterZeit")
    private zeitEnum semesterZeit;
    @JsonProperty("semesterJahr")
    private String semesterJahr;
    @JsonProperty("typ")
    private typEnum typ;


    public Lehrveranstaltung() {
    }

    public Lehrveranstaltung(String titel, String semesterJahr, typEnum typ, zeitEnum semesterZeit) {
        this.titel = titel;
        this.semesterJahr = semesterJahr;
        this.typ = typ;
        this.semesterZeit = semesterZeit;
    }

    public int getLehrveranstaltungsID() {
        return lehrveranstaltungsID;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public typEnum getTyp() {
        return typ;
    }

    public zeitEnum getSemesterZeit() {
        return semesterZeit;
    }

    public String getSemesterJahr() {
        return semesterJahr;
    }

    public enum typEnum { VORLESUNG, SEMINAR }

    public enum zeitEnum { WS, SS }
}
