import Controller.Communication.NutzerWrapper;
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
import java.util.ArrayList;
import java.util.Map;

public class BewertungsStatistik extends OberController{

    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;
    private ObservableList<String> nutzer;
    private ArrayList<Integer[]> Liste;

    @FXML
    private ListView AntowrtenProFrage;

    @FXML
    private Button alleTeilnehmer;

    @FXML
    private Button bestanden;

    @FXML
    private Button durchgefallen;



    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
    }

    public void alleTeilnehmer()
    {
            nutzer =  FXCollections.observableArrayList();
            ArrayList <Frage> Test = nService.ladeBewertung(lvid);
            this.Liste= nService.erstelleBewertungsStatistik(Test.get(0).getTestId(),0);
            if (Liste!=null)
            {
                int k=0;
                for (int i = 0; i < Liste.size(); i++)
                {
                    nutzer.add(k, i + 1 + ". Frage:"+Test.get(i).getFrage() +" Antwort A: " +Liste.get(i)[1]);
                    k++;
                    nutzer.add(k, i + 1 + ". Frage:"+Test.get(i).getFrage() +" Antwort B: " +Liste.get(i)[2]);
                    k++;
                    nutzer.add(k, i + 1 + ". Frage:"+Test.get(i).getFrage() +" Antwort C: " +Liste.get(i)[3]);
                    k++;
                    nutzer.add(k, i + 1 + ". Frage:"+Test.get(i).getFrage() +" Antwort D: " +Liste.get(i)[4]);
                    k++;
                }
                AntowrtenProFrage.setItems(nutzer);
    }
            alleTeilnehmer.setVisible(false);
            bestanden.setVisible(true);
            durchgefallen.setVisible(true);
    }



    public void bestanden()
    {
        nutzer =  FXCollections.observableArrayList();
        ArrayList <Frage> Test = nService.ladeBewertung(lvid);
        this.Liste= nService.erstelleBewertungsStatistik(Test.get(0).getTestId(),1);
        int k=0;
        for (int i = 0; i < Liste.size(); i++)
        {
            nutzer.add(k, i + 1 + ". Frage:"+Test.get(i).getFrage() +" Antwort A: " +Liste.get(i)[1]);
            k++;
            nutzer.add(k, i + 1 + ". Frage:"+Test.get(i).getFrage() +" Antwort B: " +Liste.get(i)[2]);
            k++;
            nutzer.add(k, i + 1 + ". Frage:"+Test.get(i).getFrage() +" Antwort C: " +Liste.get(i)[3]);
            k++;
            nutzer.add(k, i + 1 + ". Frage:"+Test.get(i).getFrage() +" Antwort D: " +Liste.get(i)[4]);
            k++;
            AntowrtenProFrage.setItems(nutzer);
        }

        alleTeilnehmer.setVisible(true);
        bestanden.setVisible(false);
        durchgefallen.setVisible(true);
    }

    public void durchgefallen()
    {
        nutzer =  FXCollections.observableArrayList();
        ArrayList <Frage> Test = nService.ladeBewertung(lvid);
        this.Liste= nService.erstelleBewertungsStatistik(Test.get(0).getTestId(),-1);
        int k=0;
        for (int i = 0; i < Liste.size(); i++)
        {
            nutzer.add(k, i + 1 + ". Frage:"+Test.get(i).getFrage() +" Antwort A: " +Liste.get(i)[1]);
            k++;
            nutzer.add(k, i + 1 + ". Frage:"+Test.get(i).getFrage() +" Antwort B: " +Liste.get(i)[2]);
            k++;
            nutzer.add(k, i + 1 + ". Frage:"+Test.get(i).getFrage() +" Antwort C: " +Liste.get(i)[3]);
            k++;
            nutzer.add(k, i + 1 + ". Frage:"+Test.get(i).getFrage() +" Antwort D: " +Liste.get(i)[4]);
            k++;
            AntowrtenProFrage.setItems(nutzer);
        }
        alleTeilnehmer.setVisible(true);
        bestanden.setVisible(true);
        durchgefallen.setVisible(false);
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
