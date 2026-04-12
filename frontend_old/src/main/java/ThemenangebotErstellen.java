import datenklassen.ArbeitsThema;
import datenklassen.Literatur;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import sun.text.resources.cldr.es.FormatData_es_HN;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ThemenangebotErstellen extends OberController {
    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;
    private String literatur;


    @FXML
    private TextField Titel;

    @FXML
    private Label Fehler;

    @FXML
    private TextArea Beschreibung;

    @FXML
    private Label angelegt;

    @FXML
    private Button bibtex;



    public void setPrimaryStage(Stage stage)
    {
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

    public void anlegen()
    {
        Fehler.setVisible(false);
        if(Titel.getText().length()>0 && Beschreibung.getText().length()>0 && literatur!=null)
        {
            ArbeitsThema at = new ArbeitsThema(Titel.getText(),Beschreibung.getText());
            at.setLehrendenId(sessionId);
            at.setLiteraturliste(literatur);
            if(lService.erstelleThema(at))
            {
                this.changeToStartseite();
            }
            else
            {
                Fehler.setText("Das Themenangebot konnte nicht angelegt werden");
                Fehler.setVisible(true);
            }
        }
        else
        {
            if(Titel.getText().length()==0){
                Fehler.setText("Bitte geben Sie einen Titel an.");
            }
            else if (Beschreibung.getText().length() == 0 ){
                Fehler.setText("Bitte geben Sie eine Beschreibung an.");
            }
           else if (literatur==null){
                Fehler.setText("Bitte geben Sie die dazugehoerige Literatur an.");
            }

                Fehler.setVisible(true);
        }
    }

    public void bibtexAnlegen() throws IOException
    {
        final FileChooser loadTXT = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("bibtex Datei (*.txt)", "*.txt");
        loadTXT.getExtensionFilters().add(extFilter);
        loadTXT.setInitialDirectory(new File(System.getProperty("user.dir")));
        File MaterialFile = loadTXT.showOpenDialog(bibtex.getScene().getWindow());
        // https://git.uni-due.de/sktrkley/self-study-exercises-for-programming/-/blob/master/04.3/src/main/java/de/paluno/exercises/javafx/Controller.java
        // Siehe Methode: loadButtonPressed()
        // Stand: 23.05.2021 / 16 Uhr


        String devidedFileName[] = MaterialFile.getName().split("\\.");
        String bezeichnungFile = "";
        String dateiEndungFile = devidedFileName[devidedFileName.length-1];
        for (int i = 0; i < devidedFileName.length-1; i++) {
            bezeichnungFile += devidedFileName[i];
        }
        literatur = FileClass.encodeFileToBase64(MaterialFile);
        angelegt.setVisible(true);
    }


}
