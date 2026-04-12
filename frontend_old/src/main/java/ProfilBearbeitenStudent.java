import datenklassen.Student;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.TextField;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

 public class ProfilBearbeitenStudent extends OberController {

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
     private TextField Passwort;

     @FXML
     private TextField Studienfeld;

     @FXML
      private Label Studienfach;

     @FXML
     private Label Email;

     @FXML
      private Label Matrikelnummer;

     @FXML
     private Button Profilbild;




    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
    }


     public void fillLabel() {
         Student student = (Student) nService.rufeNutzer(sessionId);
         Vorname.setText(student.getVorname());
         Nachname.setText(student.getNachname());
         Email.setText(student.getEmail());
         String Mnr = String.valueOf(student.getMatrikelnr());
         Matrikelnummer.setText(Mnr);
     }

     public void changeToProfilStudent(){
         try {
             scene = stage.getScene();
             fxmlLoader = new FXMLLoader(getClass().getResource("ProfilStudent.fxml"));
             root = (Parent) fxmlLoader.load();
             ProfilStudent PS = (ProfilStudent) fxmlLoader.getController();
             PS.setLvid(lvid);
             PS.setRollenId(rollenId);
             PS.setSessionId(sessionId);
             PS.setNutzerService(nService);
             PS.setStudentService(sService);
             PS.setLehrenderService(lService);
             PS.fillLabel(sessionId);
             PS.MeineLehrveranstaltungen(sessionId);
             PS.setDate(date);
             PS.setLoginAnzahl(loginAnzahl);
             PS.setPrimaryStage(stage);
             scene.setRoot(root);
         } catch (IOException e) {
             e.printStackTrace();
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

     public void speichern()
     {
         String adresse = Adresse.getText();
         String passwort = Passwort.getText();
         String studienfeld = Studienfeld.getText();
         Map<String, String> studentHilfe = new HashMap<>();
         if(adresse.length()!=0) {
             studentHilfe.put("adresse", adresse);
         }
         if(passwort.length()!=0) {
             studentHilfe.put("passwort", passwort);
         }
         if (studienfeld.length() != 0){
             studentHilfe.put( "studienfach", studienfeld);
         }
         if(profilbild!=null) {
             studentHilfe.put("profilbild", this.profilbild);
         }

         sService.aktualisiereStudent(sessionId, studentHilfe);

         this.changeToProfilStudent();
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



