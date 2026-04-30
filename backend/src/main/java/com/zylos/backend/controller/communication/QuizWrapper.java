package com.zylos.backend.controller.communication;

import java.util.List;

import com.zylos.backend.database.Frage;
@Deprecated(since = "2026-04", forRemoval = true)
public class QuizWrapper {

    private int lvId;

    private String name;

    private List<Frage> fragen;

    public QuizWrapper(int lvId, String name, List<Frage> fragen) {
            this.lvId = lvId;
            this.name = name;
            this.fragen = fragen;
    }

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

        public List<Frage> getFragen() {
            return fragen;
        }

        public void setFragen(List<Frage> fragen) {
            this.fragen = fragen;
        }
}


