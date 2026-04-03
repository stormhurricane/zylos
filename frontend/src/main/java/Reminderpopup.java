import Controller.Communication.ChatWrapper;
import Controller.Communication.VeranstaltungsWrapper;
import datenklassen.Lehrveranstaltung;
import datenklassen.Reminder;
import datenklassen.Termin;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static java.time.LocalTime.now;

public class Reminderpopup extends OberController {

    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;
    private ArrayList<Reminder> reminder;
    private ObservableList<String> liste;
    private Map<String, String> date;

    @FXML
    private Button popUpSchließen;

    @FXML
    private ListView reminderAnzeigen;

    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
    }

    public boolean reminderPruefen()
    {
        Map<String, String> dateTimeMap = new HashMap<>();
        if(date==null)
        {
            String tag= new String();
            String monat= new String();
            String stunde= new String();
            String minute= new String();

            if(LocalDateTime.now().getDayOfMonth()>9)
            {
                tag = new String ("" + LocalDateTime.now().getDayOfMonth());
            }
            else
            {
                tag = new String ("0" + LocalDateTime.now().getDayOfMonth());
            }
            if(LocalDateTime.now().getMonthValue()>9)
            {
                monat = new String("" + LocalDateTime.now().getMonthValue());
            }
            else
            {
                monat = new String ("0" + LocalDateTime.now().getMonthValue());
            }
            if(LocalDateTime.now().getHour()>9)
            {
                stunde = new String("" + LocalDateTime.now().getHour());
            }
            else
            {
                stunde = new String("0" + LocalDateTime.now().getHour());
            }
            if(LocalDateTime.now().getMinute()>9)
            {
                minute = new String("" + LocalDateTime.now().getMinute());
            }
            else
            {
                minute = new String("0" + LocalDateTime.now().getMinute());
            }

            String zeit = new String(stunde + ":" + minute);
            String datum = new String(tag + "." + monat + "." + LocalDateTime.now().getYear());

            dateTimeMap.put("date", datum);
            dateTimeMap.put("time", zeit);
        }
        else
        {
            dateTimeMap.put("date", date.get("Datum"));
            dateTimeMap.put("time", date.get("Zeit"));
        }

        reminder = nService.ladeReminder(sessionId, dateTimeMap);
        if(reminder!=null)
        {
        if(reminder.size()>0)
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

    public void reminderZeigen()
    {
        liste = FXCollections.observableArrayList();
        if (reminder != null)
        {
            for (int i = 0; i < reminder.size(); i++)
            {
                Termin hilfe = nService.terminDesReminders(reminder.get(i).getTerminId());
                Lehrveranstaltung lvhilfe = nService.sucheLVPerID(hilfe.getlvId()).getLehrveranstaltung();
                liste.add(i, hilfe.getTag() + "." + hilfe.getMonat() +"." + hilfe.getJahr() + " - " + hilfe.getUhrzeit() + " Uhr " + lvhilfe.getTitel() + ": " + hilfe.getBetreff());
            }
            reminderAnzeigen.setItems(liste);
            System.out.println(sessionId);
        }
    }

    public void schliessen()
    {
        stage.close();
    }

    public void setDate(Map<String, String> date) {
        this.date = date;
    }
}
