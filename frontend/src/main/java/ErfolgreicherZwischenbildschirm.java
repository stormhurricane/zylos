import Services.LehrenderService;
import Services.NutzerService;
import Services.StudentService;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ErfolgreicherZwischenbildschirm extends OberController{
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
}
