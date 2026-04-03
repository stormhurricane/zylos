import java.io.IOException;
import java.util.ArrayList;;
import Controller.Communication.NutzerWrapper;
import datenklassen.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;



public class Freundesliste  extends OberController{
    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;

    private ObservableList<String> nutzer;
    private ArrayList<NutzerWrapper> Liste;

    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private ListView ListeNutzer;


    public void changeToStartseite()
    {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("Startseite.fxml"));
            root = (Parent) fxmlLoader.load();
            Startseite SS = (Startseite) fxmlLoader.getController();
            SS.setRollenId(rollenId);
            SS.setSessionId(sessionId);
            SS.setNutzerService(nService);
            SS.setStudentService(sService);
            SS.setLehrenderService(lService);
            SS.reminderPruefen();
            SS.pruefeRolle();
            SS.setDate(date);
            SS.setLoginAnzahl(loginAnzahl);
            SS.setPrimaryStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void freundeAnzeigen()
    {
        nutzer =  FXCollections.observableArrayList();
        this.Liste= nService.meineFreunde(this.getSessionId());
        if (Liste!=null)
        {
            for (int i = 0; i < Liste.size(); i++) {
                if (Liste.get(i).getMoeglicherStudent() != null) {
                    nutzer.add(i, i + 1 + ". " + Liste.get(i).getMoeglicherStudent().getVorname() + " " + Liste.get(i).getMoeglicherStudent().getNachname() + ", " + Liste.get(i).getMoeglicherStudent().getMatrikelnr());
                }
                else if ( Liste.get(i).getMoeglicherLehrender() != null){
                    nutzer.add(i, i + 1 + ". " + Liste.get(i).getMoeglicherLehrender().getVorname() + " " + Liste.get(i).getMoeglicherLehrender().getNachname());
                }
            }
            ListeNutzer.setItems(nutzer);
        }
    }
    public void profilAnzeigen()
    {
        if (!ListeNutzer.getSelectionModel().isEmpty())
        {
            String StudentID = this.FreundPositionHerausfinden();
            int Position = Integer.parseInt(StudentID);
            Position = Position - 1;
            if (Liste.get(Position).getMoeglicherLehrender() != null) {
                this.changeToProfilLehrender();
            } else if (Liste.get(Position).getMoeglicherStudent() != null) {
                this.changeToProfilStudent();
            }
        }
    }

    public int FreundIdHerausfinden(){
        String StudentID= this.FreundPositionHerausfinden();
        int Position = Integer.parseInt(StudentID);
        Position= Position -1;
        if (Liste.get(Position).getMoeglicherStudent() != null){
            return Liste.get(Position).getMoeglicherStudent().getNutzerId();

        }
        else if (Liste.get(Position).getMoeglicherLehrender() != null){
            return Liste.get(Position).getMoeglicherLehrender().getNutzerId();
        }
        return -2; // sollte nie passieren
    }

    public String FreundPositionHerausfinden() {
        String SP = new String("");
        if (!ListeNutzer.getSelectionModel().isEmpty()) {
            String SID = ListeNutzer.getSelectionModel().getSelectedItem().toString();
            for (int i = 0; i < SID.length(); i++) {
                Character Hilfe = SID.charAt(i);
                if (Hilfe.equals('.')) {
                    return SP;
                }
                SP = new String(SP + Hilfe);
            }
            return SP; // sollte nie passieren -> for schleife sollte nie zuende gehen
        }
        return SP; // sollte nie passieren
    }

    public void changeToProfilStudent() {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("ProfilStudent.fxml"));
            root = (Parent) fxmlLoader.load();
            ProfilStudent PS = (ProfilStudent) fxmlLoader.getController();
            PS.setLvid(lvid);
            PS.setRollenId(rollenId);
            PS.setSessionId(sessionId);
            PS.setNutzerService(nService);
            PS.setStudentService(sService);
            PS.setLehrenderService(lService);
            PS.fillLabel(this.FreundIdHerausfinden());
            PS.prufeRolle(this.FreundIdHerausfinden());
            PS.MeineLehrveranstaltungen(this.FreundIdHerausfinden());
            PS.setDate(date);
            PS.setLoginAnzahl(loginAnzahl);
            PS.setPrimaryStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeToProfilLehrender() {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("ProfilLehrender.fxml"));
            root = (Parent) fxmlLoader.load();
            ProfilLehrender PL = (ProfilLehrender) fxmlLoader.getController();
            PL.setLvid(lvid);
            PL.setRollenId(rollenId);
            PL.setSessionId(sessionId);
            PL.setNutzerService(nService);
            PL.setStudentService(sService);
            PL.setLehrenderService(lService);
            PL.fillLabel(this.FreundIdHerausfinden());
            PL.prufeRolle(this.FreundIdHerausfinden());
            PL.MeineLehrveranstaltungen(this.FreundIdHerausfinden());
            PL.setDate(date);
            PL.setLoginAnzahl(loginAnzahl);
            PL.setPrimaryStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

