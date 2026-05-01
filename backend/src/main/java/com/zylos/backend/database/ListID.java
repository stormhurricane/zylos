package com.zylos.backend.database;

import jakarta.persistence.Embeddable;
import java.io.Serializable;


@Embeddable
@Deprecated(since = "2024-06", forRemoval = true)
public class ListID implements Serializable{

    private int nutzer_id;
    private int lehrveranstaltungsID;

    public ListID() {

    }

    public ListID(int nutzer_id, int lehrveranstaltungsID) {
        this.nutzer_id = nutzer_id;
        this.lehrveranstaltungsID = lehrveranstaltungsID;
    }

    public int getNutzer_id() {
        return nutzer_id;
    }

    public void setNutzer_id(int nutzer_id) {
        this.nutzer_id = nutzer_id;
    }

    public int getLehrveranstaltungsID() {
        return lehrveranstaltungsID;
    }

    public void setLehrveranstaltungsID(int lehrveranstaltungsID) {
        this.lehrveranstaltungsID = lehrveranstaltungsID;
    }

}
