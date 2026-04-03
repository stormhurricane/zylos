import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;;
import Controller.Communication.VeranstaltungsWrapper;
import datenklassen.Lehrveranstaltung;
import datenklassen.LehrveranstaltungsMaterial;
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



public class MaterialLVanzeigen extends OberController {

    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;
    private ArrayList<LehrveranstaltungsMaterial> LehrveranstaltungsMaterial;
    private ObservableList<String> LVMaterial;

    @FXML
    private ListView LehrveranstaltungsMaterialien;

    @FXML
    private Label Erfolg;


    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
    }


    public int MaterialIdHerausfinden(){
        String MATID= this.MATPositionHerausfinden();
        int Position = Integer.parseInt(MATID);
        Position= Position -1;
        return Position;

    }

    public String MATPositionHerausfinden() {
        String MATPosition = new String("");
        if (!LehrveranstaltungsMaterialien.getSelectionModel().isEmpty()) {
            String MATID = LehrveranstaltungsMaterialien.getSelectionModel().getSelectedItem().toString();
            for (int i = 0; i < MATID.length(); i++) {
                Character Hilfe = MATID.charAt(i);
                if (Hilfe.equals('.')) {
                    return MATPosition;
                }
                MATPosition = new String(MATPosition + Hilfe);
            }
            return MATPosition; // sollte nie passieren -> for schleife sollte nie zuende gehen
        }
        return MATPosition; // sollte nie passieren
    }

    public void AlleMaterialienAnzeigen()
    {
        LVMaterial =  FXCollections.observableArrayList();

        LehrveranstaltungsMaterial = nService.ladeLVMaterial(lvid);
        if (LehrveranstaltungsMaterial==null)
        {

        }
        else {
            for (int i = 0; i < LehrveranstaltungsMaterial.size(); i++)
            {
                LVMaterial.add(i, i + 1 + ". " + LehrveranstaltungsMaterial.get(i).getBezeichnung() + "." + LehrveranstaltungsMaterial.get(i).getDateiEndung());
            }
            LehrveranstaltungsMaterialien.setItems(LVMaterial);
        }
    }

    public void materialAnsehen() throws IOException
    {
        Erfolg.setVisible(false);

        String home = System.getProperty("user.home");
        File file = new File(home + "\\Downloads\\" + LehrveranstaltungsMaterial.get(this.MaterialIdHerausfinden()).getBezeichnung() + "." + LehrveranstaltungsMaterial.get(this.MaterialIdHerausfinden()).getDateiEndung());
        file.createNewFile();

        byte[] inhalt = FileClass.decodeBase64toFileByteArray(LehrveranstaltungsMaterial.get(this.MaterialIdHerausfinden()).getInhalt());
        try {
            OutputStream os = new FileOutputStream(file);
            os.write(inhalt);
            os.close();

            Erfolg.setVisible(true);
        }
        catch (Exception e) {
            System.out.println("Exception:" + e);
        }

    }

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
