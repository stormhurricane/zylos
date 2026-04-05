package com.zylos.backend.database;

import com.sun.istack.NotNull;

import javax.persistence.*;

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


