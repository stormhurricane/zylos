package com.zylos.backend.database;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "LERNKARTENTHEMA")
public class LernkartenThema {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @NotNull
    @Column
    private int id;

    private String beschreibung;

    private int lvId;

    public LernkartenThema(String beschreibung, int lvId) {
        this.beschreibung = beschreibung;
        this.lvId = lvId;
    }

    public LernkartenThema() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getBeschreibung() {
        return beschreibung;
    }

    public void setBeschreibung(String beschreibung) {
        this.beschreibung = beschreibung;
    }

    public int getLvId() {
        return lvId;
    }

    public void setLvId(int lvId) {
        this.lvId = lvId;
    }
}
