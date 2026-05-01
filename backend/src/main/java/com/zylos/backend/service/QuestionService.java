package com.zylos.backend.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.zylos.backend.model.dto.QuestionResponse;
import com.zylos.backend.database.Frage;
import com.zylos.backend.repository.FrageRepository;

import java.util.List;

@Service
@Deprecated(since="2024-06", forRemoval=true)
public class QuestionService {

    @Autowired
    FrageRepository frageRepository;


    public boolean createQuestions(int testId,List<Frage> fragen) {
        for (Frage frage : fragen) {
            frage.setTestId(testId);
            frageRepository.save(frage);
        }
        return true;
    }

    public List<Frage> findeAlleFragenMitTestId(int testId) {
        return frageRepository.findAllByTestId(testId);
    }

    public Frage findeFrageNameMitId(int frageId) { return frageRepository.findById(frageId); }

    public List<QuestionResponse> getEvaluationQuestionsForCourse(int testId) {
        return frageRepository.findAllByTestId(testId).stream()
                .map(QuestionResponse::new)
                .toList();

    }

}
