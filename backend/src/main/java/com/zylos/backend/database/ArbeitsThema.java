package com.zylos.backend.database;

import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;

@Entity
@Table(name = "ARBEITSTHEMA")
public class ArbeitsThema {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @NotNull
    @Column
    private int id;

    private int lehrendenId;

    @NotNull
    private String titel;

    @NotNull
    private String beschreibung;

    @NotNull
    @Lob
    private String literaturliste;

    public ArbeitsThema() {
    }

    public ArbeitsThema(String titel, String beschreibung) {
        this.titel = titel;
        this.beschreibung = beschreibung;
    }

    public int getId() {
        return id;
    }

    public int getLehrendenId() {
        return lehrendenId;
    }

    public void setLehrendenId(int lehrendenId) {
        this.lehrendenId = lehrendenId;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public String getLiteraturliste() {
        return literaturliste;
    }

    public void setLiteraturliste(String literaturliste) {
        this.literaturliste = literaturliste;
    }
}
