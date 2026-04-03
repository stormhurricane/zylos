package datenklassen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Nutzer {

    private int id;

    private String vorname;

    private String nachname;

    private String email;

    private String adresse;

    private String passwort;

    private String profilbild;

    // private List<Lehrveranstaltung> beigetreteneKurse;
    //SZ: Auskommentiert, da Lehrveranstaltung nicht für Registrierung relevant

    @JsonCreator
    public Nutzer(@JsonProperty("Vorname") String vorname,
                  @JsonProperty("Nachname") String nachname,
                  @JsonProperty("Email") String email,
                  @JsonProperty("Adresse") String adresse,
                  @JsonProperty("Passwort") String passwort,
                  @JsonProperty("Profilbild") String profilbild){
        this.vorname = vorname;
        this.nachname = nachname;
        this.email = email;
        this.adresse = adresse;
        this.passwort = passwort;
        this.profilbild = profilbild;

    }


    /*public Nutzer(String vorname, String nachname, String email, String adresse, String passwort) {
        this.vorname = vorname;
        this.nachname = nachname;
        this.email = email;
        this.adresse = adresse;
        this.passwort = passwort;
    }
*/
    public Nutzer() {

    }

    /*public int getId() {
        return nutzer_id;
    }*/

    public int getNutzerId(){return this.id;}

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

    public String getProfilbild() {
        return profilbild;
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
}
