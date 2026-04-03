import datenklassen.Lehrender;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


 public class ProfilBearbeitenLehrender extends OberController {

         Stage stage;
          private Scene scene;
         private FXMLLoader fxmlLoader;
         private Parent root;

  private String profilbild;

  @FXML
  private Label Profilstring;

  @FXML
  private Label Vorname;

  @FXML
  private Label Nachname;

  @FXML
  private TextField Adresse;

 @FXML
 private TextField Forschungsgebiet;

 @FXML
 private TextField Lehrstuhl;

 @FXML
 private TextField Passwort;

 @FXML
 private Label Email;

 @FXML
 private Button Profilbild;

 public void setPrimaryStage(Stage stage) {
            this.stage = stage;
        }

 public void fillLabel() {
     Lehrender lehrender = (Lehrender) nService.rufeNutzer(sessionId);
     Vorname.setText(lehrender.getVorname());
     Nachname.setText(lehrender.getNachname());
     Email.setText(lehrender.getEmail());
        }

 public void changeToProfilLehrender(){
     try {
         scene = stage.getScene();
         fxmlLoader = new FXMLLoader(getClass().getResource("ProfilLehrender.fxml"));
         root = (Parent) fxmlLoader.load();
         ProfilLehrender PL = (ProfilLehrender) fxmlLoader.getController();
         PL.setLvid(lvid);
         PL.setRollenId(rollenId);
         PL.setSessionId(sessionId);
         PL.setNutzerService(nService);
         PL.setStudentService(sService);
         PL.setLehrenderService(lService);
         PL.fillLabel(sessionId);
         PL.MeineLehrveranstaltungen(sessionId);
         PL.setDate(date);
         PL.setLoginAnzahl(loginAnzahl);
         PL.setPrimaryStage(stage);
         scene.setRoot(root);
     } catch (IOException e) {
        e.printStackTrace();
           }
       }
      public void changeToStartseite() {
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

      public void speichern(){
           String adresse = Adresse.getText();
           String passwort = Passwort.getText();
           String lehrstuhl = Lehrstuhl.getText();
           String forschungsgebiet = Forschungsgebiet.getText();
           Map<String, String> lehrenderHilfe = new HashMap<>();
            if(adresse.length()!=0) {
                lehrenderHilfe.put("adresse", adresse);
            }
            if(passwort.length()!=0) {
                lehrenderHilfe.put("passwort", passwort);
            }
            if(lehrstuhl.length()!=0) {
                lehrenderHilfe.put("lehrstuhl", lehrstuhl);
            }
            if(forschungsgebiet.length()!=0) {
                lehrenderHilfe.put("forschungsgebiet", forschungsgebiet);
            }
            if(profilbild!=null) {
                lehrenderHilfe.put("profilbild", this.profilbild);
            }
             lService.aktualisiereLehrender(sessionId, lehrenderHilfe);
             this.changeToProfilLehrender();
         }


    public void profilbildHochladen() throws IOException
    {
        final FileChooser loadTXT = new FileChooser();
        loadTXT.setInitialDirectory(new File(System.getProperty("user.dir")));
        File StudentFile = loadTXT.showOpenDialog(Profilbild.getScene().getWindow());
        // https://git.uni-due.de/sktrkley/self-study-exercises-for-programming/-/blob/master/04.3/src/main/java/de/paluno/exercises/javafx/Controller.java
        // Siehe Methode: loadButtonPressed()
        // Stand: 23.05.2021 / 16 Uhr

        if(FileClass.validateFileImage(StudentFile)){
            String profilbild = FileClass.encodeFileToBase64(StudentFile);
            Profilstring.setText("Eingabe gueltig");
            this.profilbild = profilbild;
        } else {
            Profilstring.setText("Dateiformat ungueltig");
        }
    }
    }

