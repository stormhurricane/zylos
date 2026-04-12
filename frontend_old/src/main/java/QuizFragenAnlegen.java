import Controller.Communication.QuizWrapper;
import datenklassen.Frage;
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

public class QuizFragenAnlegen extends OberController implements Initializable {
    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;
    private String name;

    private List<Frage> fragen;


    @FXML
    private TextField frage;

    @FXML
    private TextField antwortA;

    @FXML
    private TextField antwortB;

    @FXML
    private TextField antwortC;

    @FXML
    private TextField antwortD;

    @FXML
    private ChoiceBox richtig;

    @FXML
    private Label fehler;

    @FXML
    private Label Name;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.fillChoiceBox();
    }

    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
        fragen= new ArrayList<>();
    }

    public void setName(String name) {
        this.name = name;
        Name.setText(name);
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

    public void frageErstellen()
    {
        fehler.setVisible(false);
        if(frage.getText().length()>0 && antwortA.getText().length()>0 && antwortB.getText().length()>0 && antwortC.getText().length()>0 && antwortD.getText().length()>0 && !richtig.getSelectionModel().isEmpty())
        {
            Character hilfe = richtig.getValue().toString().charAt(0);
            Frage frage1 = new Frage(frage.getText(), antwortA.getText(), antwortB.getText(), antwortC.getText(), antwortD.getText(), hilfe);
            fragen.add(frage1);
            frage.clear();
            antwortA.clear();
            antwortB.clear();
            antwortC.clear();
            antwortD.clear();
        }
        else
        {
            fehler.setVisible(true);
        }
    }
    public void speichern()
    {
        fehler.setVisible(false);
        if(frage.getText().length()>0 && antwortA.getText().length()>0 && antwortB.getText().length()>0 && antwortC.getText().length()>0 && antwortD.getText().length()>0 && !richtig.getSelectionModel().isEmpty())
        {
            Character hilfe = richtig.getValue().toString().charAt(0);
            Frage frage1 = new Frage(frage.getText(), antwortA.getText(), antwortB.getText(), antwortC.getText(), antwortD.getText(), hilfe);
            fragen.add(frage1);
            QuizWrapper qw = new QuizWrapper(lvid, name, fragen);
            lService.legeQuizAn(qw);
            this.changeToLehrveranstaltungUebersicht();
        }
        else
        {
            fehler.setVisible(true);
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
