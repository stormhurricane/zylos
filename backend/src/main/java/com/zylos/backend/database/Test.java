package com.zylos.backend.database;


import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "TEST")
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @NotNull
    @Column
    private int id;

    private int lvId;

    private String name;

    private testArtEnum testArt;

    public Test(int lvId, String name) {
        this.lvId = lvId;
        this.name = name;
    }

    public Test() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id;}

    public int getLvId() {
        return lvId;
    }

    public void setLvId(int lvId) {
        this.lvId = lvId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Test.testArtEnum getTestArt() {
        return testArt;
    }

    public void setTestArt(Test.testArtEnum testArt) {
        this.testArt = testArt;
    }

    public enum testArtEnum { QUIZ, BEWERTUNG}
}

