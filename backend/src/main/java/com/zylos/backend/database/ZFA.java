package com.zylos.backend.database;

import jakarta.validation.constraints.NotNull;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "ZFACODE")
@Deprecated(since = "2024-06", forRemoval = true)
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
