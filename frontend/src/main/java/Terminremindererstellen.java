import datenklassen.Lehrveranstaltung;
import datenklassen.Reminder;
import datenklassen.Student;
import datenklassen.Termin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.FXML;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;


public class Terminremindererstellen extends OberController implements Initializable {

    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;

    @FXML
    private ChoiceBox anzeige;

    @FXML
    private DatePicker dttermin;

    @FXML
    private TextField timetermin;

    @FXML
    private TextField infotermin;

    @FXML
    private DatePicker dtreminder;

    @FXML
    private TextField timereminder;

    @FXML
    private Label fehler;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.fillChoiceBox();
    }

    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
    }


    private void fillChoiceBox() {
        ObservableList<String> choiceBoxAnzeige =
                FXCollections.observableArrayList(
                        new String("Email"),
                        new String("Pop-Up"));

        anzeige.setItems(choiceBoxAnzeige);
    }

    public void changeToLehrveranstaltungUebersicht() {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("LVUebersicht.fxml"));
            root = (Parent) fxmlLoader.load();
            LehrveranstaltungUebersicht LVU = (LehrveranstaltungUebersicht) fxmlLoader.getController();
            LVU.setLvid(lvid);
            LVU.setRollenId(rollenId);
            LVU.setSessionId(sessionId);
            LVU.setNutzerService(nService);
            LVU.setStudentService(sService);
            LVU.setLehrenderService(lService);
            LVU.Titel();
            LVU.prufeRolle();
            LVU.setDate(date);
            LVU.setLoginAnzahl(loginAnzahl);
            LVU.setPrimaryStage(stage);
            scene.setRoot(root);

        } catch (IOException e) {
            e.printStackTrace();

        }
    }


    public int terminErstelllen()
    {
        fehler.setVisible(false);
        if(date==null && dttermin.getValue().isBefore(LocalDate.now())){
            fehler.setVisible(true);
        }else{

            String jahrHilfe = new String(String.valueOf(dttermin.getValue().getYear()));
            String monatHilfe = new String(String.valueOf(dttermin.getValue().getMonthValue()));
            String tagHilfe = new String(String.valueOf(dttermin.getValue().getDayOfMonth()));
            Termin hilfe = new Termin(lvid, jahrHilfe, monatHilfe, tagHilfe, timetermin.getText(), infotermin.getText());
            int feedback = lService.erstelleTermin(hilfe);
            return feedback;
        }
        return -2;
    }

    public boolean reminderErstellen()
    {
        fehler.setVisible(false);
        if(date==null && dttermin.getValue().isBefore(LocalDate.now())){
            fehler.setVisible(true);
        }
        else if(dttermin.getValue().isBefore(dtreminder.getValue())) {
            fehler.setVisible(true);
        }else{

            String jahrHilfe = new String(String.valueOf(dttermin.getValue().getYear()));
            String monatHilfe = new String(String.valueOf(dttermin.getValue().getMonthValue()));
            String tagHilfe = new String(String.valueOf(dttermin.getValue().getDayOfMonth()));
            Termin termin = new Termin(lvid, jahrHilfe, monatHilfe, tagHilfe, timetermin.getText(), infotermin.getText());
            int terminId = lService.erstelleTermin(termin);
            String reminderJahrHilfe = new String(String.valueOf(dtreminder.getValue().getYear()));
            String remindermonatHilfe = new String(String.valueOf(dtreminder.getValue().getMonthValue()));
            String remindertagHilfe = new String(String.valueOf(dtreminder.getValue().getDayOfMonth()));
            if (anzeige.getValue().equals("Email")) {
                Reminder hilfe = new Reminder(terminId, reminderJahrHilfe, remindermonatHilfe, remindertagHilfe, timereminder.getText(), Reminder.FormEnum.EMAIL);
                return lService.reminderTermin(hilfe);
            }
            if (anzeige.getValue().equals("Pop-Up")) {
                Reminder hilfe = new Reminder(terminId, reminderJahrHilfe, remindermonatHilfe, remindertagHilfe, timereminder.getText(), Reminder.FormEnum.POPUP);
                return lService.reminderTermin(hilfe);
            }
        }
        return false;
    }

    public void erstellen()
    {
        boolean erfolg= false;
        if(dttermin.getValue()!=null && timetermin.getText().length()>0 && infotermin.getText().length()>0) {
            if (this.zeitPruefen(timetermin.getText()))
            {
                if (dtreminder.getValue() == null && timereminder.getText().length() == 0 && anzeige.getSelectionModel().isEmpty())
                {
                    int fb = this.terminErstelllen();
                    if(fb!=-2) erfolg = true;
                }
                else if (dtreminder.getValue() != null && timereminder.getText().length() > 0 && !anzeige.getSelectionModel().isEmpty())
                {
                  if(this.zeitPruefen(timereminder.getText()))
                  {
                    erfolg = this.reminderErstellen();
                }
                  else
                  {
                      this.fehler.setVisible(true);
                  }
                }
                else
                {
                    this.fehler.setVisible(true);
                }
            }
            else
            {
                this.fehler.setVisible(true);
            }
        }
        else
        {
            this.fehler.setVisible(true);
        }
        if(erfolg)
        {
            this.changeToLehrveranstaltungUebersicht();
        }
    }

    public boolean zeitPruefen(String zeit)
    {
        if (zeit.length()==5)
        {
            Character erster = zeit.charAt(0);
            Character zweiter = zeit.charAt(1);
            Character dritter = zeit.charAt(2);
            Character vierter = zeit.charAt(3);
            Character fuenfter = zeit.charAt(4);
            if(erster.equals('0') || erster.equals('1') || erster.equals('2'))
            {
                if(erster.equals('0') || erster.equals('1'))
                {
                    if(zweiter.equals('0') || zweiter.equals('1') || zweiter.equals('2') || zweiter.equals('3') || zweiter.equals('4') || zweiter.equals('5') || zweiter.equals('6') || zweiter.equals('7') || zweiter.equals('8') || zweiter.equals('9'))
                    {
                        if(dritter.equals(':'))
                        {
                            if(vierter.equals('0') || vierter.equals('1') || vierter.equals('2') || vierter.equals('3') || vierter.equals('4') || vierter.equals('5') )
                            {
                                if(fuenfter.equals('0') || fuenfter.equals('1') || fuenfter.equals('2') || fuenfter.equals('3') || fuenfter.equals('4') || fuenfter.equals('5') || fuenfter.equals('6') || fuenfter.equals('7') || fuenfter.equals('8') || fuenfter.equals('9'))
                                {
                                    return true;
                                }
                                else
                                {
                                    return false;
                                }
                            }
                            else
                            {
                                return false;
                            }
                        }
                        else
                        {
                            return false;
                        }
                    }
                    else
                    {
                        return false;
                    }
                }
                else
                {
                    if(zweiter.equals('0') || zweiter.equals('1') || zweiter.equals('2') || zweiter.equals('3'))
                    {
                        if(dritter.equals(':'))
                        {
                            if(vierter.equals('0') || vierter.equals('1') || vierter.equals('2') || vierter.equals('3') || vierter.equals('4') || vierter.equals('5') )
                            {
                               if(fuenfter.equals('0') || fuenfter.equals('1') || fuenfter.equals('2') || fuenfter.equals('3') || fuenfter.equals('4') || fuenfter.equals('5') || fuenfter.equals('6') || fuenfter.equals('7') || fuenfter.equals('8') || fuenfter.equals('9'))
                               {
                                   return true;
                               }
                               else
                               {
                                   return false;
                               }
                            }
                            else
                            {
                                return false;
                            }
                        }
                        else
                        {
                            return false;
                        }
                    }
                    else
                    {
                        return false;
                    }
                }
            }
            else
            {
                return false;
            }
        }
        else
        {
            return false;
        }
    }
}
