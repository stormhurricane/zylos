package datenklassen;

public class LehrveranstaltungsMaterial {

    private Integer materialID;

    private String bezeichnung;

    private String dateiEndung;

    private String inhalt;

    private int lehrveranstaltungsId;

    public LehrveranstaltungsMaterial() { }

    public LehrveranstaltungsMaterial(String bezeichnung, String dateiEndung, String inhalt, int lehrveranstaltungsId) {
        this.bezeichnung = bezeichnung;
        this.dateiEndung = dateiEndung;
        this.inhalt = inhalt;
        this.lehrveranstaltungsId = lehrveranstaltungsId;
    }

    public Integer getMaterialID() {
        return materialID;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public String getDateiEndung() {
        return dateiEndung;
    }

    public void setDateiEndung(String dateiEndung) {
        this.dateiEndung = dateiEndung;
    }

    public String getInhalt() {
        return inhalt;
    }

    public void setInhalt(String inhalt) {
        this.inhalt = inhalt;
    }

    public int getLehrveranstaltungsId() {
        return lehrveranstaltungsId;
    }

    public void setLehrveranstaltungsId(int lehrveranstaltungsId) {
        this.lehrveranstaltungsId = lehrveranstaltungsId;
    }
}
