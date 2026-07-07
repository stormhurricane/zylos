package com.zylos.backend.database;


import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;

@MappedSuperclass
@Deprecated(since = "2024-06", forRemoval = true)
public abstract class Nutzer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @NotNull
    @Column
    private int id;

    @Column
    @NotNull
    private String vorname;

    @Column
    @NotNull
    private String nachname;

    @Column
    @NotNull
    private String email;

    @Column
    @NotNull
    private String adresse;

    @Column
    @NotNull
    private String passwort;

    @Column
    @NotNull
    @Lob
    private String profilbild;


    public Nutzer(String vorname, String nachname, String email, String adresse, String passwort, String profilbild) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.email = email;
        this.adresse = adresse;
        this.passwort = passwort;
        this.profilbild = profilbild;
    }

    public Nutzer() {

    }

    public int getId() {
        return id;
    }

    public String getVorname() {
        return vorname;
    }

    public String getNachname() {
        return nachname;
    }

    public String getEmail() {
        return email;
    }

    public String getAdresse() {
        return adresse;
    }

    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    public String getPasswort() {
        return passwort;
    }

    public void setPasswort(String passwort) {
        this.passwort = passwort;
    }

    public String getProfilbild() {
        return profilbild;
    }

    public void setProfilbild(String profilbild) {
        this.profilbild = profilbild;
    }
}
