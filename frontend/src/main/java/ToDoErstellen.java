import Controller.Communication.VeranstaltungsWrapper;
import datenklassen.ToDos;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ToDoErstellen extends OberController  {
    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;

    @FXML
    private TextField vorname;

    @FXML
    private TextField nachname;

    @FXML
    private TextArea inhalt;

    @FXML
    private Label fehlerVA;

    @FXML
    private Label fehlerFelder;


    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
    }


    public void changeToProjektgruppenUebersicht() {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("ProjektgruppenUebersicht.fxml"));
            root = (Parent) fxmlLoader.load();
            ProjektgruppenUebersicht PGU = (ProjektgruppenUebersicht) fxmlLoader.getController();
            PGU.setLvid(lvid);
            PGU.setRollenId(rollenId);
            PGU.setSessionId(sessionId);
            PGU.setNutzerService(nService);
            PGU.setStudentService(sService);
            PGU.setLehrenderService(lService);
            PGU.Titel();
            PGU.prufeRolle();
            PGU.setDate(date);
            PGU.setLoginAnzahl(loginAnzahl);
            PGU.setPrimaryStage(stage);
            scene.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void anlegen()
    {
        fehlerFelder.setVisible(false);
        fehlerVA.setVisible(false);
        if(vorname.getText().length()!=0 && nachname.getText().length()!=0 && inhalt.getText().length()!=0)
        {
            if(this.suchen()!=0)
            {
                ToDos td = new ToDos(this.getLvid(), inhalt.getText(), this.suchen(), false);
                nService.fuegeToDosZu(td);
                this.changeToToDo();
            }
        }
        else
        {
            fehlerFelder.setVisible(true);
        }
    }

    public int suchen()
    {
        Map<String, String> zufuegeMap = new HashMap<>();
        zufuegeMap.put("vorname", vorname.getText());
        zufuegeMap.put("nachname", nachname.getText());

        ArrayList<Integer> Hilfe = lService.sucheStudent(zufuegeMap);
        if(Hilfe.size()!=0)
        {
            for (int j = 0; j < Hilfe.size(); j++) {
                ArrayList<VeranstaltungsWrapper> meineLV = nService.ladeMeineLVs(Hilfe.get(j));
                for (int i = 0; i < meineLV.size(); i++) {
                    if (meineLV.get(i).getProjektgruppe() != null) {
                        if (meineLV.get(i).getProjektgruppe().getLehrveranstaltungsID() == lvid) {
                            return Hilfe.get(j);
                        }
                    }
                }
            }
                fehlerVA.setVisible(true);
                return 0;
        }
        else
        {
            fehlerVA.setVisible(true);
            return 0;
        }
    }

    public void changeToToDo()
    {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("ToDoListe.fxml"));
            root = (Parent) fxmlLoader.load();
            ToDoListe TDL = (ToDoListe) fxmlLoader.getController();
            TDL.setLvid(lvid);
            TDL.setRollenId(rollenId);
            TDL.setSessionId(sessionId);
            TDL.setNutzerService(nService);
            TDL.setStudentService(sService);
            TDL.setLehrenderService(lService);
            TDL.TodoAnzeigen();
            TDL.setDate(date);
            TDL.setLoginAnzahl(loginAnzahl);
            TDL.setPrimaryStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
