import Controller.Communication.VeranstaltungsWrapper;
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
import java.util.HashMap;
import java.util.Map;

public class MeineLehrveranstaltungen extends OberController {

    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;
    private ArrayList<VeranstaltungsWrapper> AlleLehrveranstaltungen;
    private ObservableList<String> Lehrveranstaltung;

    @FXML
    private Button Beitreten;

    @FXML
    private Button LehrveranstaltungAnzeigen;

    @FXML
    private ListView AlleLehrveranstaltungenAnzeigen;

    @FXML
    private Label FehlerAnzeigen;

    @FXML
    private Label FehlerBeitreten;


    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
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
            LVU.prufeRolle();
            LVU.Titel();
            LVU.setDate(date);
            LVU.setLoginAnzahl(loginAnzahl);
            LVU.setPrimaryStage(stage);
            scene.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();

        }
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
    

    public void LehrveranstaltungAnzeigen(){
        if (!AlleLehrveranstaltungenAnzeigen.getSelectionModel().isEmpty()) {
            if (this.LVIdHerausfinden()==10) {
                this.changeToLehrveranstaltungUebersicht();
            }
            else if(this.LVIdHerausfinden()==-10)
            {
                this.changeToProjektgruppenUebersicht();
            }
        }

    }

    public int LVIdHerausfinden()
    {
        String LVID= this.LVPositionHerausfinden();
        int Position = Integer.parseInt(LVID);
        Position= Position -1;
        if(AlleLehrveranstaltungen.get(Position).getLehrveranstaltung()!=null)
        {
            this.setLvid(AlleLehrveranstaltungen.get(Position).getLehrveranstaltung().getLehrveranstaltungsID());
            return 10;
        }
        else if(AlleLehrveranstaltungen.get(Position).getProjektgruppe()!=null)
        {
            this.setLvid(AlleLehrveranstaltungen.get(Position).getProjektgruppe().getLehrveranstaltungsID());
            return -10;
        }
        return 0 ;// sollte nie passieren
    }

    public String LVPositionHerausfinden() {
        String LVP = new String("");
        if (!AlleLehrveranstaltungenAnzeigen.getSelectionModel().isEmpty()) {
            String LVID = AlleLehrveranstaltungenAnzeigen.getSelectionModel().getSelectedItem().toString();
            for (int i = 0; i < LVID.length(); i++) {
                Character Hilfe = LVID.charAt(i);
                if (Hilfe.equals('.')) {
                    return LVP;
                }
                LVP = new String(LVP + Hilfe);
            }
            return LVP; // sollte nie passieren -> for schleife sollte nie zuende gehen
        }
        return LVP; // sollte nie passieren
    }

    public void AlleLehrveranstaltungenAnzeigen()
    {
        FehlerAnzeigen.setVisible(false);
        Lehrveranstaltung =  FXCollections.observableArrayList();

        AlleLehrveranstaltungen = nService.ladeMeineLVs(sessionId);
        if (AlleLehrveranstaltungen==null)
        {
            FehlerAnzeigen.setVisible(true);
        }
        else {
            for (int i = 0; i < AlleLehrveranstaltungen.size(); i++)
            {
                if(AlleLehrveranstaltungen.get(i).getLehrveranstaltung()!=null)
                {
                    Lehrveranstaltung.add(i, i + 1 + ". " + AlleLehrveranstaltungen.get(i).getLehrveranstaltung().getTitel() + " " + AlleLehrveranstaltungen.get(i).getLehrveranstaltung().getTyp() + " " + AlleLehrveranstaltungen.get(i).getLehrveranstaltung().getSemesterZeit() + " " + AlleLehrveranstaltungen.get(i).getLehrveranstaltung().getSemesterJahr());
                }
                else if(AlleLehrveranstaltungen.get(i).getProjektgruppe()!=null)
                {
                    Lehrveranstaltung.add(i, i + 1 + ". " + AlleLehrveranstaltungen.get(i).getProjektgruppe().getTitel() + " Projektgruppe " + AlleLehrveranstaltungen.get(i).getProjektgruppe().getSemesterZeit() + " " + AlleLehrveranstaltungen.get(i).getProjektgruppe().getSemesterJahr());
                }
            }
            AlleLehrveranstaltungenAnzeigen.setItems(Lehrveranstaltung);
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

