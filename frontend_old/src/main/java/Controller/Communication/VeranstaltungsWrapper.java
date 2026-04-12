package Controller.Communication;

import com.google.gson.annotations.SerializedName;
import datenklassen.Lehrveranstaltung;
import datenklassen.Projektgruppe;

public class VeranstaltungsWrapper {

    @SerializedName("lehrveranstaltung")
    private Lehrveranstaltung moeglicheLV;
    @SerializedName("projektgruppe")
    private Projektgruppe moeglicheProjektgruppe;

  //  private boolean projektgruppe;

    public VeranstaltungsWrapper(Lehrveranstaltung lv) {
        if (lv instanceof Projektgruppe) {
            this.moeglicheProjektgruppe = (Projektgruppe) lv;
         //   this.projektgruppe = true;
        }
        else {
            this.moeglicheLV = lv;
          //  this.projektgruppe = false;
        }
    }

    public VeranstaltungsWrapper(){}

  //  public boolean isProjektgruppe() {
 //       return this.projektgruppe;
  //  }

    public Lehrveranstaltung getLehrveranstaltung() {
        return this.moeglicheLV;
    }

    public Projektgruppe getProjektgruppe() {
        return this.moeglicheProjektgruppe;
    }

    public void setMoeglicheLV(Lehrveranstaltung moeglicheLV) {
        this.moeglicheLV = moeglicheLV;
    }

    public void setMoeglicheProjektgruppe(Projektgruppe moeglicheProjektgruppe) {
        this.moeglicheProjektgruppe = moeglicheProjektgruppe;
    }

   // public void setProjektgruppe(boolean projektgruppe) {
 //       this.projektgruppe = projektgruppe;
  //  }
}
