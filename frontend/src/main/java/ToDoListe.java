import Controller.Communication.NutzerWrapper;
import datenklassen.ToDos;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class ToDoListe extends OberController{
    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;

    private ObservableList<String> todo;
    private ArrayList<ToDos> Liste;

    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    private ListView ListeNutzer;


    public void TodoAnzeigen()
    {
        todo =  FXCollections.observableArrayList();
        this.Liste= nService.zeigeToDos(this.getLvid());
        if (Liste!=null)
        {
            for (int i = 0; i < Liste.size(); i++)
                {
                    String va= new String();
                    if(Liste.get(i).getVerantwortlichenId()!=0)
                    {
                       va = new String(nService.rufeNutzer(Liste.get(i).getVerantwortlichenId()).getVorname() + " " + nService.rufeNutzer(Liste.get(i).getVerantwortlichenId()).getNachname());
                    }
                    if(!Liste.get(i).isErledigt())
                    {
                        todo.add(i, i + 1 + ". " + Liste.get(i).getInhalt() + "; " + va);
                    }
                    else
                    {
                        todo.add(i, i + 1 + ". " + Liste.get(i).getInhalt() + "; " + va + "; erledigt");
                    }
                }
            ListeNutzer.setItems(todo);
        }
    }

    public void erledigt()
    {
        if (!ListeNutzer.getSelectionModel().isEmpty())
        {
            String StudentID= this.TodoPositionHerausfinden();
            int Position = Integer.parseInt(StudentID);
            Position= Position -1;
            nService.hakeToDoAb(this.TodoIdHerausfinden());
            this.changeToToDo();
        }
    }

    public int TodoIdHerausfinden(){
        String StudentID= this.TodoPositionHerausfinden();
        int Position = Integer.parseInt(StudentID);
        Position= Position -1;
        return Liste.get(Position).getId();
    }

    public String TodoPositionHerausfinden() {
        String SP = new String("");
        if (!ListeNutzer.getSelectionModel().isEmpty()) {
            String SID = ListeNutzer.getSelectionModel().getSelectedItem().toString();
            for (int i = 0; i < SID.length(); i++) {
                Character Hilfe = SID.charAt(i);
                if (Hilfe.equals('.')) {
                    return SP;
                }
                SP = new String(SP + Hilfe);
            }
            return SP; // sollte nie passieren -> for schleife sollte nie zuende gehen
        }
        return SP; // sollte nie passieren
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


    public void changeToToDoErstellen()
    {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("ToDoErstellen.fxml"));
            root = (Parent) fxmlLoader.load();
            ToDoErstellen TDE = (ToDoErstellen) fxmlLoader.getController();
            TDE.setLvid(lvid);
            TDE.setRollenId(rollenId);
            TDE.setSessionId(sessionId);
            TDE.setNutzerService(nService);
            TDE.setStudentService(sService);
            TDE.setLehrenderService(lService);
            TDE.setDate(date);
            TDE.setLoginAnzahl(loginAnzahl);
            TDE.setPrimaryStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
