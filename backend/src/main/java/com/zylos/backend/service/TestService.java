package com.zylos.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zylos.backend.controller.communication.NutzerWrapper;
import com.zylos.backend.controller.communication.QuizWrapper;
import com.zylos.backend.database.Frage;
import com.zylos.backend.database.Test;
import com.zylos.backend.repository.TestRepository;

import java.util.ArrayList;
import java.util.List;

@Service
public class TestService {

    @Autowired
    TestRepository testRepository;

    @Autowired
    TeilnehmerService teilnehmerService;

    @Autowired
    FrageService frageService;

    public boolean erstelleTest(QuizWrapper quizWrapper) {
        if (testRepository.findByLvIdAndName(quizWrapper.getLvId(), quizWrapper.getName()) != null) {
            return false;
        }
        Test test = new Test(quizWrapper.getLvId(), quizWrapper.getName());
        test.setTestArt(Test.testArtEnum.QUIZ);
        testRepository.save(test);
        int testId = testRepository.findByLvIdAndName(quizWrapper.getLvId(), quizWrapper.getName()).getId();
        return frageService.erstelleFrage(testId, quizWrapper.getFragen());
    }

    public List<Test> zeigeTestlisteAn(int lvId){
        return testRepository.findAllByLvIdAndTestArt(lvId, Test.testArtEnum.QUIZ);
    }

    public Test zeigeTestAn(int testId){
        return testRepository.findTestById(testId);
    }

    public List<Integer> zeigeAlleTeilnehmerIdsEinesTestsAn(int testId) {
        List<NutzerWrapper> teilnehmerEinesTests = teilnehmerService.erstelleTeilnehmerListeEinerLV(this.zeigeTestAn(testId).getLvId());
        List<Integer> teilnehmerIdsEinesTests = new ArrayList<>();
        for(int i = 0; i < teilnehmerEinesTests.size(); i++) {
            teilnehmerIdsEinesTests.add(teilnehmerEinesTests.get(i).getMoeglicherStudent().getId());
        }
        return teilnehmerIdsEinesTests;
    }

    public boolean erstelleBewertung(QuizWrapper quizWrapper) {
        Test test = new Test(quizWrapper.getLvId(), quizWrapper.getName());
        test.setTestArt(Test.testArtEnum.BEWERTUNG);
        testRepository.save(test);
        int testId = testRepository.findByLvIdAndName(quizWrapper.getLvId(), quizWrapper.getName()).getId();
        return frageService.erstelleFrage(testId, quizWrapper.getFragen());
    }

    public List<Frage> zeigeBewertungsFragenEinerLv(int lvID) {
        int testId = testRepository.findTestByLvIdAndTestArt(lvID, Test.testArtEnum.BEWERTUNG).getId();
        return frageService.zeigeBewertungsFragenEinerLv(testId);
    }

    public int zeigeBewertungsIdAn(int lvId) {
        int testId = testRepository.findTestByLvIdAndTestArt(lvId, Test.testArtEnum.BEWERTUNG).getId();
        return testId;
    }
}
