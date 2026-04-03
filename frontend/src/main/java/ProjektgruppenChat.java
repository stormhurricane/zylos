import Controller.Communication.ChatWrapper;
import datenklassen.ChatNachricht;
import datenklassen.ProjektgruppenNachricht;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.List;

public class ProjektgruppenChat extends OberController{
    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;

    private ObservableList<String> nutzer;
    private List<ProjektgruppenNachricht> Liste;

    @FXML
    private ListView ListeNutzer;

    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
        this.NachrichtAnzeigen();
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


    public void NachrichtAnzeigen() {
        nutzer = FXCollections.observableArrayList();
        this.Liste= nService.zeigeGruppenChat(this.getLvid());
        if (Liste != null) {
            for (int i = 0; i < Liste.size(); i++) {

                nutzer.add(i, i + 1 + ". " + Liste.get(i).getSender() + ": " + Liste.get(i).getInhalt());
            }
            ListeNutzer.setItems(nutzer);
        }
    }

    public void changeToNachrichtSchreiben()
    {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("ProjektgruppeNachrichtSchreiben.fxml"));
            root = (Parent) fxmlLoader.load();
            ProjektgruppeNachrichtSchreiben PGNS = (ProjektgruppeNachrichtSchreiben) fxmlLoader.getController();
            PGNS.setLvid(lvid);
            PGNS.setRollenId(rollenId);
            PGNS.setSessionId(sessionId);
            PGNS.setNutzerService(nService);
            PGNS.setStudentService(sService);
            PGNS.setLehrenderService(lService);
            PGNS.fillLabel();
            PGNS.setDate(date);
            PGNS.setLoginAnzahl(loginAnzahl);
            PGNS.setPrimaryStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

