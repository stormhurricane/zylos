import datenklassen.Lernkarte;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class LernkarteAnsehen extends OberController {
    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;
    private int anzahl;

    private ArrayList<Lernkarte> Liste;


    @FXML
    private Label Frage;

    @FXML
    private Label Antwort;


    public void setPrimaryStage(Stage stage)
    {
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

    public void antwortZeigen()
    {
        Antwort.setVisible(true);
    }

    public void fillLabel(int id)
    {
        Liste= nService.lernkartenListe(id);
        anzahl=0;
        this.lernkarteAnzeigen();
    }

    public void lernkarteAnzeigen()
    {
        Frage.setText(Liste.get(anzahl).getFrage());
        Antwort.setText(Liste.get(anzahl).getAntwort());
    }

    public void weiter()
    {
        if(anzahl<Liste.size()-1)
        {
            anzahl++;
            Antwort.setVisible(false);
            this.lernkarteAnzeigen();
        }
        else
        {
            this.changeToLernkartenListe();
        }
    }


    public void changeToLernkartenListe() {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("LernkartenListe.fxml"));
            root = (Parent) fxmlLoader.load();
            LernkarteListe LKL = (LernkarteListe) fxmlLoader.getController();
            LKL.setLvid(lvid);
            LKL.setRollenId(rollenId);
            LKL.setSessionId(sessionId);
            LKL.setNutzerService(nService);
            LKL.setStudentService(sService);
            LKL.setLehrenderService(lService);
            LKL.LernkartenAnzeigen();
            LKL.setDate(date);
            LKL.setLoginAnzahl(loginAnzahl);
            LKL.setPrimaryStage(stage);
            scene.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();

        }
    }
}
