package com.zylos.backend;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.zylos.backend.controller.BewertungsController;
import com.zylos.backend.controller.LehrveranstaltungsController;
import com.zylos.backend.controller.PrivateChatController;
import com.zylos.backend.controller.communication.ChatWrapper;
import com.zylos.backend.database.*;
import com.zylos.backend.repository.*;
import com.zylos.backend.service.ChatNachrichtService;
import com.zylos.backend.service.StatistikService;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

//Quelle
//https://www.baeldung.com/java-spring-mockito-mock-mockbean

@SpringBootTest
public class ModulTestZyklus3 {

    @MockBean
    VersuchRepository versuchRepository;

    @MockBean
    BewertungsFeedbackRepository bewertungsFeedbackRepository;

    @MockBean
    TestRepository testRepository;

    @MockBean
    FrageRepository frageRepository;

    @Autowired
    BewertungsController bewertungsController;

    @Autowired
    StatistikService statistikService;



    @Test
    public void modulTest1() {


        //Given
        //Definiere Versuche vor
        Versuch bewertungsVersuch1 = new Versuch(1, 1, true);
        Versuch bewertungsVersuch2 = new Versuch(2, 1, false);
        bewertungsVersuch1.setId(1);
        bewertungsVersuch2.setId(2);
        List<Versuch> bewertungsVersuche = new ArrayList<>();

        bewertungsVersuche.add(bewertungsVersuch1);
        bewertungsVersuche.add(bewertungsVersuch2);
        Mockito.when(versuchRepository.findAllByTestId(1)).thenReturn(bewertungsVersuche);

        BewertungsFeedback bewertung1 = new BewertungsFeedback(1, 1, true, 'A');
        BewertungsFeedback bewertung2 = new BewertungsFeedback(2, 1, true, 'C');
        List<BewertungsFeedback> bewertungen1 = new ArrayList<>();
        List<BewertungsFeedback> bewertungen2 = new ArrayList<>();
        bewertungen1.add(bewertung1);
        bewertungen2.add(bewertung2);
        Mockito.when(bewertungsFeedbackRepository.findAllByVersuchId(1)).thenReturn(bewertungen1);
        Mockito.when(bewertungsFeedbackRepository.findAllByVersuchId(2)).thenReturn(bewertungen2);

        com.zylos.backend.database.Test bewertung = new com.zylos.backend.database.Test(1, "Bewertung");
        bewertung.setId(1);
        Mockito.when(testRepository.findTestById(1)).thenReturn(bewertung);

        Mockito.when(versuchRepository.findVersuchById(1)).thenReturn(bewertungsVersuch1);
        Mockito.when(versuchRepository.findVersuchById(2)).thenReturn(bewertungsVersuch2);

        com.zylos.backend.database.Test quiz = new com.zylos.backend.database.Test(1, "Quiz");
        quiz.setId(2);
        List<com.zylos.backend.database.Test> quize = new ArrayList<>();
        quize.add(quiz);
        Mockito.when(testRepository.findAllByLvIdAndTestArt(1, com.zylos.backend.database.Test.testArtEnum.QUIZ)).thenReturn(quize);

        Versuch quizVersuch1 = new Versuch(1, 2, true);
        Versuch quizVersuch2 = new Versuch(2, 2, false);
        quizVersuch1.setId(3);
        quizVersuch2.setId(4);
        List<Versuch> quizVersuche1 = new ArrayList<>();
        List<Versuch> quizVersuche2 = new ArrayList<>();
        quizVersuche1.add(quizVersuch1);
        quizVersuche2.add(quizVersuch2);
        Mockito.when(versuchRepository.findAllByNutzerIdAndTestIdAndBestanden(1, 2, true)).thenReturn(quizVersuche1);
        Mockito.when(versuchRepository.findAllByNutzerIdAndTestIdAndBestanden(2, 2, false)).thenReturn(quizVersuche2);

        Mockito.when(versuchRepository.findVersuchByNutzerIdAndTestId(1, 1)).thenReturn(bewertungsVersuch1);
        Mockito.when(versuchRepository.findVersuchByNutzerIdAndTestId(2, 1)).thenReturn(bewertungsVersuch2);

        Frage frage = new Frage("Mockito?", "Ja", "Nein", "Niemals", "Then Suffer", 'D');
        frage.setId(1);
        List<Frage> fragenList = new ArrayList<>();
        fragenList.add(frage);
        Mockito.when(frageRepository.findAllByTestId(1)).thenReturn(fragenList);

        //When
        // 1 = bestandene
        List<int[]> resultat = bewertungsController.erstelleBewertungsStatistik(1, 1);

        //Then
        assertEquals(1, resultat.size());
        assertEquals(1, resultat.get(0)[1]);
        assertEquals(0, resultat.get(0)[2]);
        assertEquals(0, resultat.get(0)[3]);
        assertEquals(0, resultat.get(0)[4]);


    }


