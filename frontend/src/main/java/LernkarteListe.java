import datenklassen.LernkartenThema;
import datenklassen.Student;
import datenklassen.Test;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class LernkarteListe extends OberController{
    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;


    private ObservableList<String> lernkarte;
    private ArrayList<LernkartenThema> Liste;

    @FXML
    private ListView ListeLK;

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

    public void LernkartenAnzeigen()
    {
        lernkarte =  FXCollections.observableArrayList();
        this.Liste= nService.lernkartenThemaListe(lvid);
        if (Liste!=null)
        {
            for (int i = 0; i < Liste.size(); i++)
            {
               lernkarte.add(i, i + 1 + ". " + Liste.get(i).getBeschreibung());
            }
            ListeLK.setItems(lernkarte);
        }
    }

    public int LernkarteIdHerausfinden(){
        String StudentID= this.LernkartePositionHerausfinden();
        int Position = Integer.parseInt(StudentID);
        Position= Position -1;
        return Liste.get(Position).getId();

    }

    public String LernkartePositionHerausfinden() {
        String SP = new String("");
        if (!ListeLK.getSelectionModel().isEmpty()) {
            String SID = ListeLK.getSelectionModel().getSelectedItem().toString();
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

    public void changeToLernkarteAnsehen() {

        if (!ListeLK.getSelectionModel().isEmpty()) {
            try {
                scene = stage.getScene();
                fxmlLoader = new FXMLLoader(getClass().getResource("LernkarteAnsehen.fxml"));
                root = (Parent) fxmlLoader.load();
                LernkarteAnsehen LKA = (LernkarteAnsehen) fxmlLoader.getController();
                LKA.setLvid(lvid);
                LKA.setRollenId(rollenId);
                LKA.setSessionId(sessionId);
                LKA.setNutzerService(nService);
                LKA.setStudentService(sService);
                LKA.setLehrenderService(lService);
                LKA.setDate(date);
                LKA.fillLabel(this.LernkarteIdHerausfinden());
                LKA.setLoginAnzahl(loginAnzahl);
                LKA.setPrimaryStage(stage);
                scene.setRoot(root);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void changeToLernkarteErstellen() {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("LernkarteErstellen.fxml"));
            root = (Parent) fxmlLoader.load();
            LernkarteErstellen LKE = (LernkarteErstellen) fxmlLoader.getController();
            LKE.setLvid(lvid);
            LKE.setRollenId(rollenId);
            LKE.setSessionId(sessionId);
            LKE.setNutzerService(nService);
            LKE.setStudentService(sService);
            LKE.setLehrenderService(lService);
            LKE.setDate(date);
            LKE.setLoginAnzahl(loginAnzahl);
            LKE.setPrimaryStage(stage);
            scene.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
