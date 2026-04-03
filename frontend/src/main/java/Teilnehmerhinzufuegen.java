import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;;
import Controller.Communication.NutzerWrapper;
import Controller.Communication.VeranstaltungsWrapper;
import datenklassen.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;


public class Teilnehmerhinzufuegen extends OberController implements Initializable {


    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;

    @FXML
    private TextField Benutzername;

    @FXML
    private TextField Nachname;

    @FXML
    private ChoiceBox choiceBox;

    @FXML
    private ListView ListeNutzer;

    @FXML
    private Label Hinzufuegen;

    private ObservableList<String> nutzer;
    private ArrayList<Student> Liste;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.fillChoiceBox();
    }

    private void fillChoiceBox() {
        ObservableList<String> choiceBoxData =
                FXCollections.observableArrayList(
                        new String("Matrikelnummer"),
                        new String("Name"));

        choiceBox.setItems(choiceBoxData);
    }

    public void studentSuchen()
    {
        if(choiceBox.getSelectionModel().isEmpty())
        {
            Hinzufuegen.setText("Die Suchart fehlt.");
        }
        else if(!choiceBox.getSelectionModel().isEmpty())
        {
            if(choiceBox.getValue().equals("Matrikelnummer"))
            {
                if(Benutzername.getText().length()==0)
                {
                    Hinzufuegen.setText("Bitte fuelle alle Pflichtfelder aus");
                }
                else{
                Map<String, String> zufuegeMap = new HashMap<>();
                zufuegeMap.put("matrikelnummer", Benutzername.getText());

                ArrayList<Integer> Hilfe = lService.sucheStudent(zufuegeMap);
                ArrayList<Student> liste = new ArrayList<>();
                Student student = (Student) nService.rufeNutzer(Hilfe.get(0));
                liste.add(0,student);
                this.studentAnzeigen(liste);
            }
            }
            if(choiceBox.getValue().equals("Name")) {
                if (Benutzername.getText().length() == 0 || Nachname.getText().length() == 0)
                {
                    Hinzufuegen.setText("Bitte fuelle alle Pflichtfelder aus");
                } else {
                    Map<String, String> zufuegeMap = new HashMap<>();
                    zufuegeMap.put("vorname", Benutzername.getText());
                    zufuegeMap.put("nachname", Nachname.getText());

                    ArrayList<Integer> Hilfe = lService.sucheStudent(zufuegeMap);
                    ArrayList<Student> liste = new ArrayList<>();
                    for (int i = 0; i < Hilfe.size(); i++) {
                        Student student = (Student) nService.rufeNutzer(Hilfe.get(i));
                        liste.add(i, student);
                    }
                    this.studentAnzeigen(liste);
                }
            }
        }
    }

    public void studentAnzeigen(ArrayList<Student> liste)
    {
        nutzer =  FXCollections.observableArrayList();
        this.Liste=liste;
        if (Liste!=null)
        {
            for (int i = 0; i < Liste.size(); i++)
            {
                nutzer.add(i, i + 1 + ". " + Liste.get(i).getVorname() + " " + Liste.get(i).getNachname()+ ", "+ Liste.get(i).getMatrikelnr());
            }
            ListeNutzer.setItems(nutzer);
        }
    }

    public int StudentIdHerausfinden(){
        String StudentID= this.StudentPositionHerausfinden();
        int Position = Integer.parseInt(StudentID);
        Position= Position -1;
        return Liste.get(Position).getNutzerId();
    }

    public String StudentPositionHerausfinden() {
        String SP = new String("");
        if (!ListeNutzer.getSelectionModel().isEmpty()) {
            String SID = ListeNutzer.getSelectionModel().getSelectedItem().toString();
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

    public void studentHinzufuegen()
    {
        if (!ListeNutzer.getSelectionModel().isEmpty())
        {
            Map<String, Integer> zufuegeMap = new HashMap<>();
            zufuegeMap.put("lehrveranstaltungsID", this.getLvid());
            zufuegeMap.put("studentenID", this.StudentIdHerausfinden());
            boolean erfolg = lService.fuegeStudentZu(zufuegeMap);
            if(erfolg==true)
            {
                Hinzufuegen.setText("Erfolreich hinzugefuegt");
            }
            else
            {
                Hinzufuegen.setText("Der Student konnte nicht hinzugefuegt werden");
            }

        }
    }

    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
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

    public void startseiteLV()
    {
        VeranstaltungsWrapper vw = nService.sucheLVPerID(lvid);
        if(vw.getLehrveranstaltung()!=null)
        {
            this.changeToLehrveranstaltungUebersicht();
        }
        else if(vw.getProjektgruppe()!=null)
        {
            this.changeToProjektgruppenUebersicht();
        }
    }
}
