package com.zylos.backend.database;

import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "FREUNDE" )
@Deprecated(since = "2024-06", forRemoval = true)
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
