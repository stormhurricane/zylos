import java.io.IOException;
import java.util.Map;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class LaunchSeite extends OberController{
    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;



    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
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
            SP.setDate(date);
            SP.setLoginAnzahl(loginAnzahl);
            SP.setPrimaryStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void changeToPreRegistration() {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("PreRegistration.fxml"));
            root = (Parent) fxmlLoader.load();
            PreRegistration PR = (PreRegistration) fxmlLoader.getController();
            PR.setLvid(lvid);
            PR.setRollenId(rollenId);
            PR.setSessionId(sessionId);
            PR.setNutzerService(nService);
            PR.setStudentService(sService);
            PR.setLehrenderService(lService);
            PR.setDate(date);
            PR.setLoginAnzahl(loginAnzahl);
            PR.setPrimaryStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setDate(Map<String, String> date) {
        this.date = date;
    }

    public void changeToDatumEinstellen() {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("DatumEinstellen.fxml"));
            root = (Parent) fxmlLoader.load();
            DatumEinstellen DE = (DatumEinstellen) fxmlLoader.getController();
            DE.setLvid(lvid);
            DE.setRollenId(rollenId);
            DE.setSessionId(sessionId);
            DE.setNutzerService(nService);
            DE.setStudentService(sService);
            DE.setLehrenderService(lService);
            DE.setDate(date);
            DE.setLoginAnzahl(loginAnzahl);
            DE.setPrimaryStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}