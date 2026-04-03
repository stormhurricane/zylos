import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class DatumEinstellen extends OberController{

    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;

    @FXML
    private TextField zeit;

    @FXML
    private DatePicker datum;

    @FXML
    private Label fehler;



    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
    }

    public void changeToLaunchSeite()
    {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("LaunchSeite.fxml"));
            root = (Parent) fxmlLoader.load();
            LaunchSeite LS = (LaunchSeite) fxmlLoader.getController();
            LS.setLvid(lvid);
            LS.setRollenId(rollenId);
            LS.setSessionId(sessionId);
            LS.setStudentService(sService);
            LS.setNutzerService(nService);
            LS.setLehrenderService(lService);
            LS.setDate(date);
            LS.setLoginAnzahl(loginAnzahl);
            LS.setPrimaryStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void datumAendern()
    {
        date= new HashMap<String, String>() ;
        if(zeit.getText().length()>0 && datum.getValue()!=null) {
            if (this.zeitPruefen(zeit.getText()))
            {
                String time = zeit.getText();

                String tag= new String();
                String monat= new String();

                if(datum.getValue().getDayOfMonth()>9)
                {
                    tag = new String ("" + datum.getValue().getDayOfMonth());
                }
                else
                {
                    tag = new String ("0" + datum.getValue().getDayOfMonth());
                }
                if(datum.getValue().getMonthValue()>9)
                {
                    monat = new String("" + datum.getValue().getMonthValue());
                }
                else
                {
                    monat = new String ("0" + datum.getValue().getMonthValue());
                }

                String Datum = new String(tag + "." + monat + "." + datum.getValue().getYear());
                date.put("Datum", Datum);
                date.put("Zeit", time);

                HashMap<String, String> semester= new HashMap<String, String>() ;
                semester.put("Monat", monat);
                semester.put("Jahr", ""+datum.getValue().getYear());

                this.bestehenspruefung(semester);
                this.changeToLaunchSeite();
            }
            else {
                this.fehler.setVisible(true);
            }
        }
            else
            {
                this.fehler.setVisible(true);
            }
    }

    public void bestehenspruefung(Map<String, String> zeit)
    {
        int monat = Integer.parseInt(zeit.get("Monat"));
        int jahr = Integer.parseInt(zeit.get("Jahr"));

        String semesterZeit= new String();
        String semesterJahr= new String();

        if(monat<10 && monat>4)
        {
            int jahr2 =jahr-1;
            semesterZeit=new String("ws");
            semesterJahr=new String(jahr2+"/"+jahr);
        }
        else
        {
            semesterZeit=new String("ss");
            semesterJahr=new String(""+jahr);
        }
        HashMap<String, String> semester= new HashMap<String, String>() ;
        semester.put("semesterZeit", semesterZeit);
        semester.put("semesterJahr", semesterJahr);
        nService.erzwingeBestehenspruefung(semester);
    }


    public void datumZuruecksetzen()
    {
        date=null;
        this.changeToLaunchSeite();
    }

    public void setDate(Map<String, String> date) {
        this.date = date;
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
