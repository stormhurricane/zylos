package datenklassen;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Student extends Nutzer{

    private int matrikelnr;


    private String studienfach;

    public Student(@JsonProperty("Vorname")String vorname,
                   @JsonProperty ("Nachname")String nachname,
                   @JsonProperty ("Email")String email,
                   @JsonProperty ("Adresse")String adresse,
                   @JsonProperty ("Passwort")String passwort,
                   @JsonProperty ("Profilbild") String profilbild,
                   @JsonProperty ("Studienfach")String studienfach) {
        super(vorname, nachname, email, adresse, passwort, profilbild);
        this.studienfach = studienfach;
    }

    public Student() {

    }

    public int getMatrikelnr() {
        return matrikelnr;
    }

    public String getStudienfach() {
        return studienfach;
    }

    public void setStudienfach(String studienfach) {
        this.studienfach = studienfach;
    }
}
