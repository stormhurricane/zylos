import Controller.Communication.VeranstaltungsWrapper;
import datenklassen.Lehrveranstaltung;
import datenklassen.Student;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import java.io.IOException;
import java.util.ArrayList;
import javafx.scene.control.Label;


public class ProfilStudent extends OberController{
  Stage stage;
  private Scene scene;
  private FXMLLoader fxmlLoader;
  private Parent root;
  private int id;

  private ArrayList<VeranstaltungsWrapper> LehrveranstaltungenListe;
  private ObservableList Lehrveranstaltung;

   @FXML
   private Label Vorname;

   @FXML
   private Label Nachname;

   @FXML
   private Label Adresse;

   @FXML
   private Label Email;

   @FXML
   private Label Matrikelnummer;

    @FXML
    private Label Studienfach;

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



   public void setPrimaryStage(Stage stage) {
        this.stage = stage;
    }


  public void fillLabel(int Id) {
       id=Id;
      Student student = (Student) nService.rufeNutzer(Id);
          Vorname.setText(student.getVorname());
          Nachname.setText(student.getNachname());
          Studienfach.setText(student.getStudienfach());
           if(student.getAdresse()!=null) {
                Adresse.setText(student.getAdresse());
             }
          Email.setText(student.getEmail());
          Studienfach.setText(student.getStudienfach());
          String Mnr = String.valueOf(student.getMatrikelnr());
          Matrikelnummer.setText(Mnr);
          if(student.getProfilbild()!=null) {
               Image img = new Image(FileClass.decodeBase64toFile(student.getProfilbild()));
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


    public void prufeRolle(int NutzerID)
    {
           if (rollenId == 1) {
               if (NutzerID == sessionId) {
                   Adresse.setVisible(true);
                   Matrikelnummer.setVisible(true);
                }
                else {
                   Adresse.setVisible(false);
                   Matrikelnummer.setVisible(false);
                 }
            }
           else if (rollenId == -1){
               Adresse.setVisible(true);
               Matrikelnummer.setVisible(true);
            }
           this.prufeKommunikation(NutzerID);
   }

    public void prufeKommunikation(int NutzerID)
    {
        if (rollenId == -1)
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
        else if (rollenId == 1)
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
                    freundschaftsanfrage.setVisible(true);
                }
                else
                {
                    nachricht.setVisible(false);
                    freundschaftsanfrage.setVisible(false);
                }
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



    public void MeineLehrveranstaltungen(int Id){
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


}

