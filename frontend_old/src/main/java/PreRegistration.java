import java.io.IOException;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PreRegistration extends OberController{
    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;


    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
    }

    public void changeToRegistrationStudent() {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("RegistrationStudent.fxml"));
            root = (Parent) fxmlLoader.load();
            RegistrationStudent RS = (RegistrationStudent) fxmlLoader.getController();
            RS.setLvid(lvid);
            RS.setRollenId(rollenId);
            RS.setSessionId(sessionId);
            RS.setNutzerService(nService);
            RS.setStudentService(sService);
            RS.setLehrenderService(lService);
            RS.setDate(date);
            RS.setLoginAnzahl(loginAnzahl);
            RS.setPrimaryStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void changeToRegistrationLehrender() {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("RegistrationLehrender.fxml"));
            root = (Parent) fxmlLoader.load();
            RegistrationLehrender RL = (RegistrationLehrender) fxmlLoader.getController();
            RL.setLvid(lvid);
            RL.setRollenId(rollenId);
            RL.setSessionId(sessionId);
            RL.setNutzerService(nService);
            RL.setStudentService(sService);
            RL.setLehrenderService(lService);
            RL.setDate(date);
            RL.setLoginAnzahl(loginAnzahl);
            RL.setPrimaryStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
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
