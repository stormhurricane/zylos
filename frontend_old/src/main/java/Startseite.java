import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Supplier;;
import javafx.beans.property.ReadOnlyObjectProperty;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Stage;
import javafx.fxml.Initializable;


import java.util.Random;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Popup;
import javafx.stage.Stage;
import javafx.stage.Window;

public class Startseite extends OberController {
    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;
    private Executor exec;

    @FXML
    private Button Anlegen;

    @FXML
    private Button freundeslsite;

    @FXML
    private Button themen;

    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
        this.setSessionId(sessionId);
    }


    public void reminderPruefen() {
        //https://gist.github.com/james-d/346c63b6e4db4d94914f Stand 18.06.2021 Stand 10:53 Uhr

            exec = Executors.newCachedThreadPool(runnable ->
                    new Thread(runnable) {{
                        setDaemon(true);
                    }});

            Runnable messageReadSimulator = () -> {

                while (sessionId!=0) {

                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException exc) {
                        break;
                    }
                    if (Platform.isFxApplicationThread())
                    {
                        popup();
                    }
                    else
                    {
                        Platform.runLater(() -> popup());
                    }
                }
            };
            exec.execute(messageReadSimulator);
    }


    private void popup() {

        try {
            Stage primaryStage = new Stage();
            FXMLLoader loader1 = new FXMLLoader(getClass().getResource("Reminderpopup.fxml"));
            Parent root1 = (Parent) loader1.load();
            Reminderpopup RP = (Reminderpopup) loader1.getController();
            RP.setPrimaryStage(primaryStage);
            RP.setLvid(lvid);
            RP.setRollenId(rollenId);
            RP.setSessionId(sessionId);
            RP.setNutzerService(nService);
            RP.setStudentService(sService);
            RP.setLehrenderService(lService);
            RP.setDate(date);
            RP.setLoginAnzahl(loginAnzahl);
            if (RP.reminderPruefen()) {
                RP.reminderZeigen();
                Scene scene = new Scene(root1);
                primaryStage.setScene(scene);
                primaryStage.show();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeToProfilStudent() {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("ProfilStudent.fxml"));
            root = (Parent) fxmlLoader.load();
            ProfilStudent PS = (ProfilStudent) fxmlLoader.getController();
            PS.setLvid(lvid);
            PS.setRollenId(rollenId);
            PS.setSessionId(sessionId);
            PS.setNutzerService(nService);
            PS.setStudentService(sService);
            PS.setLehrenderService(lService);
            PS.fillLabel(sessionId);
            PS.MeineLehrveranstaltungen(sessionId);
            PS.setDate(date);
            PS.setLoginAnzahl(loginAnzahl);
            PS.setPrimaryStage(stage);
            this.setSessionId(0);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeToProfilLehrender() {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("ProfilLehrender.fxml"));
            root = (Parent) fxmlLoader.load();
            ProfilLehrender PL = (ProfilLehrender) fxmlLoader.getController();
            PL.setLvid(lvid);
            PL.setRollenId(rollenId);
            PL.setSessionId(sessionId);
            PL.setNutzerService(nService);
            PL.setStudentService(sService);
            PL.setLehrenderService(lService);
            PL.fillLabel(sessionId);
            PL.MeineLehrveranstaltungen(sessionId);
            PL.setDate(date);
            PL.setLoginAnzahl(loginAnzahl);
            PL.setPrimaryStage(stage);
            this.setSessionId(0);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void logout() {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("StudentLogin.fxml"));
            root = (Parent) fxmlLoader.load();
            StudentLogin SL = (StudentLogin) fxmlLoader.getController();
            this.setSessionId(0);
            SL.setLvid(lvid);
            SL.setRollenId(rollenId);
            SL.setSessionId(0);
            SL.setNutzerService(nService);
            SL.setStudentService(sService);
            SL.setLehrenderService(lService);
            SL.setDate(date);
            SL.setLoginAnzahl(loginAnzahl);
            SL.setPrimaryStage(stage);
            System.out.println(sessionId);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void changeToAlleLehrveranstaltungen() {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("AlleLehrveranstaltungen.fxml"));
            root = (Parent) fxmlLoader.load();
            AlleLehrveranstaltungen ALV = (AlleLehrveranstaltungen) fxmlLoader.getController();
            ALV.setLvid(lvid);
            ALV.setRollenId(rollenId);
            ALV.setSessionId(sessionId);
            ALV.setNutzerService(nService);
            ALV.setStudentService(sService);
            ALV.setLehrenderService(lService);
            ALV.AlleLehrveranstaltungenAnzeigen();
            ALV.setDate(date);
            ALV.setLoginAnzahl(loginAnzahl);
            ALV.setPrimaryStage(stage);
            this.setSessionId(0);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void changeToFreundesliste() {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("Freundesliste.fxml"));
            root = (Parent) fxmlLoader.load();
            Freundesliste FL = (Freundesliste) fxmlLoader.getController();
            FL.setLvid(lvid);
            FL.setRollenId(rollenId);
            FL.setSessionId(sessionId);
            FL.setNutzerService(nService);
            FL.setStudentService(sService);
            FL.setLehrenderService(lService);
            FL.freundeAnzeigen();
            FL.setDate(date);
            FL.setLoginAnzahl(loginAnzahl);
            FL.setPrimaryStage(stage);
            this.setSessionId(0);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
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
                this.setSessionId(0);
                scene.setRoot(root);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
    }



        public void changeToLehrveranstaltungAnlegen (){
         try {
        scene = stage.getScene();
        fxmlLoader = new FXMLLoader(getClass().getResource("LehrveranstaltungAnlegen.fxml"));
        root = (Parent) fxmlLoader.load();
        LehrveranstaltungAnlegen LVA = (LehrveranstaltungAnlegen) fxmlLoader.getController();
        LVA.setLvid(lvid);
        LVA.setRollenId(rollenId);
        LVA.setSessionId(sessionId);
        LVA.setNutzerService(nService);
        LVA.setStudentService(sService);
        LVA.setLehrenderService(lService);
        LVA.setDate(date);
        LVA.setLoginAnzahl(loginAnzahl);
        LVA.setPrimaryStage(stage);
        this.setSessionId(0);
        scene.setRoot(root);
    } catch (IOException e) {
        e.printStackTrace();

    }
}


        public void changeToProfilBearbeitenStudent ()
        {
            try {
                scene = stage.getScene();
                fxmlLoader = new FXMLLoader(getClass().getResource("ProfilbearbeitenStudent.fxml"));
                root = (Parent) fxmlLoader.load();
                ProfilBearbeitenStudent PBS = (ProfilBearbeitenStudent) fxmlLoader.getController();
                PBS.setLvid(lvid);
                PBS.setRollenId(rollenId);
                PBS.setSessionId(sessionId);
                PBS.setNutzerService(nService);
                PBS.setStudentService(sService);
                PBS.setLehrenderService(lService);
                PBS.fillLabel();
                PBS.setDate(date);
                PBS.setLoginAnzahl(loginAnzahl);
                PBS.setPrimaryStage(stage);
                this.setSessionId(0);
                scene.setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void changeToProfilBearbeitenLehrender ()
        {
            try {
                scene = stage.getScene();
                fxmlLoader = new FXMLLoader(getClass().getResource("ProfilbearbeitenLehrender.fxml"));
                root = (Parent) fxmlLoader.load();
                ProfilBearbeitenLehrender PBL = (ProfilBearbeitenLehrender) fxmlLoader.getController();
                PBL.setLvid(lvid);
                PBL.setRollenId(rollenId);
                PBL.setSessionId(sessionId);
                PBL.setNutzerService(nService);
                PBL.setStudentService(sService);
                PBL.setLehrenderService(lService);
                PBL.setPrimaryStage(stage);
                PBL.fillLabel();
                PBL.setDate(date);
                PBL.setLoginAnzahl(loginAnzahl);
                PBL.setPrimaryStage(stage);
                this.setSessionId(0);
                scene.setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    public void changeToLVsuchen() {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("LehrveranstaltungSuchen.fxml"));
            root = (Parent) fxmlLoader.load();
            LehrveranstaltungSuchen LVS = (LehrveranstaltungSuchen) fxmlLoader.getController();
            LVS.setLvid(lvid);
            LVS.setRollenId(rollenId);
            LVS.setSessionId(sessionId);
            LVS.setNutzerService(nService);
            LVS.setStudentService(sService);
            LVS.setLehrenderService(lService);
            LVS.setDate(date);
            LVS.setLoginAnzahl(loginAnzahl);
            LVS.setPrimaryStage(stage);
            this.setSessionId(0);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

        public void meinProfil ()
        {
            if (rollenId == -1) {
                this.changeToProfilLehrender();
            } else if (rollenId == 1) {
                this.changeToProfilStudent();
            } else {
                // Technischer Fehler
            }
        }

        public void ProfilBearbeiten ()
        {
            if (rollenId == -1) {
                this.changeToProfilBearbeitenLehrender();
            } else if (rollenId == 1) {
                this.changeToProfilBearbeitenStudent();
            } else {
                // Technischer Fehler
            }
        }

        public void pruefeRolle ()
        {
            if (rollenId == -1) {
                Anlegen.setVisible(true);
                themen.setVisible(true);
            } else if (rollenId == 1) {
                Anlegen.setVisible(false);
                themen.setVisible(false);
            } else {
                // Technischer Fehler
            }
        }

    public void changeToNachrichten()
    {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("ChatListe.fxml"));
            root = (Parent) fxmlLoader.load();
            ChatListe CL = (ChatListe) fxmlLoader.getController();
            CL.setLvid(lvid);
            CL.setRollenId(rollenId);
            CL.setSessionId(sessionId);
            CL.setNutzerService(nService);
            CL.setStudentService(sService);
            CL.setLehrenderService(lService);
            CL.ChatAnzeigen();
            CL.setDate(date);
            CL.setLoginAnzahl(loginAnzahl);
            CL.setPrimaryStage(stage);
            this.setSessionId(0);
            scene.setRoot(root);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void changeToProjektgruppeAnlegen(){
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("ProjektgruppeAnlegen.fxml"));
            root = (Parent) fxmlLoader.load();
            ProjektgruppeAnlegen PGA = (ProjektgruppeAnlegen) fxmlLoader.getController();
            PGA.setLvid(lvid);
            PGA.setRollenId(rollenId);
            PGA.setSessionId(sessionId);
            PGA.setNutzerService(nService);
            PGA.setStudentService(sService);
            PGA.setLehrenderService(lService);
            PGA.setDate(date);
            PGA.setLoginAnzahl(loginAnzahl);
            PGA.setPrimaryStage(stage);
            this.setSessionId(0);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }


    public void changeToMeineLehrveranstaltungen() {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("MeineLehrveranstaltungen.fxml"));
            root = (Parent) fxmlLoader.load();
            MeineLehrveranstaltungen MLV = (MeineLehrveranstaltungen) fxmlLoader.getController();
            MLV.setLvid(lvid);
            MLV.setRollenId(rollenId);
            MLV.setSessionId(sessionId);
            MLV.setNutzerService(nService);
            MLV.setStudentService(sService);
            MLV.setLehrenderService(lService);
            MLV.AlleLehrveranstaltungenAnzeigen();
            MLV.setDate(date);
            MLV.setLoginAnzahl(loginAnzahl);
            MLV.setPrimaryStage(stage);
            this.setSessionId(0);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void changeToKalendereinsehen() {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("Kalendereinsehen.fxml"));
            root = (Parent) fxmlLoader.load();
            Kalendereinsehen KE = (Kalendereinsehen) fxmlLoader.getController();
            KE.setLvid(lvid);
            KE.setRollenId(rollenId);
            KE.setSessionId(sessionId);
            KE.setNutzerService(nService);
            KE.setStudentService(sService);
            KE.setLehrenderService(lService);
            KE.setDate(date);
            KE.setLoginAnzahl(loginAnzahl);
            KE.setPrimaryStage(stage);
            this.setSessionId(0);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }

    public void changeToThemenangebotErstellen() {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("ThemenangebotErstellen.fxml"));
            root = (Parent) fxmlLoader.load();
            ThemenangebotErstellen TAE = (ThemenangebotErstellen) fxmlLoader.getController();
            TAE.setLvid(lvid);
            TAE.setRollenId(rollenId);
            TAE.setSessionId(sessionId);
            TAE.setNutzerService(nService);
            TAE.setStudentService(sService);
            TAE.setLehrenderService(lService);
            TAE.setDate(date);
            TAE.setLoginAnzahl(loginAnzahl);
            TAE.setPrimaryStage(stage);
            this.setSessionId(0);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();

        }
    }


    public void setDate(Map<String, String> date) {
        this.date = date;
    }
}




