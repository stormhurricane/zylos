package gruppei.backend.database;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "Material")
public class LehrveranstaltungsMaterial {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @NotNull
    private Integer materialID;

    private String bezeichnung;

    private String dateiEndung;

    @Lob
    private String inhalt;

    @NotNull
    private int lehrveranstaltungsId;


    public LehrveranstaltungsMaterial() { }

    public LehrveranstaltungsMaterial(String bezeichnung, String dateiEndung,
                                      String inhalt, int lehrveranstaltungsId) {
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
