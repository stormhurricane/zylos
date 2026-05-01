package com.zylos.backend.database;

import jakarta.validation.constraints.NotNull;
import jakarta.persistence.*;

@Entity
@Table(name = "CHAT")
@Deprecated(since = "2024-06", forRemoval = true)
public class Chat {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @NotNull
    @Column
    private int id;

    private int nutzerId1;

    private int nutzerId2;

    public Chat(int nutzerId1, int nutzerId2) {
        this.nutzerId1 = nutzerId1;
        this.nutzerId2 = nutzerId2;
    }

    public Chat() {

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {this.id = id;}

    public int getNutzerId1() {
        return nutzerId1;
    }

    public void setNutzerId1(int nutzerId1) {
        this.nutzerId1 = nutzerId1;
    }

    public int getNutzerId2() {
        return nutzerId2;
    }

    public void setNutzerId2(int nutzerId2) {
        this.nutzerId2 = nutzerId2;
    }
}
