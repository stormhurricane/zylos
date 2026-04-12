import Controller.Communication.VeranstaltungsWrapper;
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

public class LehrveranstaltungAnlegen extends OberController implements Initializable {
    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;

    @FXML
    private TextField Titel;

    @FXML
    private TextField Jahr;

    @FXML
    private ChoiceBox Veranstaltung;

    @FXML
    private ChoiceBox Semester;

    @FXML
    private Label FehlerManuell;

    @FXML
    private Label FehlerCSV;

    @FXML
    private Label ErfolgManuell;

    @FXML
    private Label ErfolgCSV;

    @FXML
    private Button Anlegen;

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
                        new String("Sommersemester"),
                        new String("Wintersemester"));

        Semester.setItems(choiceBoxSemester);

        ObservableList<String> choiceBoxVeranstaltung =
                FXCollections.observableArrayList(
                        new String("Vorlesung"),
                        new String("Seminar"));

        Veranstaltung.setItems(choiceBoxVeranstaltung);
    }

    public void manuellAnlegen()
    {
        FehlerManuell.setVisible(false);
        FehlerCSV.setVisible(false);
        ErfolgManuell.setVisible(false);
        ErfolgCSV.setVisible(false);

        if (!Semester.getSelectionModel().isEmpty() && !Veranstaltung.getSelectionModel().isEmpty() && Titel.getText().length()!=0 && (Jahr.getText().length()==4||Jahr.getText().length()==9))
        {
            String titel = new String(Titel.getText());
            String jahr = new String(Jahr.getText());
            boolean erfolg = false;


            if(Semester.getValue().equals("Sommersemester"))
            {
                if(Veranstaltung.getValue().equals("Vorlesung"))
                {
                    Lehrveranstaltung ug = new Lehrveranstaltung(titel, jahr, Lehrveranstaltung.typEnum.VORLESUNG, Lehrveranstaltung.zeitEnum.SS);
                    erfolg = lService.legeLehrveranstaltungAn(this.getSessionId(), new VeranstaltungsWrapper(ug));
                    if(erfolg==false)
                    {
                        this.manuellenFehlerZeigen();
                    }
                    else
                    {
                        Map<String, String> LVsuche = new HashMap<>();
                        LVsuche.put("titel",titel);
                        LVsuche.put("semesterJahr",jahr);
                        LVsuche.put("semesterZeit","SS");
                        LVsuche.put("typ","Vorlesung");

                        ErfolgManuell.setVisible(true);
                        this.beitreten(nService.sucheLVPerMap(LVsuche).getLehrveranstaltung().getLehrveranstaltungsID());
                    }
                }
                else if (Veranstaltung.getValue().equals("Seminar"))
                {
                    Lehrveranstaltung ug = new Lehrveranstaltung(titel, jahr, Lehrveranstaltung.typEnum.SEMINAR, Lehrveranstaltung.zeitEnum.SS);
                    erfolg = lService.legeLehrveranstaltungAn(this.getSessionId(), new VeranstaltungsWrapper(ug));
                    if(erfolg==false)
                    {
                        this.manuellenFehlerZeigen();
                    }
                    else
                    {
                        Map<String, String> LVsuche = new HashMap<>();
                        LVsuche.put("titel",titel);
                        LVsuche.put("semesterJahr",jahr);
                        LVsuche.put("semesterZeit","SS");
                        LVsuche.put("typ","Seminar");

                        ErfolgManuell.setVisible(true);
                        this.beitreten(nService.sucheLVPerMap(LVsuche).getLehrveranstaltung().getLehrveranstaltungsID());
                    }
                }
            }
            else  if(Semester.getValue().equals("Wintersemester"))
            {
                if(Veranstaltung.getValue().equals("Vorlesung"))
                {
                    Lehrveranstaltung ug = new Lehrveranstaltung(titel, jahr, Lehrveranstaltung.typEnum.VORLESUNG, Lehrveranstaltung.zeitEnum.WS);
                    erfolg = lService.legeLehrveranstaltungAn(this.getSessionId(), new VeranstaltungsWrapper(ug));
                    if(erfolg==false)
                    {
                        this.manuellenFehlerZeigen();
                    }
                    else
                    {
                        Map<String, String> LVsuche = new HashMap<>();
                        LVsuche.put("titel",titel);
                        LVsuche.put("semesterJahr",jahr);
                        LVsuche.put("semesterZeit","WS");
                        LVsuche.put("typ","Vorlesung");

                        ErfolgManuell.setVisible(true);
                        this.beitreten(nService.sucheLVPerMap(LVsuche).getLehrveranstaltung().getLehrveranstaltungsID());
                    }
                }
                else if (Veranstaltung.getValue().equals("Seminar"))
                {
                    Lehrveranstaltung ug = new Lehrveranstaltung(titel, jahr, Lehrveranstaltung.typEnum.SEMINAR, Lehrveranstaltung.zeitEnum.WS);
                    erfolg = lService.legeLehrveranstaltungAn(this.getSessionId(), new VeranstaltungsWrapper(ug));
                    if(erfolg==false)
                    {
                        this.manuellenFehlerZeigen();
                    }
                    else
                    {
                        Map<String, String> LVsuche = new HashMap<>();
                        LVsuche.put("titel",titel);
                        LVsuche.put("semesterJahr",jahr);
                        LVsuche.put("semesterZeit","WS");
                        LVsuche.put("typ","Seminar");

                        ErfolgManuell.setVisible(true);
                        this.beitreten(nService.sucheLVPerMap(LVsuche).getLehrveranstaltung().getLehrveranstaltungsID());
                    }
                }
            }
        }
        else {
            this.manuellenFehlerZeigen();
        }
    }

    public void manuellenFehlerZeigen()
    {
        FehlerManuell.setVisible(true);
    }

    public void csvAnlegen()
    {
        FehlerManuell.setVisible(false);
        FehlerCSV.setVisible(false);
        ErfolgManuell.setVisible(false);
        ErfolgCSV.setVisible(false);

        final FileChooser loadTXT = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("CSV Datei (*.csv)", "*.csv");
        loadTXT.getExtensionFilters().add(extFilter);
        loadTXT.setInitialDirectory(new File(System.getProperty("user.dir")));
        File csv = loadTXT.showOpenDialog(Anlegen.getScene().getWindow());
        // https://git.uni-due.de/sktrkley/self-study-exercises-for-programming/-/blob/master/04.3/src/main/java/de/paluno/exercises/javafx/Controller.java
        // Siehe Methode: loadButtonPressed()
        // Stand: 23.05.2021 / 16 Uhr

        List<Boolean> Hilfe = this.erstelleLVVonCSV(csv);

        for(int i=1; i<Hilfe.size(); i++)
        {
            if(Hilfe.get(i)==false)
            {
                this.csvFehlerZeigen();
                return;
            }
        }
        ErfolgCSV.setVisible(true);
    }

    private List<Boolean> erstelleLVVonCSV(File file) {
        try {
            BufferedReader br = new BufferedReader((new FileReader(file)));
            String line = "";
            List<Boolean> replies = new LinkedList<Boolean>();
            while((line = br.readLine()) != null) {
                if (line.contains(";")) {
                    String[] neueLV = line.split(";");
                    replies.add(lService.legeLVAn(this.getSessionId(), neueLV));

                }
                else if (line.contains(";")) {
                    String[] neueLV = line.split((","));
                    replies.add(lService.legeLVAn(this.getSessionId(), neueLV));
                }
            }
            return replies;
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void csvFehlerZeigen()
    {
        FehlerCSV.setVisible(true);
    }


    public void beitreten(int LVid) {
         {
            Map<String, Integer> teilnahmeMap = new HashMap<>();
            teilnahmeMap.put("nutzerID", sessionId);
            teilnahmeMap.put("lehrveranstaltungsID", LVid);
            nService.treteLVBei(teilnahmeMap);
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
