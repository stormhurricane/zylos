import datenklassen.Lehrveranstaltung;
import datenklassen.Student;
import datenklassen.Termin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import javafx.scene.control.Label;

public class Kalendereinsehen extends OberController {

    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;
    private ArrayList<Termin> geladeneTermine;
    private ObservableList<String> TerminListe;


    @FXML
    private ListView TermineAnzeigen;

    @FXML
    private Button TermineEinsehen;

    @FXML
    private DatePicker DatumAuswahl;

    @FXML
    private Label fehlerDatum;

    @FXML
    private Label fehlerTermin;


    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
    }

    public void changeToStartseite() {
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

    public void TermineAnzeigen() {
        fehlerDatum.setVisible(false);
        fehlerTermin.setVisible(false);

        if (DatumAuswahl.getValue() != null) {
            LocalDate aktuellesDatum = DatumAuswahl.getValue();
            geladeneTermine = nService.ladeTermine(this.getSessionId(), aktuellesDatum);
            TerminListe = FXCollections.observableArrayList();
            if (!geladeneTermine.isEmpty()) {
                for (int i = 0; i < geladeneTermine.size(); i++) {
                    if (geladeneTermine.get(i) != null)
                    {
                        TerminListe.add(i,  geladeneTermine.get(i).getTag() + "." + geladeneTermine.get(i).getMonat() + "." + geladeneTermine.get(i).getJahr() + " - " + geladeneTermine.get(i).getUhrzeit() + " " + geladeneTermine.get(i).getBetreff());
                    }
                }
                TermineAnzeigen.setItems(TerminListe);
            }
            else
            {
                fehlerTermin.setVisible(true);
            }
        } else {
            fehlerDatum.setVisible(true);
        }
    }
}
