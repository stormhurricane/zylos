import Controller.Communication.QuizWrapper;
import datenklassen.Frage;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;


public class QuizAnlegen extends OberController {
    Stage stage;
    private Scene scene;
    private FXMLLoader fxmlLoader;
    private Parent root;

    @FXML
    private TextField name;

    @FXML
    private Button Anlegen;

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
    public void changeToQuizFragenAnlegen() {
        if (name.getText().length() > 0) {
            try {
                scene = stage.getScene();
                fxmlLoader = new FXMLLoader(getClass().getResource("QuizFragenAnlegen.fxml"));
                root = (Parent) fxmlLoader.load();
                QuizFragenAnlegen QFA = (QuizFragenAnlegen) fxmlLoader.getController();
                QFA.setLvid(lvid);
                QFA.setRollenId(rollenId);
                QFA.setSessionId(sessionId);
                QFA.setNutzerService(nService);
                QFA.setStudentService(sService);
                QFA.setLehrenderService(lService);
                QFA.setName(name.getText());
                QFA.setDate(date);
                QFA.setLoginAnzahl(loginAnzahl);
                QFA.setPrimaryStage(stage);
                scene.setRoot(root);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void xmlAnlegen()
    {
        final FileChooser loadTXT = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML Datei (*.xml)", "*.xml");
        loadTXT.getExtensionFilters().add(extFilter);
        loadTXT.setInitialDirectory(new File(System.getProperty("user.dir")));
        File xml = loadTXT.showOpenDialog(Anlegen.getScene().getWindow());
        // https://git.uni-due.de/sktrkley/self-study-exercises-for-programming/-/blob/master/04.3/src/main/java/de/paluno/exercises/javafx/Controller.java
        // Siehe Methode: loadButtonPressed()
        // Stand: 23.05.2021 / 16 Uhr

       this.erstelleDurchXML(xml);
    }


        private void erstelleDurchXML(File file)
        {
            List<Frage> fragen = new LinkedList<>();
            DocumentBuilderFactory dbFac = DocumentBuilderFactory.newInstance();
            String name = file.getName().replace(".xml", "");
            try {
                DocumentBuilder db = dbFac.newDocumentBuilder();
                Document erstellendesQuiz = db.parse(file);
                erstellendesQuiz.getDocumentElement().normalize();

                // https://stackoverflow.com/questions/428073/what-is-the-best-simplest-way-to-read-in-an-xml-file-in-java-application
                // Stand: 15.06.2021 / 17 Uhr
                // Konzept des DocumentBuilders, Nodelist, Typecast auf Element

                NodeList nodeList = erstellendesQuiz.getElementsByTagName("Fragenzeile");
                for (int count = 0; count < nodeList.getLength(); count++) {
                    Node node = nodeList.item(count);

                    if (node.getNodeType() == Node.ELEMENT_NODE) {
                        Element nodeElement = (Element) node;
                        String frageStellung = nodeElement.getElementsByTagName("Frage").item(0).getTextContent();
                        String[] antworten = new String[] {nodeElement.getElementsByTagName("AntwortA").item(0).getTextContent(),
                                nodeElement.getElementsByTagName("AntwortB").item(0).getTextContent(),
                                nodeElement.getElementsByTagName("AntwortC").item(0).getTextContent(),
                                nodeElement.getElementsByTagName("AntwortD").item(0).getTextContent()};
                        char korrekteAntwort = nodeElement.getElementsByTagName("KorrekteAntwort").item(0).getTextContent().charAt(0);
                        fragen.add(new Frage(frageStellung, antworten[0], antworten[1], antworten[2], antworten[3], korrekteAntwort ));

                    }
                }
            } catch (ParserConfigurationException e) {
                e.printStackTrace();
            } catch (SAXException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            QuizWrapper qw = new QuizWrapper(lvid, name, fragen);
            lService.legeQuizAn(qw);
            this.changeToLehrveranstaltungUebersicht();
        }
}
