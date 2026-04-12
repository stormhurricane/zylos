import Client.RetrofitClient;
import Controller.LehrenderEndpoint;
import Controller.NutzerEndpoint;
import Controller.StudentEndpoint;
import Services.LehrenderService;
import Services.NutzerService;
import Services.StudentService;
import datenklassen.Lehrender;
import datenklassen.Nutzer;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import retrofit2.Response;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;

public class Launch extends Application {

    public static final String BASE_URL = "http://localhost:8080";



    @Override
    public void start(Stage stage) throws Exception
    {
        StudentEndpoint studentEndpoint = getStudentClient(BASE_URL);
        NutzerEndpoint nutzerendpoint = getNutzerClient(BASE_URL);
        LehrenderEndpoint lehrenderEndpoint = getLehrenderClient(BASE_URL);
        NutzerService nService = new NutzerService(nutzerendpoint);
        StudentService sService = new StudentService(studentEndpoint);
        LehrenderService lService = new LehrenderService(lehrenderEndpoint);
        FXMLLoader loader = new FXMLLoader(getClass().getResource("LaunchSeite.fxml"));
        Parent root = (Parent) loader.load();
        LaunchSeite LS = (LaunchSeite) loader.getController();
        int rollenId = 0; // 1 = Student & -1 = Lehrender
        int sessionId = 0;
        int lvid = 0;
        LS.setLoginAnzahl(0);
        LS.setLvid(lvid);
        LS.setRollenId(rollenId);
        LS.setSessionId(sessionId);
        LS.setStudentService(sService);
        LS.setNutzerService(nService);
        LS.setLehrenderService(lService);
        LS.setPrimaryStage(stage);
        LS.setDate(null);
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }


    public static NutzerEndpoint getNutzerClient(String url){
        return RetrofitClient.getClient(url).create(NutzerEndpoint.class);
    }

    public static StudentEndpoint getStudentClient(String url){
        return RetrofitClient.getClient(url).create(StudentEndpoint.class);
    }

    public static LehrenderEndpoint getLehrenderClient(String url){
        return RetrofitClient.getClient(url).create(LehrenderEndpoint.class);
    }

    public static void main(String[]args){ launch(args); }

}

