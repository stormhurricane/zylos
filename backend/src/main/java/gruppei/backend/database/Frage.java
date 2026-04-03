package gruppei.backend.database;


import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "FRAGE")
public class Frage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @NotNull
    @Column
    private int id;

    private int testId;

    private String frage;

    private String antwortA;

    private String antwortB;

    private String antwortC;

    private String antwortD;

    private char loesung;

    public Frage(String frage, String antwortA, String antwortB, String antwortC, String antwortD, char loesung) {
        this.frage = frage;
        this.antwortA = antwortA;
        this.antwortB = antwortB;
        this.antwortC = antwortC;
        this.antwortD = antwortD;
        this.loesung = loesung;
    }

    public Frage() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) { this.id = id;}

    public int getTestId() {
        return testId;
    }

    public void setTestId(int testId) {
        this.testId = testId;
    }

    public String getFrage() {
        return frage;
    }

    public void setFrage(String frage) {
        this.frage = frage;
    }

    public char getLoesung() {
        return loesung;
    }

    public void setLoesung(char loesung) {
        this.loesung = loesung;
    }

    public String getAntwortA() {
        return antwortA;
    }

    public void setAntwortA(String antwortA) {
        this.antwortA = antwortA;
    }

    public String getAntwortB() {
        return antwortB;
    }

    public void setAntwortB(String antwortB) {
        this.antwortB = antwortB;
    }

    public String getAntwortC() {
        return antwortC;
    }

    public void setAntwortC(String antwortC) {
        this.antwortC = antwortC;
    }

    public String getAntwortD() {
        return antwortD;
    }

    public void setAntwortD(String antwortD) {
        this.antwortD = antwortD;
    }
}
