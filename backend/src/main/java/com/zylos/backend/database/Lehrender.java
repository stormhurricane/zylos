package com.zylos.backend.database;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
public class Lehrender extends Nutzer {

    @Column
    private String forschungsgebiet;

    @Column
    private String lehrstuhl;

    public Lehrender(String vorname, String nachname, String email, String adresse,
                     String passwort,String profilbild, String forschungsgebiet, String lehrstuhl) {
        super(vorname, nachname, email, adresse, passwort, profilbild);
        this.forschungsgebiet = forschungsgebiet;
        this.lehrstuhl = lehrstuhl;
    }

    public Lehrender() {
    }


    public String getForschungsgebiet() {
        if(this.forschungsgebiet != null) {
            return forschungsgebiet;
        } else {
            return "Kein Forschungsgebiet";
        }
    }


    public void setForschungsgebiet(String forschungsgebiet) {
        this.forschungsgebiet = forschungsgebiet;
    }

    public String getLehrstuhl() {
        if(this.lehrstuhl != null) {
            return lehrstuhl;
        } else {
            return "Kein Lehrstuhl";
        }
    }

    public void setLehrstuhl(String lehrstuhl) {
        this.lehrstuhl = lehrstuhl;
    }
}
