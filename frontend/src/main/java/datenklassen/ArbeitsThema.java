package datenklassen;

public class ArbeitsThema {

    private int id;

    private int lehrendenId;

    private String titel;

    private String beschreibung;

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
