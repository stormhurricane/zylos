import Services.LehrenderService;
import Services.NutzerService;
import Services.StudentService;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Map;

public class ZweiFaktorLogin  extends OberController{
    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;


    @FXML
    private TextField code;

    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
    }
    public void auslesen()
    {
        String sicherheit = code.getText();
        boolean zahl= true;
        if(sicherheit.length()==0)
        {
            zahl=false;
        }
        for (int i=0; i< sicherheit.length(); i++)
        {
            Character hilfe = sicherheit.charAt(i);
            if (!(hilfe.equals('0') || hilfe.equals('1') || hilfe.equals('2') || hilfe.equals('3') || hilfe.equals('4') || hilfe.equals('5') || hilfe.equals('6') || hilfe.equals('7') || hilfe.equals('8') || hilfe.equals('9')))
            {
                zahl=false;
            }
        }
            if(zahl)
            {
                int z = Integer.parseInt(sicherheit);
                boolean erfolg = sService.verifiziereLogin(this.getSessionId(), z);
                //boolean erfolg = true; /// Nur zum testen
                if (erfolg)
                {
                    this.changeToStartseite();
                }
                else
                {
                this.changeToLogin();
                }
            }
            else
            {
                this.changeToLogin();
            }
    }



    public void changeToLogin() {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("StudentLogin.fxml"));
            root = (Parent) fxmlLoader.load();
            StudentLogin SP = (StudentLogin) fxmlLoader.getController();
            SP.setLvid(lvid);
            SP.setRollenId(rollenId);
            SP.setSessionId(sessionId);
            SP.setNutzerService(nService);
            SP.setStudentService(sService);
            SP.setLehrenderService(lService);
            SP.codeFehler();
            SP.setDate(date);
            SP.setLoginAnzahl(loginAnzahl);
            SP.setPrimaryStage(stage);
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
            SS.setDate(date);
            SS.reminderPruefen();
            SS.pruefeRolle();
            SS.setPrimaryStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setDate(Map<String, String> date) {
        this.date = date;
    }
}

