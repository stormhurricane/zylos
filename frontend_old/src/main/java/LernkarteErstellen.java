import datenklassen.Lernkarte;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class LernkarteErstellen extends OberController {
    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;
    private int LKanzahl;
    private int id;

    @FXML
    private TextField ThemaTF;

    @FXML
    private TextField Frage;

    @FXML
    private Label Fehler;

    @FXML
    private TextArea Antwort;

    @FXML
    private Label ThemaL;



    public void setPrimaryStage(Stage stage)
    {
        this.stage = stage;
        LKanzahl=0;
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

    public void hinzufuegen()
    {
        if(LKanzahl==0)
        {
            this.erstesErstellen();
        }
        else
        {
            this.nichtErstesErstellen();
        }
    }

    public void erstesErstellen()
    {
        Fehler.setVisible(false);
        if(ThemaTF.getText().length()>0 && Frage.getText().length()>0 && Antwort.getText().length()>0)
        {
            id = nService.lernkartenThemaErstellen(lvid, ThemaTF.getText());
            Lernkarte lk = new Lernkarte(id, Frage.getText(), Antwort.getText());
            nService.lernkarteErstellen(lk);
            LKanzahl++;
            ThemaTF.setVisible(false);
            ThemaL.setText(ThemaTF.getText());
            ThemaL.setVisible(true);
            Frage.clear();
            Antwort.clear();
        }
        else
        {
            Fehler.setVisible(true);
        }
    }

    public void nichtErstesErstellen()
    {
        Fehler.setVisible(false);
        if(Frage.getText().length()>0 && Antwort.getText().length()>0)
        {
            Lernkarte lk = new Lernkarte(id, Frage.getText(), Antwort.getText());
            nService.lernkarteErstellen(lk);
            Frage.clear();
            Antwort.clear();
        }
        else
        {
            Fehler.setVisible(true);
        }

    }

    public void anlegen()
    {
        Fehler.setVisible(false);
        if(Frage.getText().length()>0 && Antwort.getText().length()>0)
        {
            this.hinzufuegen();
            this.changeToLernkartenListe();
        }
        else
        {
            Fehler.setVisible(true);
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
