package com.zylos.backend.database;

import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;

@Entity
@Table(name = "TEILNEHMERLISTE")
@Deprecated(since = "2024-06", forRemoval = true)
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


