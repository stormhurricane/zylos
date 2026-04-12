import datenklassen.Feedback;
import datenklassen.Frage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.io.IOException;
import java.util.ArrayList;

public class QuizFeedback extends OberController {


    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;
    private String titel;

    @FXML
    private Label feedback;

    @FXML
    private Label Titel;

    @FXML
    ListView fragenFeedback;

    private ObservableList<String> quiz;
    private ArrayList<Feedback> Liste;
    private int versuchsId;
    private ArrayList<Frage> fragen;

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


    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
    }
    public void QuizAnzeigen() {
        quiz = FXCollections.observableArrayList();
        this.Liste = sService.zeigeFeedbackEinesVersuchs(versuchsId);
        if (Liste != null) {
            for (int i = 0; i < Liste.size(); i++)
            {
                if(Liste.get(i).isAbgegebeneAntwort()==true)
                {
                    quiz.add(i, i + 1 + ".Frage: " + fragen.get(i).getFrage() + " richtig beantwortet");
                }
                else
                {
                    quiz.add(i, i + 1 + ".Frage: " + fragen.get(i).getFrage() + " falsch beantwortet");
                }
            }
            fragenFeedback.setItems(quiz);
        }
        if(sService.pruefeVersuch(versuchsId))
        {
            feedback.setText("Bestanden");
        }
        else
        {
            feedback.setText("Durchgefallen");
        }
        Titel.setText(titel);
    }

    public void setVersuchsId(int versuchsId) {
        this.versuchsId = versuchsId;
    }

    public void setFragen(ArrayList<Frage> fragen) {
        this.fragen = fragen;
    }

    public void setTitel(String titel) {
        this.titel = titel;
    }
}

