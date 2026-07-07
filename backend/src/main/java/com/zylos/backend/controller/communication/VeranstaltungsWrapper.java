package com.zylos.backend.controller.communication;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.zylos.backend.database.Lehrveranstaltung;
import com.zylos.backend.database.Projektgruppe;

@Deprecated(since = "2026-04", forRemoval = true)
public class VeranstaltungsWrapper {

    @JsonProperty("lehrveranstaltung")
    private Lehrveranstaltung moeglicheLV;
    @JsonProperty("projektgruppe")
    private Projektgruppe moeglicheProjektgruppe;

    public VeranstaltungsWrapper(Lehrveranstaltung lv) {
        if (lv instanceof Projektgruppe) {
            this.moeglicheProjektgruppe = (Projektgruppe) lv;
         //   this.projektgruppe = true;
        }
        else {
            this.moeglicheLV = lv;
         //   this.projektgruppe = false;
        }
    }

    public VeranstaltungsWrapper(){}

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

}
