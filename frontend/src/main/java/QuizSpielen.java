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

public class QuizSpielen extends OberController implements Initializable {
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

    @FXML
    private Label Name;

    private Test test;

    private ArrayList<Frage> fragen;

    private int index;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.fillChoiceBox(); }

    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
        Name.setText(test.getName());
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
        fragen= sService.zeigeFragenEinesTests(test.getId());

        frage.setText(fragen.get(index).getFrage());
        antwortA.setText(fragen.get(index).getAntwortA());
        antwortB.setText(fragen.get(index).getAntwortB());
        antwortC.setText(fragen.get(index).getAntwortC());
        antwortD.setText(fragen.get(index).getAntwortD());
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public void antwortUeberpruefen()
    {
        Character hilfe = richtig.getValue().toString().charAt(0);
        boolean abgegebeneAntwort=false;
        if(hilfe.equals(fragen.get(index).getLoesung()))
        {
            abgegebeneAntwort=true;
        }
        Feedback fb = new Feedback(versuchId, fragen.get(index).getId(), abgegebeneAntwort);
        sService.legeFeedbackAn(fb);
        index++;
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
        boolean abgegebeneAntwort=false;
        if(hilfe.equals(fragen.get(index).getLoesung()))
        {
            abgegebeneAntwort=true;
        }
        Feedback fb = new Feedback(versuchId, fragen.get(index).getId(), abgegebeneAntwort);
        sService.legeFeedbackAn(fb);
        this.changeToQuizFeedback();
    }


    public void setTest(Test test)
    {
        this.test = test;
    }

    public void setVersuchId(int versuchId) {
        this.versuchId = versuchId;
    }

    public void changeToQuizFeedback()
    {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("QuizFeedback.fxml"));
            root = (Parent) fxmlLoader.load();
            QuizFeedback QF = (QuizFeedback) fxmlLoader.getController();
            QF.setLvid(lvid);
            QF.setRollenId(rollenId);
            QF.setSessionId(sessionId);
            QF.setNutzerService(nService);
            QF.setStudentService(sService);
            QF.setLehrenderService(lService);
            QF.setVersuchsId(versuchId);
            QF.setFragen(fragen);
            QF.setTitel(Name.getText());
            QF.QuizAnzeigen();
            QF.setDate(date);
            QF.setLoginAnzahl(loginAnzahl);
            QF.setPrimaryStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

