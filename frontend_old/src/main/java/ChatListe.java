import Controller.Communication.ChatWrapper;
import Controller.Communication.NutzerWrapper;
import datenklassen.Chat;
import datenklassen.Student;
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

public class ChatListe extends OberController {
    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;

    @FXML
    private ListView ListeNutzer;

    private ObservableList<String> nutzer;
    private ArrayList<Chat> Liste;

    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
    }

    public void changeToStartseite() {
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

    public void ChatAnzeigen() {
        nutzer = FXCollections.observableArrayList();
        this.Liste = nService.ladeChat(sessionId);
        if (Liste != null) {
            for (int i = 0; i < Liste.size(); i++) {
                String nutzer1 = new String(nService.rufeNutzer(Liste.get(i).getNutzerId1()).getVorname() + " " + nService.rufeNutzer(Liste.get(i).getNutzerId1()).getNachname());
                String nutzer2 = new String(nService.rufeNutzer(Liste.get(i).getNutzerId2()).getVorname() + " " + nService.rufeNutzer(Liste.get(i).getNutzerId2()).getNachname());
                nutzer.add(i, i + 1 + ". " + nutzer1 + " und " + nutzer2);
            }
            ListeNutzer.setItems(nutzer);
        }
    }

    public int[] ChatIdHerausfinden() {
        String StudentID = this.ChatPositionHerausfinden();
        int Position = Integer.parseInt(StudentID);
        Position = Position - 1;
        int[] hilfe =new int[]{Liste.get(Position).getNutzerId1(),Liste.get(Position).getNutzerId2()};
        return hilfe;
    }

    public String ChatPositionHerausfinden() {
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
            return SP; // sollte nie passieren
        }
        return SP; // sollte nie passieren
    }

    public void changeToNachrichten() {
        if (!ListeNutzer.getSelectionModel().isEmpty()) {
            try {
                scene = stage.getScene();
                fxmlLoader = new FXMLLoader(getClass().getResource("NachrichtenListe.fxml"));
                root = (Parent) fxmlLoader.load();
                NachrichtenListe NL = (NachrichtenListe) fxmlLoader.getController();
                NL.setLvid(lvid);
                NL.setRollenId(rollenId);
                NL.setSessionId(sessionId);
                NL.setNutzerService(nService);
                NL.setStudentService(sService);
                NL.setLehrenderService(lService);
                NL.setId1(this.ChatIdHerausfinden()[0]);
                NL.setId2(this.ChatIdHerausfinden()[1]);
                NL.NachrichtAnzeigen();
                NL.setDate(date);
                NL.setLoginAnzahl(loginAnzahl);
                NL.setPrimaryStage(stage);
                scene.setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

