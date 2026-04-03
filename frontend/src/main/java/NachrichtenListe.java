import Controller.Communication.ChatWrapper;
import Controller.Communication.NutzerWrapper;
import datenklassen.ChatNachricht;
import datenklassen.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class NachrichtenListe extends OberController{
    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;
    private int Id1;
    private int Id2;

    private ObservableList<String> nutzer;
    private List<ChatNachricht> Liste;

    @FXML
    private ListView ListeNutzer;

    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
    }

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


    public void NachrichtAnzeigen() {
        nutzer = FXCollections.observableArrayList();
        ChatWrapper hilfe = nService.ladeNachrichten(Id1, Id2);
        this.Liste= hilfe.getChatNachrichten();
        if (Liste != null) {
            for (int i = 0; i < Liste.size(); i++) {

                nutzer.add(i, i + 1 + ". " + Liste.get(i).getSender() + ": " + Liste.get(i).getInhalt());
            }
            ListeNutzer.setItems(nutzer);
        }
    }
    

    public void setId1(int id1) {
        Id1 = id1;
    }

    public void setId2(int id2) {
        Id2 = id2;
    }

    public void changeToNachrichtSchreiben()
    {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("NachrichtSchreiben.fxml"));
            root = (Parent) fxmlLoader.load();
            NachrichtSchreiben NS = (NachrichtSchreiben) fxmlLoader.getController();
            NS.setLvid(lvid);
            NS.setRollenId(rollenId);
            NS.setSessionId(sessionId);
            if(Id1==sessionId) {
                NS.setEmpfaengerId(Id2);
            }
            if(Id2==sessionId) {
                NS.setEmpfaengerId(Id1);
            }
            NS.setNutzerService(nService);
            NS.setStudentService(sService);
            NS.setLehrenderService(lService);
            NS.fillLabel();
            NS.setDate(date);
            NS.setLoginAnzahl(loginAnzahl);
            NS.setPrimaryStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
