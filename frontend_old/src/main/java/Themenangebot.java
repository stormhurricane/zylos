import datenklassen.ArbeitsThema;
import datenklassen.Literatur;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Themenangebot extends OberController {
    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;
    private String literatur;


    @FXML
    private Label Titel;

    @FXML
    private Label Beschreibung;

    @FXML
    private Label erfolg;

    @FXML
    private Button bibtex;

    @FXML
    private ListView literaturListe;





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

    public void fillLabel(ArbeitsThema at)
    {
        Titel.setText(at.getTitel());
        Beschreibung.setText(at.getBeschreibung());
        literatur= at.getLiteraturliste();
        this.literaturAnzeigen();
    }

    public void bibtexHerunterladen() throws IOException
    {
        erfolg.setVisible(false);

        String home = System.getProperty("user.home");
        File file = new File(home + "\\Downloads\\" + Titel.getText()+"."+ "txt");
        file.createNewFile();

        byte[] inhalt = FileClass.decodeBase64toFileByteArray(literatur);
        try {
            OutputStream os = new FileOutputStream(file);
            os.write(inhalt);
            os.close();

            erfolg.setVisible(true);
        }
        catch (Exception e) {
            System.out.println("Exception:" + e);
        }
    }

    public List<Literatur> bibtexAuslesen(File file) throws IOException {

        List<Literatur> literaturList = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(file));

        String line = null;
        while((line = br.readLine())!=null){


            if(line.length()==0 || line.substring(0, 1).matches("}")) {
                continue;
            }

            if(line.substring(0, 1).matches("@")){
                String art = "";
                char[] c = line.toCharArray();
                int i = 1;
                while(c[i]!='{'){
                    art += c[i];
                    i++;
                }
                Literatur literatur = new Literatur(art, "", "", "");
                literatur.setArt(art);
                literaturList.add(literatur);
            }

            if(line.contains("=")) {
                String devidedLine[] = line.split("=");
                if (devidedLine[0].equals(" title ")) {
                    String titel = "";
                    int klammern = 0;
                    char[] c = devidedLine[1].toCharArray();
                    for(int i=1; i<=c.length-1; i++) {
                        if(c[i]=='{'){
                            klammern++;
                        }
                        else if(c[i]=='}') {
                            klammern--;
                        }
                        if(klammern==0){
                            titel +="}";
                            break;

                        }
                        titel +=c[i];
                    }
                    literaturList.get(literaturList.size()-1).setTitel(titel);

                }
                if (devidedLine[0].equals(" author ")) {
                    String autor = "";
                    int klammern = 0;
                    char[] c = devidedLine[1].toCharArray();
                    for(int i=1; i<=c.length-1; i++) {
                        if(c[i]=='{'){
                            klammern++;
                        }
                        else if(c[i]=='}') {
                            klammern--;
                        }
                        if(klammern==0){
                            autor +="}";
                            break;

                        }
                        autor +=c[i];
                    }
                    literaturList.get(literaturList.size()-1).setAutor(autor);
                }
                if (devidedLine[0].equals(" year ")) {
                    String jahr = "";
                    int klammern = 0;
                    char[] c = devidedLine[1].toCharArray();
                    for(int i=1; i<=c.length-1; i++) {
                        if(c[i]=='{'){
                            klammern++;
                        }
                        else if(c[i]=='}') {
                            klammern--;
                        }
                        if(klammern==0){
                            jahr +="}";
                            break;

                        }
                        jahr +=c[i];
                    }
                    literaturList.get(literaturList.size()-1).setJahr(jahr);
                }
            }
        }
        for(Literatur literatur : literaturList) {
            if(literatur.getAutor().equals("")) {
                literatur.setAutor("unbekannt");
            }
            if(literatur.getTitel().equals("")){
                literatur.setTitel("unbekannt");
            }
            if(literatur.getJahr().equals("")){
                literatur.setJahr("unbekannt");
            }
        }
        br.close();
        return literaturList;
    }

    public void literaturAnzeigen() {
        byte[] inhalt = FileClass.decodeBase64toFileByteArray(literatur);
        String home = System.getProperty("user.home");
        File file  = new File(home + "\\Downloads\\" + Titel.getText() + ".txt");

        try {
            OutputStream os = new FileOutputStream(file);
            os.write(inhalt);
            os.close();
        }
        catch (Exception e) {
            System.out.println("Exception:" + e);
        }

        List<Literatur> lt = new ArrayList<Literatur>();

        try {
             lt = this.bibtexAuslesen(file);
             file.delete();
        }
        catch (Exception e) {
            System.out.println("Exception:" + e);
        }

        ObservableList<String> Liste =  FXCollections.observableArrayList();

        if(lt!=null)
        {
            for (int i=0; i<lt.size(); i++)
            {
                Liste.add(i, i + 1 + ". Autor: " + lt.get(i).getAutor()+ "; Jahr: " + lt.get(i).getJahr() + "; Titel: " + lt.get(i).getTitel() + "; " +lt.get(i).getArt());
            }
            literaturListe.setItems(Liste);
        }
    }
}
