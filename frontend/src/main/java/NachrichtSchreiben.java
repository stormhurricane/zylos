import Controller.Communication.ChatWrapper;
import datenklassen.Chat;
import datenklassen.ChatNachricht;
import datenklassen.Lehrveranstaltung;
import datenklassen.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.control.Label;


public class NachrichtSchreiben extends OberController {
    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;
    private int EmpfaengerId;

    @FXML
    private Label Ziel;

    @FXML
    private TextArea Inhalt;

    @FXML
    private Label fehler;

    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
    }

    public void setEmpfaengerId(int empfaengerId) {
        EmpfaengerId = empfaengerId;
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

    public void fillLabel()
    {
        Ziel.setText(nService.rufeNutzer(EmpfaengerId).getVorname()+ " "+ nService.rufeNutzer(EmpfaengerId).getNachname());
    }

    public void schreiben()
    {
        fehler.setVisible(false);
        String sender= new String(nService.rufeNutzer(this.getSessionId()).getVorname()+ " "+ nService.rufeNutzer(this.getSessionId()).getNachname());
        String inhalt= Inhalt.getText();
        ChatWrapper hilfe = nService.ladeNachrichten(sessionId, EmpfaengerId);
        ChatNachricht hass = new ChatNachricht(hilfe.getChatId(), sender, inhalt);

       if( nService.sendePN(hass)!=null)
       {
           this.changeToNachrichten();
       }
       else
       {
            fehler.setVisible(true);
       }
    }

    public void changeToNachrichten() {
            try {
                scene = stage.getScene();
                fxmlLoader = new FXMLLoader(getClass().getResource("NachrichtenListe.fxml"));
                root = (Parent) fxmlLoader.load();
                NachrichtenListe NL = (NachrichtenListe) fxmlLoader.getController();
                NL.setLvid(lvid);
                NL.setRollenId(rollenId);
                NL.setSessionId(sessionId);
                NL.setNutzerService(nService);
                NL.setStudentService(sService);
                NL.setLehrenderService(lService);
                NL.setId1(sessionId);
                NL.setId2(EmpfaengerId);
                NL.NachrichtAnzeigen();
                NL.setDate(date);
                NL.setLoginAnzahl(loginAnzahl);
                NL.setPrimaryStage(stage);
                scene.setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
}
