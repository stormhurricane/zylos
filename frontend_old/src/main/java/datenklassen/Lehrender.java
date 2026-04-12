package datenklassen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Lehrender extends Nutzer{

    private String forschungsgebiet;

    private String lehrstuhl;
    @JsonCreator
    public Lehrender(@JsonProperty ("Vorname")String vorname,
                     @JsonProperty ("Nachname")String nachname,
                     @JsonProperty ("Email")String email,
                     @JsonProperty ("Adresse")String adresse,
                     @JsonProperty ("Passwort")String passwort,
                     @JsonProperty ("Profilbild") String profilbild,
                     @JsonProperty ("Forschungsgebiet")String forschungsgebiet,
                     @JsonProperty ("Lehrstuhl")String lehrstuhl){
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
