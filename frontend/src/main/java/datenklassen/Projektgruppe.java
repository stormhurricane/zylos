package datenklassen;

import com.google.gson.annotations.SerializedName;

public class Projektgruppe extends Lehrveranstaltung{

    @SerializedName("sichtbarkeit")
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
