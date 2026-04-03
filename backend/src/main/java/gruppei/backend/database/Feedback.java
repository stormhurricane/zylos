package gruppei.backend.database;


import com.sun.istack.NotNull;

import javax.persistence.*;

@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Entity
@Table(name = "FEEDBACK")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @NotNull
    @Column
    private int id;

    private int versuchId;

    private int frageId;

    private boolean abgegebeneAntwort;

    public Feedback(int versuchId, int frageId, boolean abgegebeneAntwort) {
        this.versuchId = versuchId;
        this.frageId = frageId;
        this.abgegebeneAntwort = abgegebeneAntwort;
    }

    public Feedback() {
    }

    public int getId() {
        return id;
    }

    public int getVersuchId() {
        return versuchId;
    }

    public void setVersuchId(int versuchId) {
        this.versuchId = versuchId;
    }

    public int getFrageId() {
        return frageId;
    }

    public void setFrageId(int frageId) {
        this.frageId = frageId;
    }

    public boolean isAbgegebeneAntwort() {
        return abgegebeneAntwort;
    }

    public void setAbgegebeneAntwort(boolean abgegebeneAntwort) {
        this.abgegebeneAntwort = abgegebeneAntwort;
    }
}
