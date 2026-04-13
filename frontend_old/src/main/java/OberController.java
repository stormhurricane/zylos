import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import Services.NutzerService;
import Services.StudentService;
import Services.LehrenderService;

import java.io.IOException;
import java.util.Map;

public abstract class OberController {
    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;

    public int getLoginAnzahl() {
        return loginAnzahl;
    }

    protected int loginAnzahl;

    public void setLoginAnzahl(int loginAnzahl) {
        this.loginAnzahl = loginAnzahl;
    }

    public Map<String, String> getDate() {
        return date;
    }

    public void setDate(Map<String, String> date) {
        this.date = date;
    }

    protected Map<String, String> date;



    protected NutzerService nService;
    protected StudentService sService;
    protected LehrenderService lService;

    protected int sessionId;
    protected int rollenId;
    protected int lvid;

    public void setLvid(int lvid){this.lvid = lvid;}

    public int getLvid() { return lvid; }

    public void setNutzerService(NutzerService nService) {
        this.nService = nService;
    }

    public void setStudentService(StudentService sService) {
        this.sService = sService;
    }

    public void setLehrenderService(LehrenderService lService){this.lService = lService;}

    public void setRollenId(int rollenId){this.rollenId = rollenId;}

    public void setSessionId(int sessionId){this.sessionId = sessionId;}

    public int getSessionId(){ return sessionId;}

    public int getRollenId(){return  rollenId;}

    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
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
            SS.setPrimaryStage(stage);
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
            LS.setRollenId(rollenId);
            LS.setSessionId(sessionId);
            LS.setStudentService(sService);
            LS.setNutzerService(nService);
            LS.setLehrenderService(lService);
            LS.setPrimaryStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
