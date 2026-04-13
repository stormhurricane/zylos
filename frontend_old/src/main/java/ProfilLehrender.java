import Controller.Communication.VeranstaltungsWrapper;
import datenklassen.ArbeitsThema;
import datenklassen.Lehrender;
import datenklassen.Lehrveranstaltung;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;


public class ProfilLehrender extends OberController{
    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;
    private int id;

    private ArrayList<VeranstaltungsWrapper> LehrveranstaltungenListe;
    private ObservableList<String> Lehrveranstaltung;

    private ArrayList<ArbeitsThema> ThemenangebotListe;
    private ObservableList<String> Themenangebot;

    @FXML
    private Label Vorname;

    @FXML
    private Label Nachname;

    @FXML
    private Label Adresse;

    @FXML
    private Label Email;

    @FXML
    private Label Forschungsgebiet;

    @FXML
    private Label Lehrstuhl;

    @FXML
    private ImageView ProfilView;

    @FXML
    private ListView MeineLehrveranstaltungen;

    @FXML
    private Button nachricht;

    @FXML
    private Button freundschaftsanfrage;

    @FXML
    private Label fa;

    @FXML
    private ListView themenangebote;


   public void setPrimaryStage(Stage stage) {
        this.stage = stage;
    }

   public void fillLabel(int Id) {
       id=Id;
       Lehrender lehrender = (Lehrender) nService.rufeNutzer(Id);
           Vorname.setText(lehrender.getVorname());
           Nachname.setText(lehrender.getNachname());
           Adresse.setText(lehrender.getAdresse());
           Email.setText(lehrender.getEmail());
           if(lehrender.getForschungsgebiet()!=null){
               Forschungsgebiet.setText(lehrender.getForschungsgebiet());
           }
           if(lehrender.getLehrstuhl()!=null) {
               Lehrstuhl.setText(lehrender.getLehrstuhl());
            }
            if(lehrender.getProfilbild()!=null) {
               Image img = new Image(FileClass.decodeBase64toFile(lehrender.getProfilbild()));
               ProfilView.setImage(img);
            }
    }
    public void changeToStartseite()
    {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("Startseite.fxml"));
            root = (Parent) fxmlLoader.load();
            Startseite SS = (Startseite) fxmlLoader.getController();
            SS.setLvid(lvid);
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

   public void prufeRolle(int NutzerID) {
       if (rollenId == 1)
       {
           Adresse.setVisible(false);
       }
       else if (rollenId == -1)
       {
           Adresse.setVisible(true);
       }
       else
       {
           Adresse.setVisible(false);
           // Technischer Fehler
       }
       this.prufeKommunikation(NutzerID);
   }

    public void prufeKommunikation(int NutzerID)
    {
        if (rollenId == -1)
        {
            if (NutzerID == sessionId)
            {
            nachricht.setVisible(false);
            freundschaftsanfrage.setVisible(false);
            }
            else
            {
                if (this.pruefeGemeinsameLV(NutzerID))
                {
                    nachricht.setVisible(true);
                    freundschaftsanfrage.setVisible(false);
                }
                else
                {
                    nachricht.setVisible(false);
                    freundschaftsanfrage.setVisible(false);
                }
            }
        }
        else if (rollenId == 1)
        {
            if (this.pruefeGemeinsameLV(NutzerID))
            {
                nachricht.setVisible(true);
                freundschaftsanfrage.setVisible(true);
            }
            else
            {
                nachricht.setVisible(false);
                freundschaftsanfrage.setVisible(false);
            }
        }
    }

