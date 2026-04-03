package Controller.Communication;

import datenklassen.Frage;

import java.util.List;

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