    @Test
    public void modulTest2() {
        //Given
        //Definiere Versuche vor
        Versuch bewertungsVersuch1 = new Versuch(1, 1, true);
        Versuch bewertungsVersuch2 = new Versuch(2, 1, false);
        bewertungsVersuch1.setId(1);
        bewertungsVersuch2.setId(2);
        List<Versuch> bewertungsVersuche = new ArrayList<>();

        bewertungsVersuche.add(bewertungsVersuch1);
        bewertungsVersuche.add(bewertungsVersuch2);
        Mockito.when(versuchRepository.findAllByTestId(1)).thenReturn(bewertungsVersuche);

        BewertungsFeedback bewertung1 = new BewertungsFeedback(1, 1, true, 'A');
        BewertungsFeedback bewertung2 = new BewertungsFeedback(2, 1, true, 'C');
        List<BewertungsFeedback> bewertungen1 = new ArrayList<>();
        List<BewertungsFeedback> bewertungen2 = new ArrayList<>();
        bewertungen1.add(bewertung1);
        bewertungen2.add(bewertung2);
        Mockito.when(bewertungsFeedbackRepository.findAllByVersuchId(1)).thenReturn(bewertungen1);
        Mockito.when(bewertungsFeedbackRepository.findAllByVersuchId(2)).thenReturn(bewertungen2);

        com.zylos.backend.database.Test bewertung = new com.zylos.backend.database.Test(1, "Bewertung");
        bewertung.setId(1);
        Mockito.when(testRepository.findTestById(1)).thenReturn(bewertung);

        Mockito.when(versuchRepository.findVersuchById(1)).thenReturn(bewertungsVersuch1);
        Mockito.when(versuchRepository.findVersuchById(2)).thenReturn(bewertungsVersuch2);

        com.zylos.backend.database.Test quiz = new com.zylos.backend.database.Test(1, "Quiz");
        quiz.setId(2);
        List<com.zylos.backend.database.Test> quize = new ArrayList<>();
        quize.add(quiz);
        Mockito.when(testRepository.findAllByLvIdAndTestArt(1, com.zylos.backend.database.Test.testArtEnum.QUIZ)).thenReturn(quize);

        Versuch quizVersuch1 = new Versuch(1, 2, true);
        Versuch quizVersuch2 = new Versuch(2, 2, false);
        quizVersuch1.setId(3);
        quizVersuch2.setId(4);
        List<Versuch> quizVersuche1 = new ArrayList<>();
        List<Versuch> quizVersuche2 = new ArrayList<>();
        quizVersuche1.add(quizVersuch1);
        quizVersuche2.add(quizVersuch2);
        Mockito.when(versuchRepository.findAllByNutzerIdAndTestIdAndBestanden(1, 2, true)).thenReturn(quizVersuche1);
        Mockito.when(versuchRepository.findAllByNutzerIdAndTestIdAndBestanden(2, 2, false)).thenReturn(quizVersuche2);

        Mockito.when(versuchRepository.findVersuchByNutzerIdAndTestId(1, 1)).thenReturn(bewertungsVersuch1);
        Mockito.when(versuchRepository.findVersuchByNutzerIdAndTestId(2, 1)).thenReturn(bewertungsVersuch2);

        Frage frage = new Frage("Mockito?", "Ja", "Nein", "Niemals", "Then Suffer", 'D');
        frage.setId(1);
        List<Frage> fragenList = new ArrayList<>();
        fragenList.add(frage);
        Mockito.when(frageRepository.findAllByTestId(1)).thenReturn(fragenList);

        //When
        // 1 = bestandene
        List<int[]> resultat = bewertungsController.erstelleBewertungsStatistik(1, -1);

        //Then
        assertEquals(1, resultat.size());
        assertEquals(0, resultat.get(0)[1]);
        assertEquals(0, resultat.get(0)[2]);
        assertEquals(1, resultat.get(0)[3]);
        assertEquals(0, resultat.get(0)[4]);


    }

    @Test
    public void modulTest3() {
        //Given
        BewertungsFeedback bewertung1 = new BewertungsFeedback(1, 1, true, 'A');
        BewertungsFeedback bewertung2 = new BewertungsFeedback(2, 2, true, 'C');
        BewertungsFeedback bewertung3 = new BewertungsFeedback(3, 2, true, 'C');
        List<BewertungsFeedback> bewertungen = new ArrayList<>();
        bewertungen.add(bewertung1);
        bewertungen.add(bewertung2);
        bewertungen.add(bewertung3);

        //When
        int resultatA = statistikService.bestimmeAnzahlEinerAntwortEinerBewertungsfrage(1, 'A', bewertungen);
        int resultatC = statistikService.bestimmeAnzahlEinerAntwortEinerBewertungsfrage(2, 'C', bewertungen);

        //Then
        assertEquals(1, resultatA);
        assertEquals(2, resultatC);
    }

}