    public boolean pruefeGemeinsameLV(int NutzerID)
    {
        ArrayList<VeranstaltungsWrapper> meineLV = nService.ladeMeineLVs(sessionId);
        ArrayList<VeranstaltungsWrapper> andereLV = nService.ladeMeineLVs(NutzerID);

        for (int i=0; i< meineLV.size(); i++)
        {
            for (int k=0; k< andereLV.size(); k++)
            {
                if(meineLV.get(i).getLehrveranstaltung()!=null && andereLV.get(k).getLehrveranstaltung()!=null)
                {
                    if(meineLV.get(i).getLehrveranstaltung().getLehrveranstaltungsID()==andereLV.get(k).getLehrveranstaltung().getLehrveranstaltungsID())
                    {
                        return true;
                    }
                }
                else if(meineLV.get(i).getProjektgruppe()!=null && andereLV.get(k).getProjektgruppe()!=null)
                {
                    if(meineLV.get(i).getProjektgruppe().getLehrveranstaltungsID()==andereLV.get(k).getProjektgruppe().getLehrveranstaltungsID())
                    {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public void MeineLehrveranstaltungen(int Id)
    {
        Lehrveranstaltung =  FXCollections.observableArrayList();
        LehrveranstaltungenListe = nService.ladeMeineLVs(Id);
        if (LehrveranstaltungenListe!=null)
        {
            for (int i = 0; i < LehrveranstaltungenListe.size(); i++)
            {
                if(LehrveranstaltungenListe.get(i).getLehrveranstaltung()!=null)
                {
                    Lehrveranstaltung.add(i, i + 1 + ". " + LehrveranstaltungenListe.get(i).getLehrveranstaltung().getTitel() + " " + LehrveranstaltungenListe.get(i).getLehrveranstaltung().getTyp() + " " + LehrveranstaltungenListe.get(i).getLehrveranstaltung().getSemesterZeit() + " " + LehrveranstaltungenListe.get(i).getLehrveranstaltung().getSemesterJahr());
                }
                if(LehrveranstaltungenListe.get(i).getProjektgruppe()!=null)
                {
                    Lehrveranstaltung.add(i, i + 1 + ". " + LehrveranstaltungenListe.get(i).getProjektgruppe().getTitel() + " Projektgruppe " + LehrveranstaltungenListe.get(i).getProjektgruppe().getSemesterZeit() + " " + LehrveranstaltungenListe.get(i).getProjektgruppe().getSemesterJahr());
                }
            }
            MeineLehrveranstaltungen.setItems(Lehrveranstaltung);
        }
        this.themenangebotPruefen(Id);
    }

    public void changeToNachrichtSchreiben()
    {
        try {
            scene = stage.getScene();
            fxmlLoader = new FXMLLoader(getClass().getResource("NachrichtSchreiben.fxml"));
            root = (Parent) fxmlLoader.load();
            NachrichtSchreiben NS = (NachrichtSchreiben) fxmlLoader.getController();
            NS.setLvid(lvid);
            NS.setRollenId(rollenId);
            NS.setSessionId(sessionId);
            NS.setEmpfaengerId(this.id);
            NS.setNutzerService(nService);
            NS.setStudentService(sService);
            NS.setLehrenderService(lService);
            NS.fillLabel();
            NS.setDate(date);
            NS.setLoginAnzahl(loginAnzahl);
            NS.setPrimaryStage(stage);
            scene.setRoot(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void frendschaftsanfrage()
    {
        if(!nService.sendeAnfrage(sessionId, id))
        {
            fa.setText("FA gesendet");
        }
        else
        {
            fa.setText("Schon befreundet");
        }
    }


    public void themenangebotPruefen(int Id)
    {
        if(rollenId==1 && nService.gemeinsameLV(sessionId, Id))
        {
            this.ThemenanboteAnzeigen(Id);
        }
        else
        if(Id==sessionId)
        {
            this.ThemenanboteAnzeigen(Id);
        }
        else
        {
            themenangebote.setVisible(false);
        }
    }

    public void ThemenanboteAnzeigen(int Id)
    {
        Themenangebot =  FXCollections.observableArrayList();
        ThemenangebotListe = nService.zeigeArbeitsThemen(Id);
        if (ThemenangebotListe!=null)
        {
            for (int i = 0; i < ThemenangebotListe.size(); i++)
            {
                Themenangebot.add(i, i + 1 + ". " + ThemenangebotListe.get(i).getTitel());
            }
            themenangebote.setItems(Themenangebot);
        }
    }

    public ArbeitsThema ThemaHerausfinden(){
        String ThemenID= this.ThemaPositionHerausfinden();
        int Position = Integer.parseInt(ThemenID);
        Position= Position -1;
        return ThemenangebotListe.get(Position);
    }

    public String ThemaPositionHerausfinden() {
        String TP = new String("");
        if (!themenangebote.getSelectionModel().isEmpty()) {
            String SID = themenangebote.getSelectionModel().getSelectedItem().toString();
            for (int i = 0; i < SID.length(); i++) {
                Character Hilfe = SID.charAt(i);
                if (Hilfe.equals('.')) {
                    return TP;
                }
                TP = new String(TP + Hilfe);
            }
            return TP; // sollte nie passieren -> for schleife sollte nie zuende gehen
        }
        return TP; // sollte nie passieren
    }

    public void changeToThemenangebot()
    {
        if (!themenangebote.getSelectionModel().isEmpty()) {
            try {
                scene = stage.getScene();
                fxmlLoader = new FXMLLoader(getClass().getResource("Themenangebot.fxml"));
                root = (Parent) fxmlLoader.load();
                Themenangebot TA = (Themenangebot) fxmlLoader.getController();
                TA.setLvid(lvid);
                TA.setRollenId(rollenId);
                TA.setSessionId(sessionId);
                TA.setNutzerService(nService);
                TA.setStudentService(sService);
                TA.setLehrenderService(lService);
                TA.fillLabel(this.ThemaHerausfinden());
                TA.setDate(date);
                TA.setLoginAnzahl(loginAnzahl);
                TA.setPrimaryStage(stage);
                scene.setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
    

