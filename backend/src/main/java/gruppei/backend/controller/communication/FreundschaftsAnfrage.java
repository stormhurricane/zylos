package gruppei.backend.controller.communication;

public class FreundschaftsAnfrage {


    private int[] zuBearbeitendeAnfrage;
    private boolean wirdAngenommen;

    public FreundschaftsAnfrage(int[] zuBearbeitendeAnfrage, boolean wirdAngenommen) {
        this.zuBearbeitendeAnfrage = zuBearbeitendeAnfrage;
        this.wirdAngenommen = wirdAngenommen;
    }

    public int[] getZuBearbeitendeAnfrage() {
        return zuBearbeitendeAnfrage;
    }

    public void setZuBearbeitendeAnfrage(int[] zuBearbeitendeAnfrage) {
        this.zuBearbeitendeAnfrage = zuBearbeitendeAnfrage;
    }

    public boolean isWirdAngenommen() {
        return wirdAngenommen;
    }

    public void setWirdAngenommen(boolean wirdAngenommen) {
        this.wirdAngenommen = wirdAngenommen;
    }
}
