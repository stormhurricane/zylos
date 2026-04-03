package gruppei.backend.database;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class FreundesListeID implements Serializable {

    private int nutzerId1;
    private int nutzerId2;

    public FreundesListeID() {
    }

    public FreundesListeID(int nutzerId1, int nutzerId2) {
        this.nutzerId1 = nutzerId1;
        this.nutzerId2 = nutzerId2;
    }

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
