import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class RegistrationLehrender extends OberController {
     Stage stage;
     private Scene scene;
     private FXMLLoader fxmlLoader;
     private Parent root;

     @FXML
     private TextField Vorname;

     @FXML
     private TextField Nachname;

     @FXML
     private TextField Adresse;

      @FXML
      private TextField Email;

     @FXML
     private TextField Passwort;

     @FXML
     private TextField Lehrstuhl;

     @FXML
     private TextField Forschungsgebiet;

      @FXML
     private Label FehlerEmail;

     @FXML
     private Label FehlerFelder;

     @FXML
     private Label Profilstring;

     @FXML
     private Label DateiTypBeschraenkung;

     @FXML
      private Button Profilbild;

     private String profilbild;

     public void setPrimaryStage(Stage stage) {
        this.stage = stage;
    }

     public void einlesen() {
         FehlerEmail.setVisible(false);
         FehlerFelder.setVisible(false);
         Map<String, String> lehrendenData = new HashMap<String, String>();
         if(Email.getText().length()>0 && Vorname.getText().length()>0 && Nachname.getText().length()>0 && Adresse.getText().length()>0 && Passwort.getText().length()>0 && Passwort.getText().length()>0) {
             if (this.pruefeEmail(Email.getText())) {
                 lehrendenData.put("email", Email.getText());
                 lehrendenData.put("vorname", Vorname.getText());
                 lehrendenData.put("nachname", Nachname.getText());
                 lehrendenData.put("adresse", Adresse.getText());
                 lehrendenData.put("passwort", Passwort.getText());
                 lehrendenData.put("lehrstuhl", Lehrstuhl.getText());
                 lehrendenData.put("forschungsgebiet", Forschungsgebiet.getText());
                 lehrendenData.put("profilbild", this.profilbild);
                 boolean loginErfolgreich = nService.registriereLehrenden(lehrendenData);
                 if (loginErfolgreich){
                     this.changeToZwichenbildschirm();
                 }
                 else {
                     this.fehlerZeigen();
                      }
             } else {
                 this.fehlerZeigen();
             }
         }
         else
         {
             FehlerFelder.setVisible(true);
         }
     }

     public boolean pruefeEmail(String email) {
         boolean gueltig= false;
         if(email.charAt(0)!='@') {
             for (int i = 1; i < email.length(); i++) {
                 if (email.charAt(i) == '@') {
                    if(email.charAt(i+1)!='@' && email.charAt(i+1)!='.') {
                         for (int k = i + 2; k < email.length(); k++) {
                             if(email.charAt(k)=='@') {
                                 return false;
                             }
                             if (email.charAt(k) == '.') {
                                 gueltig = true;
                                 if(k<email.length()-1) {
                                     for (int h = k + 1; h < email.length(); h++) {
                                         if(email.charAt(h)=='@' && email.charAt(h)=='.') {
                                             return false;
                                         }
                                     }
                                 }
                                 else
                                 {
                                     return false;
                                 }
                             }
                         }
                     }
                     else
                     {
                         return false;
                     }
                 }
             }
         }
         else
         {
             return false;
         }
         // Prüfe ob sie doppelt ist
         return gueltig;
     }

     public void fehlerZeigen() {
        FehlerEmail.setVisible(true);
    }

     public void changeToZwichenbildschirm() {
         try {
             scene = stage.getScene();
             fxmlLoader = new FXMLLoader(getClass().getResource("ErfolgreicheRegistration.fxml"));
             root = (Parent) fxmlLoader.load();
             ErfolgreicherZwischenbildschirm EZ = (ErfolgreicherZwischenbildschirm) fxmlLoader.getController();
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

     public void changeToLaunchSeite()
     {
         try {
             scene = stage.getScene();
             fxmlLoader = new FXMLLoader(getClass().getResource("LaunchSeite.fxml"));
             root = (Parent) fxmlLoader.load();
             LaunchSeite LS = (LaunchSeite) fxmlLoader.getController();
             LS.setLvid(lvid);
             LS.setRollenId(rollenId);
             LS.setSessionId(sessionId);
             LS.setStudentService(sService);
             LS.setNutzerService(nService);
             LS.setLehrenderService(lService);
             LS.setDate(date);
             LS.setLoginAnzahl(loginAnzahl);
             LS.setPrimaryStage(stage);
             scene.setRoot(root);
         } catch (IOException e) {
             e.printStackTrace();
         }
     }
 }