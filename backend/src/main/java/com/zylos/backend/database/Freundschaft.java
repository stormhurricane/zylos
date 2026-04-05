package com.zylos.backend.database;

import com.sun.istack.NotNull;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "FREUNDE" )
public class Freundschaft {

    @EmbeddedId
    @Column
    @NotNull
    private FreundesListeID freundesListeID;

    private boolean akzeptiert = false;

    public Freundschaft() {
    }

    public Freundschaft(FreundesListeID freundesListeID) {
        this.freundesListeID = freundesListeID;
    }

    public FreundesListeID getFreundesListID() {
        return freundesListeID;
    }

    public boolean isAkzeptiert() {
        return akzeptiert;
    }

    public void setAkzeptiert(boolean akzeptiert) {
        this.akzeptiert = akzeptiert;
    }
}
