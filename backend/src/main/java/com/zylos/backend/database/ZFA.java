package com.zylos.backend.database;

import com.sun.istack.NotNull;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "ZFACODE")
public class ZFA {

    @Id
    @NotNull
    private int id;

    private int code;

    public ZFA() {
    }

    public ZFA(int id, int code) {
        this.id = id;
        this.code = code;
    }

    public int getId() {
        return id;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
