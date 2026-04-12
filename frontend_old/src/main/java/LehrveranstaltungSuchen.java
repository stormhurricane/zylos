import datenklassen.Lehrveranstaltung;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.ChoiceBox;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.*;

public class LehrveranstaltungSuchen extends OberController implements Initializable {
    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;
    private int typ;

    @FXML
    private TextField Titel;

    @FXML
    private TextField Jahr;

    @FXML
    private ChoiceBox Veranstaltung;

    @FXML
    private ChoiceBox Semester;

    @FXML
    private Text Fehler;


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
                        new String("SS"),
                        new String("WS"));

        Semester.setItems(choiceBoxSemester);

        ObservableList<String> choiceBoxVeranstaltung =
                FXCollections.observableArrayList(
                        new String("Vorlesung"),
                        new String("Seminar"),
                        new String("Projektgruppe"));

        Veranstaltung.setItems(choiceBoxVeranstaltung);
    }

    public void beitreten()
    {
        Fehler.setVisible(false);
        if (!Semester.getSelectionModel().isEmpty() && !Veranstaltung.getSelectionModel().isEmpty() && Titel.getText().length()!=0 && Jahr.getText().length()!=0)
        {
            Map<String, Integer> teilnahmeMap = new HashMap<>();
            if(this.suchen()){
            teilnahmeMap.put("nutzerID", sessionId);
            teilnahmeMap.put("lehrveranstaltungsID", this.getLvid());
            boolean erfolg = nService.treteLVBei(teilnahmeMap);
            if (erfolg == true)
            {
                if(typ==10) {
                    this.changeToLehrveranstaltungUebersicht();
                }
                else if(typ==-10)
                {
                    this.changeToProjektgruppenUebersicht();
                }
            }
            else
            {
                Fehler.setVisible(true);
            }
        }}
        else
        {
            Fehler.setVisible(true);
        }
    }

    public void anzeigen()
    {
        Fehler.setVisible(false);
        if (!Semester.getSelectionModel().isEmpty() && !Veranstaltung.getSelectionModel().isEmpty() && Titel.getText().length()!=0 && Jahr.getText().length()!=0)
        {
            if(this.suchen())
            {
                if(typ==10) {
                    this.changeToLehrveranstaltungUebersicht();
                }
                else if(typ==-10)
                {
                    this.changeToProjektgruppenUebersicht();
                }
            }
        }
    }

    public boolean suchen()
    {
        if (!Semester.getSelectionModel().isEmpty() && !Veranstaltung.getSelectionModel().isEmpty() && Titel.getText().length()!=0 && Jahr.getText().length()!=0)
        {
            Map<String, String> LVsuche = new HashMap<>();
            LVsuche.put("titel",Titel.getText());
            LVsuche.put("semesterJahr",Jahr.getText());
            LVsuche.put("semesterZeit",Semester.getValue().toString());
            if(!Veranstaltung.getValue().toString().equals("Projektgruppe"))
            {
                LVsuche.put("typ", Veranstaltung.getValue().toString());
            }

            if(nService.sucheLVPerMap(LVsuche)!=null)
            {
                if(nService.sucheLVPerMap(LVsuche).getLehrveranstaltung()!=null)
                {
                    this.lvid = nService.sucheLVPerMap(LVsuche).getLehrveranstaltung().getLehrveranstaltungsID();
                    typ=10;
                    return true;
                }
                else if(nService.sucheLVPerMap(LVsuche).getProjektgruppe()!=null)
                {
                    if (rollenId == 1)
                    {
                        if (nService.sucheLVPerMap(LVsuche).getProjektgruppe().isSichtbarkeit())
                        {
                            this.lvid = nService.sucheLVPerMap(LVsuche).getProjektgruppe().getLehrveranstaltungsID();
                            typ = -10;
                            return true;
                        }
                        else if (nService.pruefeTeilnahme(sessionId, nService.sucheLVPerMap(LVsuche).getProjektgruppe().getLehrveranstaltungsID()))
                        {
                            this.lvid = nService.sucheLVPerMap(LVsuche).getProjektgruppe().getLehrveranstaltungsID();
                            typ = -10;
                            return true;
                        }
                        else
                        {
                            Fehler.setVisible(true);
                            return false;
                        }
                    }
                else
                {
                        this.lvid = nService.sucheLVPerMap(LVsuche).getProjektgruppe().getLehrveranstaltungsID();
                        typ = -10;
                        return true;
                }
                }
            }
            else
            {
                Fehler.setVisible(true);
                return false;
            }
        }
        else
        {
            Fehler.setVisible(true);
            return false;
        }
        return false; //kann nicht passieren, aber IntelliJ
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
    }


