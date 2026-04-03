package datenklassen;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.gson.annotations.SerializedName;


import java.util.ArrayList;
import java.util.List;


public class Lehrveranstaltung {

    @SerializedName("lehrveranstaltungsID")
    private int lehrveranstaltungsID;

    @SerializedName("titel")
    private String titel;
    @SerializedName("semesterZeit")
    private zeitEnum semesterZeit;
    @SerializedName("semesterJahr")
    private String semesterJahr;
    @SerializedName("typ")
    private typEnum typ;


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


    public Lehrveranstaltung() {
    }

    @JsonCreator
    public Lehrveranstaltung(@JsonProperty("titel") String titel,
                             @JsonProperty("semesterJahr") String semesterJahr,
                             @JsonProperty("typ") typEnum typ,
                             @JsonProperty("semesterZeit") zeitEnum semesterZeit) {

        this.titel = titel;
        this.semesterJahr = semesterJahr;
        this.typ = typ;
        this.semesterZeit = semesterZeit;

    }

    public enum typEnum { VORLESUNG, SEMINAR }

    public enum zeitEnum { WS, SS }
}
