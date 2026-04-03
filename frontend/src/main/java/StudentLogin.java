import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class StudentLogin extends OberController  {
    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;


    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private Label datenFehler;

    @FXML
    private Label Verbindungsfehler;

    @FXML
    private Label codeFehler;


    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
    }


    public void datenFehler()
    {
        datenFehler.setVisible(true);
    }

    public void codeFehler(){
        codeFehler.setVisible(true);
    }

    public void auslesen() {
        datenFehler.setVisible(false);
        Verbindungsfehler.setVisible(false);
        codeFehler.setVisible(false);
        if (username.getText().length() > 0) {
            if (username.getText().contains("@")) {
                Map<String, String> loginMap = new HashMap<>();
                loginMap.put("email", username.getText());
                loginMap.put("passwort", password.getText());
                Integer[] loginPerEmail = sService.loginPerEmail(loginMap);

                if (loginPerEmail[0] == -1 ) {
                    datenFehler.setVisible(true);
                }
                else if (loginPerEmail[0] == -2) {
                    Verbindungsfehler.setVisible(true);
                }
                else if(loginPerEmail[1] != 0) {
                    sessionId = loginPerEmail[0];
                    rollenId= loginPerEmail[1];
                    this.changeToZweiFaktor();
                }
                else {
                    Verbindungsfehler.setVisible(true);
                }

            } else if (username.getText().length() == 7) {
                Map<String, String> loginMap = new HashMap<>();
                loginMap.put("matrikelnummer", username.getText());
                loginMap.put("passwort", password.getText());
                Integer[] loginPerMatrikel = sService.loginPerMatrikelnummer(loginMap);

                if (loginPerMatrikel[0] == -1) {
                    datenFehler.setVisible(true);
                }
                else if (loginPerMatrikel[0] == -2) {
                    Verbindungsfehler.setVisible(true);
                }
                else if(loginPerMatrikel[1] == 1) {
                    sessionId = loginPerMatrikel[0];
                    rollenId = 1;
                    this.changeToZweiFaktor();
                }
                else {
                    Verbindungsfehler.setVisible(true);
                }

            }
            else {
                this.datenFehler();

                 }
        }
        else {
            this.datenFehler();
        }
    }


    public void changeToZweiFaktor() {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("ZweiFaktorLogin.fxml"));
            root = (Parent) fxmlLoader.load();
            ZweiFaktorLogin ZFL = (ZweiFaktorLogin) fxmlLoader.getController();
            ZFL.setLvid(lvid);
            ZFL.setRollenId(rollenId);
            ZFL.setSessionId(sessionId);
            ZFL.setNutzerService(nService);
            ZFL.setStudentService(sService);
            ZFL.setLehrenderService(lService);
            ZFL.setDate(date);
            ZFL.setLoginAnzahl(loginAnzahl);
            ZFL.setPrimaryStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void changeToLaunchSeite() {
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

    public void setDate(Map<String, String> date) {
        this.date = date;
    }
}

