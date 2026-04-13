package datenklassen;

public class Literatur {

    private String art;

    private String titel;

    private String autor;

    private String jahr;

    public Literatur() {
    }

    public Literatur(String art, String titel, String autor, String jahr) {
        this.art = art;
        this.titel = titel;
        this.autor = autor;
        this.jahr = jahr;
    }


    public String getArt() {
        return art;
    }

    public void setArt(String art) {
        this.art = art;
    }

    public String getTitel() {
        return titel;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
    }

    public String getJahr() {
        return jahr;
    }

    public void setJahr(String jahr) {
        this.jahr = jahr;
    }
}
