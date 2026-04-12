import Controller.Communication.StatistikWrapper;
import datenklassen.Frage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;

public class QuizStatistik extends OberController{

  Stage stage;
  private Scene scene;
  private FXMLLoader fxmlLoader;
  private Parent root;

  @FXML
  private Label titel;

  @FXML
  private Label gesamtbeteiligung;

  @FXML
  private Label bestehensquote;

  @FXML
  private ListView korrekteAntwortenProFrage;

  @FXML
  private ListView versuchStudent;

  private StatistikWrapper statistik;

    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
    }

  public void filllabel() {
      String tquote = new String("" + statistik.getTeilnahmequote());
      String bquote = new String("" + statistik.getBestehensquote());
      gesamtbeteiligung.setText(tquote);
      bestehensquote.setText(bquote);


      if (statistik.getTeilnahmequote() > 0) {
          ObservableList<String> kquote = FXCollections.observableArrayList();
          Map<Integer, Integer> korrekt = statistik.getAnzahlKorrekterAntwortenEinerFrage();
          for (Map.Entry<Integer, Integer> hilfe : korrekt.entrySet()) {
              Integer frage = hilfe.getKey();
              Integer qoute = hilfe.getValue();

              kquote.add(sService.findeFragePerId(frage) + ": Korrekte Antworten: " + qoute);
          }

          ObservableList<String> versuch = FXCollections.observableArrayList();
          Map<Integer, Integer> vpt = statistik.getVersucheProTeilnehmer();
          for (Map.Entry<Integer, Integer> help : vpt.entrySet()) {
              Integer studentID = help.getKey();
              Integer anzahl = help.getValue();

              versuch.add(nService.rufeNutzer(studentID).getVorname() + " " + nService.rufeNutzer(studentID).getNachname() + ": Anzahl an Versuchen: " + anzahl);
          }
          korrekteAntwortenProFrage.setItems(kquote);
          versuchStudent.setItems(versuch);
      }
  }

  public void erzeugeStatistik(int testid){
      statistik =lService.zeigeStatistik(testid);

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
