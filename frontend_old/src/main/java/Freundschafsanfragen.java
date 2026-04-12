import Controller.Communication.FreundschaftsAnfrage;
import Controller.Communication.NutzerWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;

public class Freundschafsanfragen extends OberController{
    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;

    private ObservableList<String> nutzer;
    private ArrayList<NutzerWrapper> Liste;

    @FXML
    private ListView ListeNutzer;

    @FXML
    private Label fehler;


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
            SS.setDate(date);
            SS.setLoginAnzahl(loginAnzahl);
            SS.setPrimaryStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void FAAnzeigen()
    {
        nutzer =  FXCollections.observableArrayList();
        this.Liste= nService.offeneAnfragen(sessionId);
        if (Liste!=null)
        {
            for (int i = 0; i < Liste.size(); i++) {
                if (Liste.get(i).getMoeglicherStudent() != null) {
                    nutzer.add(i, i + 1 + ". " + Liste.get(i).getMoeglicherStudent().getVorname() + " " + Liste.get(i).getMoeglicherStudent().getNachname() + ", " + Liste.get(i).getMoeglicherStudent().getMatrikelnr());
                }
                else if ( Liste.get(i).getMoeglicherLehrender() != null){
                    nutzer.add(i, i + 1 + ". " + Liste.get(i).getMoeglicherLehrender().getVorname() + " " + Liste.get(i).getMoeglicherLehrender().getNachname());
                }
            }
            ListeNutzer.setItems(nutzer);
        }
    }


    public int FAIdHerausfinden(){
        String StudentID= this.FAPositionHerausfinden();
        int Position = Integer.parseInt(StudentID);
        Position= Position -1;
        if (Liste.get(Position).getMoeglicherStudent() != null){
            return Liste.get(Position).getMoeglicherStudent().getNutzerId();

        }
        else if (Liste.get(Position).getMoeglicherLehrender() != null){
            return Liste.get(Position).getMoeglicherLehrender().getNutzerId();
        }
        return -2; // sollte nie passieren
    }

    public String FAPositionHerausfinden() {
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

    public void annehmen()
    {
        fehler.setVisible(false);
        if (!ListeNutzer.getSelectionModel().isEmpty())
        {
            int[] ids = new int[]{sessionId, this.FAIdHerausfinden()};
            FreundschaftsAnfrage fa = new FreundschaftsAnfrage(ids, true);

           if (nService.behandleAnfrage(fa))
           {
               this.changeToFA();
           }
           else
           {
               fehler.setVisible(true);
           }
        }
    }

    public void ablehenen()
    {
        fehler.setVisible(false);
        if (!ListeNutzer.getSelectionModel().isEmpty())
        {
            int[] ids = new int[]{sessionId, this.FAIdHerausfinden()};
            FreundschaftsAnfrage fa = new FreundschaftsAnfrage(ids, false);

            if (!nService.behandleAnfrage(fa))
            {
                this.changeToFA();
            }
            else
            {
                fehler.setVisible(true);
            }
        }
    }

    public void changeToFA()
    {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("Freundschaftsanfragen.fxml"));
            root = (Parent) fxmlLoader.load();
            Freundschafsanfragen FA = (Freundschafsanfragen) fxmlLoader.getController();
            FA.setLvid(lvid);
            FA.setRollenId(rollenId);
            FA.setSessionId(sessionId);
            FA.setNutzerService(nService);
            FA.setStudentService(sService);
            FA.setLehrenderService(lService);
            FA.FAAnzeigen();
            FA.setDate(date);
            FA.setLoginAnzahl(loginAnzahl);
            FA.setPrimaryStage(stage);
            scene.setRoot(root);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


}
