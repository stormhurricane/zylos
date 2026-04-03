import java.io.IOException;;
import Controller.Communication.VeranstaltungsWrapper;
import datenklassen.Lehrveranstaltung;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import javafx.collections.ObservableList;




public class AlleLehrveranstaltungen extends OberController {

    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;
    private ArrayList<VeranstaltungsWrapper> AlleLehrveranstaltungen;
    private ObservableList<String> Lehrveranstaltung;

    @FXML
    private Button Beitreten;

    @FXML
    private Button LehrveranstaltungAnzeigen;

    @FXML
    private ListView AlleLehrveranstaltungenAnzeigen;

    @FXML
    private Label FehlerAnzeigen;

    @FXML
    private Label FehlerBeitreten;

    @FXML
    private Label privat;


    public void setPrimaryStage(Stage stage) {
        this.stage = stage;
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


    public void Beitreten() {
        FehlerBeitreten.setVisible(false);
        FehlerAnzeigen.setVisible(false);
        privat.setVisible(false);

        if (!AlleLehrveranstaltungenAnzeigen.getSelectionModel().isEmpty()) {
            if (this.sichtbarkeitHerausfinden()) {
                Map<String, Integer> teilnahmeMap = new HashMap<>();
                this.LVIdHerausfinden();
                teilnahmeMap.put("nutzerID", sessionId);
                teilnahmeMap.put("lehrveranstaltungsID", this.getLvid());
                boolean erfolg = nService.treteLVBei(teilnahmeMap);
                if (erfolg == true) {
                    if (this.LVIdHerausfinden() == 10) {
                        this.changeToLehrveranstaltungUebersicht();
                    } else if (this.LVIdHerausfinden() == -10) {
                        this.changeToProjektgruppenUebersicht();
                    }
                } else {
                    FehlerBeitreten.setVisible(true);
                }
            }
            else
            {
                privat.setVisible(true);
            }
        }
    }

    public void LehrveranstaltungAnzeigen()
    {
        FehlerBeitreten.setVisible(false);
        FehlerAnzeigen.setVisible(false);
        privat.setVisible(false);

        if (!AlleLehrveranstaltungenAnzeigen.getSelectionModel().isEmpty()) {
            if (this.sichtbarkeitHerausfinden()) {
                if (this.LVIdHerausfinden() == 10) {
                    this.changeToLehrveranstaltungUebersicht();
                } else if (this.LVIdHerausfinden() == -10) {
                    this.changeToProjektgruppenUebersicht();
                }
            }
            else
            {
                privat.setVisible(true);
            }
        }
    }

    public int LVIdHerausfinden()
    {
    String LVID= this.LVPositionHerausfinden();
    int Position = Integer.parseInt(LVID);
    Position= Position -1;
    if(AlleLehrveranstaltungen.get(Position).getLehrveranstaltung()!=null)
    {
        this.setLvid(AlleLehrveranstaltungen.get(Position).getLehrveranstaltung().getLehrveranstaltungsID());
        return 10;
    }
    else if(AlleLehrveranstaltungen.get(Position).getProjektgruppe()!=null)
    {
        this.setLvid(AlleLehrveranstaltungen.get(Position).getProjektgruppe().getLehrveranstaltungsID());
        return -10;
    }
        return 0 ;// sollte nie passieren
    }

    public String LVPositionHerausfinden() {
        String LVP = new String("");
        if (!AlleLehrveranstaltungenAnzeigen.getSelectionModel().isEmpty()) {
            String LVID = AlleLehrveranstaltungenAnzeigen.getSelectionModel().getSelectedItem().toString();
            for (int i = 0; i < LVID.length(); i++) {
                Character Hilfe = LVID.charAt(i);
                if (Hilfe.equals('.')) {
                    return LVP;
                }
                LVP = new String(LVP + Hilfe);
            }
            return LVP; // sollte nie passieren
        }
        return LVP; // sollte nie passieren
    }

    public void AlleLehrveranstaltungenAnzeigen()
    {
        FehlerAnzeigen.setVisible(false);
        Lehrveranstaltung =  FXCollections.observableArrayList();

        AlleLehrveranstaltungen = nService.lvListe();
        if (AlleLehrveranstaltungen==null)
        {
            FehlerAnzeigen.setVisible(true);
        }
        else {
            for (int i = 0; i < AlleLehrveranstaltungen.size(); i++)
            {
                if(AlleLehrveranstaltungen.get(i).getLehrveranstaltung()!=null)
                {
                    Lehrveranstaltung.add(i, i + 1 + ". " + AlleLehrveranstaltungen.get(i).getLehrveranstaltung().getTitel() + " " + AlleLehrveranstaltungen.get(i).getLehrveranstaltung().getTyp() + " " + AlleLehrveranstaltungen.get(i).getLehrveranstaltung().getSemesterZeit() + " " + AlleLehrveranstaltungen.get(i).getLehrveranstaltung().getSemesterJahr());
                }
               else if(AlleLehrveranstaltungen.get(i).getProjektgruppe()!=null)
               {
                    if (!AlleLehrveranstaltungen.get(i).getProjektgruppe().isSichtbarkeit())
                    {
                        if(rollenId==1)
                        {
                            if(!nService.pruefeTeilnahme(sessionId, AlleLehrveranstaltungen.get(i).getProjektgruppe().getLehrveranstaltungsID()))
                            {
                                Lehrveranstaltung.add(i, i + 1 + ". " + AlleLehrveranstaltungen.get(i).getProjektgruppe().getTitel() + " Projektgruppe " + AlleLehrveranstaltungen.get(i).getProjektgruppe().getSemesterZeit() + " " + AlleLehrveranstaltungen.get(i).getProjektgruppe().getSemesterJahr() + "; Privat");
                            }
                            else
                            {
                                Lehrveranstaltung.add(i, i + 1 + ". " + AlleLehrveranstaltungen.get(i).getProjektgruppe().getTitel() + " Projektgruppe " + AlleLehrveranstaltungen.get(i).getProjektgruppe().getSemesterZeit() + " " + AlleLehrveranstaltungen.get(i).getProjektgruppe().getSemesterJahr());
                            }
                        }
                        else
                        {
                            Lehrveranstaltung.add(i, i + 1 + ". " + AlleLehrveranstaltungen.get(i).getProjektgruppe().getTitel() + " Projektgruppe " + AlleLehrveranstaltungen.get(i).getProjektgruppe().getSemesterZeit() + " " + AlleLehrveranstaltungen.get(i).getProjektgruppe().getSemesterJahr());
                        }
                    }
                    else
                    {
                        Lehrveranstaltung.add(i, i + 1 + ". " + AlleLehrveranstaltungen.get(i).getProjektgruppe().getTitel() + " Projektgruppe " + AlleLehrveranstaltungen.get(i).getProjektgruppe().getSemesterZeit() + " " + AlleLehrveranstaltungen.get(i).getProjektgruppe().getSemesterJahr());
                    }
                }
            }
            AlleLehrveranstaltungenAnzeigen.setItems(Lehrveranstaltung);
        }
    }


    public boolean sichtbarkeitHerausfinden() {
        String LVP = new String("");
        if (!AlleLehrveranstaltungenAnzeigen.getSelectionModel().isEmpty()) {
            String LVID = AlleLehrveranstaltungenAnzeigen.getSelectionModel().getSelectedItem().toString();
            for (int j = 0; j < LVID.length(); j++) {
                Character Hilfe = LVID.charAt(j);
                if (Hilfe.equals(';')) {
                    Character space = LVID.charAt(j + 1);
                    Character p = LVID.charAt(j + 2);
                    Character r = LVID.charAt(j + 3);
                    Character i = LVID.charAt(j + 4);
                    Character v = LVID.charAt(j + 5);
                    Character a = LVID.charAt(j + 6);
                    Character t = LVID.charAt(j + 7);

                    if (space.equals(' '))
                    {
                        if (p.equals('P'))
                        {
                            if (r.equals('r'))
                            {
                                if (i.equals('i'))
                                {
                                    if (v.equals('v'))
                                    {
                                        if (a.equals('a'))
                                        {
                                            if (t.equals('t'))
                                            {
                                                if ((j + 8)==LVID.length())
                                                {
                                                    return false;
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
            return true;
        }
        return true; // sollte nie passieren
    }

    public void changeToStartseite()
    {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("Startseite.fxml"));
            root = (Parent) fxmlLoader.load();
            Startseite SS = (Startseite) fxmlLoader.getController();
            SS.setLvid(0);
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
}
