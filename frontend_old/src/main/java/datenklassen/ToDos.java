package datenklassen;

public class ToDos {

    private int id;

    private int projektgruppenId;

    private String inhalt;

    private int verantwortlichenId;

    private boolean erledigt;

    public ToDos(int projektgruppenId, String inhalt, int verantwortlichenId, boolean erledigt) {
        this.projektgruppenId = projektgruppenId;
        this.inhalt = inhalt;
        this.verantwortlichenId = verantwortlichenId;
        this.erledigt = erledigt;
    }

    public ToDos() {

    }

    public int getId() {
        return id;
    }

    public int getProjektgruppenId() {
        return projektgruppenId;
    }

    public void setProjektgruppenId(int projektgruppenId) {
        this.projektgruppenId = projektgruppenId;
    }

    public String getInhalt() {
        return inhalt;
    }

    public void setInhalt(String inhalt) {
        this.inhalt = inhalt;
    }

    public int getVerantwortlichenId() {
        return verantwortlichenId;
    }

    public void setVerantwortlichenId(int verantwortlichenId) {
        this.verantwortlichenId = verantwortlichenId;
    }

    public boolean isErledigt() {
        return erledigt;
    }

    public void setErledigt(boolean erledigt) {
        this.erledigt = erledigt;
    }
}
