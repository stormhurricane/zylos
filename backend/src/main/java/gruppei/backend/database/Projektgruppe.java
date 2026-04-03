package gruppei.backend.database;


import com.fasterxml.jackson.annotation.JsonProperty;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "PROJEKTGRUPPE")
public class Projektgruppe extends Lehrveranstaltung {

    @JsonProperty("sichtbarkeit")
    private boolean sichtbarkeit;

    public boolean isSichtbarkeit() {
        return sichtbarkeit;
    }

    public void setSichtbarkeit(boolean sichtbarkeit) {
        this.sichtbarkeit = sichtbarkeit;
    }

    public Projektgruppe() {

    }

    public Projektgruppe(String titel, String semesterJahr, zeitEnum semesterZeit, boolean sichtbarkeit) {
        super(titel, semesterJahr, null, semesterZeit);
        this.sichtbarkeit = sichtbarkeit;
    }


}
