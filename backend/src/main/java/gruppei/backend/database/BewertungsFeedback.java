package gruppei.backend.database;


import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "BEWERTUNGSFEEDBACK")
public class BewertungsFeedback extends Feedback {

    private char antwort;

    public BewertungsFeedback(int versuchId, int frageId, boolean abgegebeneAntwort, char antwort) {
        super(versuchId, frageId, abgegebeneAntwort);
        this.antwort = antwort;
    }

    public BewertungsFeedback() {
    }

    public char getAntwort() {
        return antwort;
    }

    public void setAntwort(char antwort) {
        this.antwort = antwort;
    }
}
