import datenklassen.BewertungsFeedback;
import datenklassen.Feedback;
import datenklassen.Frage;
import datenklassen.Test;
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
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BewertungSpielen extends OberController implements Initializable {
    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;
    private int versuchId;

    @FXML
    private Label frage;

    @FXML
    private Label antwortA;

    @FXML
    private Label antwortB;

    @FXML
    private Label antwortC;

    @FXML
    private Label antwortD;

    @FXML
    private ChoiceBox richtig;


    private ArrayList<Frage> fragen;

    private int index;

    private List<BewertungsFeedback> feedback;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.fillChoiceBox(); }

    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
        index=0;
        fragen= nService.ladeBewertung(lvid);
        feedback=new ArrayList<BewertungsFeedback>();
    }

    public int versuchPruefen()
    {
        return this.versuchId=nService.erstelleBewertungsversuch(sessionId,lvid);
    }

    private void fillChoiceBox() {
        ObservableList<String> choiceBoxSemester =
                FXCollections.observableArrayList(
                        new String("A"),
                        new String("B"),
                        new String("C"),
                        new String("D"));

        richtig.setItems(choiceBoxSemester);
    }

    public void frageAnzeigen (){
        frage.setText(fragen.get(index).getFrage());
        antwortA.setText(fragen.get(index).getAntwortA());
        antwortB.setText(fragen.get(index).getAntwortB());
        antwortC.setText(fragen.get(index).getAntwortC());
        antwortD.setText(fragen.get(index).getAntwortD());
    }


    public void antwortUeberpruefen()
    {
        Character hilfe = richtig.getValue().toString().charAt(0);
        BewertungsFeedback fb = new BewertungsFeedback(versuchId, fragen.get(index).getId(), true, hilfe);
        index++;
        feedback.add(fb);
        this.frageAnzeigen();
    }

    public void indexUeberpruefen()
    {
        if(index<fragen.size()-1)
        {
            this.antwortUeberpruefen();
        }
        else
        {
            this.letzteAntwortUeberpruefen();
        }
    }

    public void letzteAntwortUeberpruefen()
    {
        Character hilfe = richtig.getValue().toString().charAt(0);
        BewertungsFeedback fb = new BewertungsFeedback(versuchId, fragen.get(index).getId(), true, hilfe);
        index++;
        feedback.add(fb);
        if(nService.erstelleFeedbackFuerVersuch(feedback))
        {
            this.changeToLehrveranstaltungUebersicht();
        }
    }


    public void changeToLehrveranstaltungUebersicht() {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("LVUebersicht.fxml"));
            root = (Parent) fxmlLoader.load();
            LehrveranstaltungUebersicht LVU = (LehrveranstaltungUebersicht) fxmlLoader.getController();
            LVU.setLvid(lvid);
            LVU.setRollenId(rollenId);
            LVU.setSessionId(sessionId);
            LVU.setNutzerService(nService);
            LVU.setStudentService(sService);
            LVU.setLehrenderService(lService);
            LVU.Titel();
            LVU.prufeRolle();
            LVU.setDate(date);
            LVU.setLoginAnzahl(loginAnzahl);
            LVU.setPrimaryStage(stage);
            scene.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

}