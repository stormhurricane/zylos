import datenklassen.Test;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class QuizListe extends OberController{
    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;

    private ObservableList<String> quiz;
    private ArrayList<Test> Liste;

    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private ListView ListeQuiz;

    @FXML
    private Button quizSpielen;

    @FXML
    private Button quizStatistik;

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


    public void changeToQuizStatistik()
    {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("QuizStatistik.fxml"));
            root = (Parent) fxmlLoader.load();
            QuizStatistik QS = (QuizStatistik) fxmlLoader.getController();
            QS.setLvid(lvid);
            QS.setRollenId(rollenId);
            QS.setSessionId(sessionId);
            QS.setNutzerService(nService);
            QS.setStudentService(sService);
            QS.setLehrenderService(lService);
            QS.erzeugeStatistik(this.QuizHerausfinden().getId());
            QS.filllabel();
            QS.setDate(date);
            QS.setLoginAnzahl(loginAnzahl);
            QS.setPrimaryStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void QuizAnzeigen() {
        quiz = FXCollections.observableArrayList();
        this.Liste = nService.zeigeAlleQuizEinerLV(lvid);
        if (Liste != null) {
            for (int i = 0; i < Liste.size(); i++) {

                quiz.add(i, i + 1 + ". " + Liste.get(i).getName());
            }
            ListeQuiz.setItems(quiz);
        }
    }

    public Test QuizHerausfinden() {
        String StudentID = this.QuizPositionHerausfinden();
        int Position = Integer.parseInt(StudentID);
        Position = Position - 1;
        return Liste.get(Position);
    }

    public String QuizPositionHerausfinden() {
        String SP = new String("");
        if (!ListeQuiz.getSelectionModel().isEmpty()) {
            String SID = ListeQuiz.getSelectionModel().getSelectedItem().toString();
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

    public void changeToQuizSpielen() {
        if (!ListeQuiz.getSelectionModel().isEmpty())
        {
            try {
                scene = stage.getScene();
                fxmlLoader = new FXMLLoader(getClass().getResource("QuizSpielen.fxml"));
                root = (Parent) fxmlLoader.load();
                QuizSpielen QS = (QuizSpielen) fxmlLoader.getController();
                QS.setLvid(lvid);
                QS.setRollenId(rollenId);
                QS.setSessionId(sessionId);
                QS.setNutzerService(nService);
                QS.setStudentService(sService);
                QS.setLehrenderService(lService);
                QS.setTest(this.QuizHerausfinden());

                Map<String, Integer> versuch = new HashMap<String, Integer>();
                versuch.put("nutzerId", sessionId);
                versuch.put("testId", this.QuizHerausfinden().getId());

                QS.setVersuchId(sService.legeVersuchAn(versuch));
                QS.setIndex(0);
                QS.frageAnzeigen();
                QS.setDate(date);
                QS.setLoginAnzahl(loginAnzahl);
                QS.setPrimaryStage(stage);
                scene.setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    public void pruefeRolle() {

        if (rollenId == 1) {
            quizStatistik.setVisible(false);
            quizSpielen.setVisible(true);
        } else { // Lehrender hat rollenId = -1
            quizStatistik.setVisible(true);
            quizSpielen.setVisible(false);
        }
    }
}
