import java.io.File;
import java.io.IOException;;
import Services.LehrenderService;
import datenklassen.LehrveranstaltungsMaterial;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class LehrveranstaltungUebersicht extends OberController {

    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;
    private String Material;

    @FXML
    private Button Materialanzeigen;

    @FXML
    private Button Materialhinzufuegen;

    @FXML
    private Button Teilnehmerliste;

    @FXML
    private Button Teilnehmerhinzufuegen;

    @FXML
    private Label Hinzufuegen;

    @FXML
    private Label Titel;

    @FXML
    private Button Quiz;

    @FXML
    private Button QuizErstellen;

    @FXML
    private Button BewertungErstellen;

    @FXML
    private Button BewertungStatistik;

    @FXML
    private Button BewertungTest;

    @FXML
    private Button Kalender;

    @FXML
    private Button ReminderErstellen;

    @FXML
    private Label fehler;


    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
    }

    public void changeToBewertungTest()
    {
        {
            try {
                scene = stage.getScene();
                fxmlLoader = new FXMLLoader(getClass().getResource("BewertungSpielen.fxml"));
                root = (Parent) fxmlLoader.load();
                BewertungSpielen BWT = (BewertungSpielen) fxmlLoader.getController();
                BWT.setLvid(lvid);
                BWT.setRollenId(rollenId);
                BWT.setSessionId(sessionId);
                BWT.setNutzerService(nService);
                BWT.setStudentService(sService);
                BWT.setLehrenderService(lService);
                BWT.setDate(date);
                BWT.setLoginAnzahl(loginAnzahl);
                BWT.setPrimaryStage(stage);
                if(BWT.versuchPruefen()!=-1)
                {
                    BWT.frageAnzeigen();
                    scene.setRoot(root);
                }
                else
                {
                    fehler.setVisible(true);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }

    public void changeToBewertungErstellen()
        {
            try {
                scene = stage.getScene();
                fxmlLoader = new FXMLLoader(getClass().getResource("BewertungFragenAnlegen.fxml"));
                root = (Parent) fxmlLoader.load();
                BewertungFragenAnlegen BWE = (BewertungFragenAnlegen) fxmlLoader.getController();
                BWE.setLvid(lvid);
                BWE.setRollenId(rollenId);
                BWE.setSessionId(sessionId);
                BWE.setNutzerService(nService);
                BWE.setStudentService(sService);
                BWE.setLehrenderService(lService);
                BWE.setDate(date);
                BWE.setLoginAnzahl(loginAnzahl);
                BWE.setPrimaryStage(stage);
                BWE.setName(nService.sucheLVPerID(this.getLvid()).getLehrveranstaltung().getTitel());
                scene.setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

        public void changeToBewertungStatistik()
            {
                try {
                    scene = stage.getScene();
                    fxmlLoader = new FXMLLoader(getClass().getResource("BewertungsStatistik.fxml"));
                    root = (Parent) fxmlLoader.load();
                    BewertungsStatistik BWS = (BewertungsStatistik) fxmlLoader.getController();
                    BWS.setLvid(lvid);
                    BWS.setRollenId(rollenId);
                    BWS.setSessionId(sessionId);
                    BWS.setNutzerService(nService);
                    BWS.setStudentService(sService);
                    BWS.setLehrenderService(lService);
                    BWS.alleTeilnehmer();
                    BWS.setDate(date);
                    BWS.setLoginAnzahl(loginAnzahl);
                    BWS.setPrimaryStage(stage);
                    scene.setRoot(root);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        public void changeToMaterial()
        {
            try {
                scene = stage.getScene();
                fxmlLoader = new FXMLLoader(getClass().getResource("MaterialLVanzeigen.fxml"));
                root = (Parent) fxmlLoader.load();
                MaterialLVanzeigen EZ = (MaterialLVanzeigen) fxmlLoader.getController();
                EZ.setLvid(lvid);
                EZ.setRollenId(rollenId);
                EZ.setSessionId(sessionId);
                EZ.setNutzerService(nService);
                EZ.setStudentService(sService);
                EZ.setLehrenderService(lService);
                EZ.AlleMaterialienAnzeigen();
                EZ.setDate(date);
                EZ.setLoginAnzahl(loginAnzahl);
                EZ.setPrimaryStage(stage);
                scene.setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    public void materialHochladen() throws IOException
    {
        fehler.setVisible(false);
        final FileChooser loadTXT = new FileChooser();
        loadTXT.setInitialDirectory(new File(System.getProperty("user.dir")));
        File MaterialFile = loadTXT.showOpenDialog(Materialhinzufuegen.getScene().getWindow());
        // https://git.uni-due.de/sktrkley/self-study-exercises-for-programming/-/blob/master/04.3/src/main/java/de/paluno/exercises/javafx/Controller.java
        // Siehe Methode: loadButtonPressed()
        // Stand: 23.05.2021 / 16 Uhr

        String devidedFileName[] = MaterialFile.getName().split("\\.");
        String bezeichnungFile = "";
        String dateiEndungFile = devidedFileName[devidedFileName.length-1];
        for (int i = 0; i < devidedFileName.length-1; i++) {
            bezeichnungFile += devidedFileName[i];
        }
        String material = FileClass.encodeFileToBase64(MaterialFile);
        lService.fuegeLVMaterialHinzu(new LehrveranstaltungsMaterial(bezeichnungFile, dateiEndungFile, material, this.getLvid()));
        Hinzufuegen.setText("Erfolgreich hochgeladen");
    }

    public void changeToTeilnehmerliste()
    {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("Teinehmerliste.fxml"));
            root = (Parent) fxmlLoader.load();
            Teilnehmerliste EZ = (Teilnehmerliste) fxmlLoader.getController();
            EZ.setLvid(lvid);
            EZ.setRollenId(rollenId);
            EZ.setSessionId(sessionId);
            EZ.setNutzerService(nService);
            EZ.setStudentService(sService);
            EZ.setLehrenderService(lService);
            EZ.TeilnehmerAnzeigen();
            EZ.setDate(date);
            EZ.setLoginAnzahl(loginAnzahl);
            EZ.setPrimaryStage(stage);
            scene.setRoot(root);
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }



    public void changeToTeilnehmerhinzufuegen()
    {
            try {
                scene = stage.getScene();
                fxmlLoader = new FXMLLoader(getClass().getResource("Teilnehmerhinzufuegen.fxml"));
                root = (Parent) fxmlLoader.load();
                Teilnehmerhinzufuegen EZ = (Teilnehmerhinzufuegen) fxmlLoader.getController();
                EZ.setLvid(lvid);
                EZ.setRollenId(rollenId);
                EZ.setSessionId(sessionId);
                EZ.setNutzerService(nService);
                EZ.setStudentService(sService);
                EZ.setLehrenderService(lService);
                EZ.setDate(date);
                EZ.setLoginAnzahl(loginAnzahl);
                EZ.setPrimaryStage(stage);
                scene.setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }

        }

    public void prufeRolle() {

        if (rollenId == 1) {
            Materialhinzufuegen.setVisible(false);
            Teilnehmerhinzufuegen.setVisible(false);
            QuizErstellen.setVisible(false);
            ReminderErstellen.setVisible(false);
            BewertungStatistik.setVisible(false);
            BewertungErstellen.setVisible(false);

            if(sService.pruefeBearbeitung(sessionId, lvid))
            {
                if(nService.ladeBewertung(lvid)!=null)
                {
                    BewertungTest.setVisible(true);
                }
                else
                {
                    BewertungTest.setVisible(false);
                }
            }
            else
            {
                BewertungTest.setVisible(false);
            }
        } else { // Lehrender hat rollenId = -1
            Teilnehmerhinzufuegen.setVisible(true);
            Materialhinzufuegen.setVisible(true);
            QuizErstellen.setVisible(true);
            ReminderErstellen.setVisible(true);
            if(nService.ladeBewertung(lvid)!=null)
            {
                BewertungStatistik.setVisible(true);
                BewertungErstellen.setVisible(false);
            }
            else
            {
                BewertungStatistik.setVisible(false);
                BewertungErstellen.setVisible(true);
            }
            BewertungTest.setVisible(false);


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

    public void changeToQuiz(){
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("QuizListe.fxml"));
            root = (Parent) fxmlLoader.load();
            QuizListe QL = (QuizListe) fxmlLoader.getController();
            QL.setLvid(lvid);
            QL.setRollenId(rollenId);
            QL.setSessionId(sessionId);
            QL.setNutzerService(nService);
            QL.setStudentService(sService);
            QL.setLehrenderService(lService);
            QL.pruefeRolle();
            QL.QuizAnzeigen();
            QL.setDate(date);
            QL.setLoginAnzahl(loginAnzahl);
            QL.setPrimaryStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void changeToQuizErstellen()
    {
        try {
        scene = stage.getScene();
        fxmlLoader = new FXMLLoader(getClass().getResource("QuizAnlegen.fxml"));
        root = (Parent) fxmlLoader.load();
        QuizAnlegen QA = (QuizAnlegen) fxmlLoader.getController();
        QA.setLvid(lvid);
        QA.setRollenId(rollenId);
        QA.setSessionId(sessionId);
        QA.setNutzerService(nService);
        QA.setStudentService(sService);
        QA.setLehrenderService(lService);
            QA.setDate(date);
            QA.setLoginAnzahl(loginAnzahl);
        QA.setPrimaryStage(stage);
        scene.setRoot(root);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }


    public void changeToReminderErstellen()
    {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("Terminremindererstellen.fxml"));
            root = (Parent) fxmlLoader.load();
            Terminremindererstellen TRE = (Terminremindererstellen) fxmlLoader.getController();
            TRE.setLvid(lvid);
            TRE.setRollenId(rollenId);
            TRE.setSessionId(sessionId);
            TRE.setNutzerService(nService);
            TRE.setStudentService(sService);
            TRE.setLehrenderService(lService);
            TRE.setDate(date);
            TRE.setLoginAnzahl(loginAnzahl);
            TRE.setPrimaryStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void Titel()
    {
        Titel.setText(nService.sucheLVPerID(this.getLvid()).getLehrveranstaltung().getTitel());
    }

}



