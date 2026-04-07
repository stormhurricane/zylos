package com.zylos.backend.database;

import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;

@Entity
@Table(name = "TEILNEHMERLISTE")
public class Teilnehmer {

    @EmbeddedId
    @Column
    @NotNull
    private ListID id;


    public Teilnehmer() {
    }

    public Teilnehmer(ListID id) {
        this.id = id;
    }


    public ListID getId() {
        return id;
    }
}


