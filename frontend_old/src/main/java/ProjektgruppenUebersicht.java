import java.io.File;
import java.io.IOException;;
import datenklassen.LehrveranstaltungsMaterial;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class ProjektgruppenUebersicht extends OberController {
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




    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
    }

    public void changeToMaterial()
    {
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
    }

    public void materialHochladen() throws IOException
    {
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

        if (rollenId == 1)
        {
                Teilnehmerhinzufuegen.setVisible(false);
        } else { // Lehrender hat rollenId = -1
            Teilnehmerhinzufuegen.setVisible(true);
        }
    }

    public void changeToStartseite()
    {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("Startseite.fxml"));
            root = (Parent) fxmlLoader.load();
            Startseite SS = (Startseite) fxmlLoader.getController();
            SS.setLvid(lvid);
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

    public void changeToChatraum(){

        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("ProjektgruppenChat.fxml"));
            root = (Parent) fxmlLoader.load();
            ProjektgruppenChat PGC = (ProjektgruppenChat) fxmlLoader.getController();
            PGC.setLvid(lvid);
            PGC.setRollenId(rollenId);
            PGC.setSessionId(sessionId);
            PGC.setNutzerService(nService);
            PGC.setStudentService(sService);
            PGC.setLehrenderService(lService);
            PGC.setDate(date);
            PGC.setLoginAnzahl(loginAnzahl);
            PGC.setPrimaryStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void changeToToDo()
    {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("ToDoListe.fxml"));
            root = (Parent) fxmlLoader.load();
            ToDoListe TDL = (ToDoListe) fxmlLoader.getController();
            TDL.setLvid(lvid);
            TDL.setRollenId(rollenId);
            TDL.setSessionId(sessionId);
            TDL.setNutzerService(nService);
            TDL.setStudentService(sService);
            TDL.setLehrenderService(lService);
            TDL.TodoAnzeigen();
            TDL.setDate(date);
            TDL.setLoginAnzahl(loginAnzahl);
            TDL.setPrimaryStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeToLernkartenListe() {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("LernkartenListe.fxml"));
            root = (Parent) fxmlLoader.load();
            LernkarteListe LKL = (LernkarteListe) fxmlLoader.getController();
            LKL.setLvid(lvid);
            LKL.setRollenId(rollenId);
            LKL.setSessionId(sessionId);
            LKL.setNutzerService(nService);
            LKL.setStudentService(sService);
            LKL.setLehrenderService(lService);
            LKL.LernkartenAnzeigen();
            LKL.setDate(date);
            LKL.setLoginAnzahl(loginAnzahl);
            LKL.setPrimaryStage(stage);
            scene.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void Titel()
    {
        Titel.setText(nService.sucheLVPerID(this.getLvid()).getProjektgruppe().getTitel());
    }

}
