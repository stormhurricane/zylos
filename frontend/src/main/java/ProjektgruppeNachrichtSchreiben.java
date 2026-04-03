import Controller.Communication.ChatWrapper;
import datenklassen.ChatNachricht;
import datenklassen.ProjektgruppenNachricht;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.io.IOException;

public class ProjektgruppeNachrichtSchreiben extends OberController {
    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;

    @FXML
    private Label Ziel;

    @FXML
    private TextArea Inhalt;

    @FXML
    private Label fehler;

    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
    }

    public void changeToProjektgruppenUebersicht() {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("ProjektgruppenUebersicht.fxml"));
            root = (Parent) fxmlLoader.load();
            ProjektgruppenUebersicht PGU = (ProjektgruppenUebersicht) fxmlLoader.getController();
            PGU.setLvid(lvid);
            PGU.setRollenId(rollenId);
            PGU.setSessionId(sessionId);
            PGU.setNutzerService(nService);
            PGU.setStudentService(sService);
            PGU.setLehrenderService(lService);
            PGU.Titel();
            PGU.prufeRolle();
            PGU.setDate(date);
            PGU.setLoginAnzahl(loginAnzahl);
            PGU.setPrimaryStage(stage);
            scene.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void fillLabel()
    {
        Ziel.setText(nService.sucheLVPerID(this.getLvid()).getProjektgruppe().getTitel());
    }

    public void schreiben()
    {
        String sender= new String(nService.rufeNutzer(this.getSessionId()).getVorname()+ " "+ nService.rufeNutzer(this.getSessionId()).getNachname());
        String inhalt= Inhalt.getText();
        ProjektgruppenNachricht pn = new ProjektgruppenNachricht(this.getLvid(),sender, inhalt);
        nService.sendePGNachricht(pn);
        this.changeToChatraum();
    }

    public void changeToChatraum(){

        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("ProjektgruppenChat.fxml"));
            root = (Parent) fxmlLoader.load();
            ProjektgruppenChat PGC = (ProjektgruppenChat) fxmlLoader.getController();
            PGC.setLvid(lvid);
            PGC.setRollenId(rollenId);
            PGC.setSessionId(sessionId);
            PGC.setNutzerService(nService);
            PGC.setStudentService(sService);
            PGC.setLehrenderService(lService);
            PGC.setDate(date);
            PGC.setLoginAnzahl(loginAnzahl);
            PGC.setPrimaryStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
