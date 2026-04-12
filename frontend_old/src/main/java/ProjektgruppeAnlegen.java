import Controller.Communication.VeranstaltungsWrapper;
import datenklassen.Lehrveranstaltung;
import datenklassen.Projektgruppe;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ProjektgruppeAnlegen extends OberController implements Initializable {
    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;


    @FXML
    private TextField Titel;

    @FXML
    private TextField Jahr;

    @FXML
    private ChoiceBox Semester;

    @FXML
    private Label FehlerManuell;

    @FXML
    private Label ErfolgManuell;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.fillChoiceBox();
    }

    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
    }

    private void fillChoiceBox() {
        ObservableList<String> choiceBoxSemester =
                FXCollections.observableArrayList(
                        new String("Sommersemester"),
                        new String("Wintersemester"));

        Semester.setItems(choiceBoxSemester);
    }

    public void anlegen() {
        FehlerManuell.setVisible(false);
        ErfolgManuell.setVisible(false);

        if (!Semester.getSelectionModel().isEmpty() && Titel.getText().length() != 0 && (Jahr.getText().length() == 4 || Jahr.getText().length() == 9)) {
            String titel = new String(Titel.getText());
            String jahr = new String(Jahr.getText());
            boolean sichtbarkeit = false;
            boolean erfolg = false;

            if (rollenId == -1) {
                sichtbarkeit = false;
            } else if (rollenId == 1) {
                sichtbarkeit = true;
            }

            if (Semester.getValue().equals("Sommersemester")) {
                Projektgruppe pg = new Projektgruppe(titel, jahr, Lehrveranstaltung.zeitEnum.SS, sichtbarkeit);
                VeranstaltungsWrapper hilfe = new VeranstaltungsWrapper(pg);
                erfolg = lService.legeLehrveranstaltungAn(sessionId, hilfe);
                if (erfolg) {
                    ErfolgManuell.setVisible(true);
                } else {
                    FehlerManuell.setVisible(true);
                }

            } else if (Semester.getValue().equals("Wintersemester")) {
                Projektgruppe pg = new Projektgruppe(titel, jahr, Lehrveranstaltung.zeitEnum.WS, sichtbarkeit);
                VeranstaltungsWrapper hilfe = new VeranstaltungsWrapper(pg);
                erfolg = lService.legeLehrveranstaltungAn(sessionId, hilfe);
                if (erfolg) {
                    ErfolgManuell.setVisible(true);
                } else {
                    FehlerManuell.setVisible(true);
                }
            }
        }
    }


        public void changeToStartseite()
        {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("Startseite.fxml"));
            root = (Parent) fxmlLoader.load();
            Startseite SS = (Startseite) fxmlLoader.getController();
            SS.setLvid(0);
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
}
