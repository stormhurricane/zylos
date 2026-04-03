package gruppei.backend.database;

import com.sun.istack.NotNull;

import javax.persistence.*;

@Entity
@Table(name = "PROJEKTGRUPPENNACHRICHT")
public class ProjektgruppenNachricht {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @NotNull
    @Column
    private long id;

    private int projektgruppenId;

    private String sender;

    private String inhalt;

    public ProjektgruppenNachricht(int projektgruppenId, String sender, String inhalt) {
        this.projektgruppenId = projektgruppenId;
        this.sender = sender;
        this.inhalt = inhalt;
    }

    public ProjektgruppenNachricht() {

    }

    public long getId() {
        return id;
    }

    public int getProjektgruppenId() {
        return projektgruppenId;
    }

    public void setProjektgruppenId(int projektgruppenId) {
        this.projektgruppenId = projektgruppenId;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getInhalt() {
        return inhalt;
    }

    public void setInhalt(String inhalt) {
        this.inhalt = inhalt;
    }
}
