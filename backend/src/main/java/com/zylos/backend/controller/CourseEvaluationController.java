package com.zylos.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zylos.backend.model.dto.CreateEvaluationFeedbackRequest;
import com.zylos.backend.model.dto.QuestionResponse;
import com.zylos.backend.model.dto.EvaluationStatisticResponse;
import com.zylos.backend.model.dto.CreateEvaluationAttemptRequest;
import com.zylos.backend.model.dto.CreateCourseEvaluationRequest;
import com.zylos.backend.service.*;

import java.util.List;

@RestController
@RequestMapping(path="api/v3/lvBewertung") // Todo : evtl. in api/courseEvaluation umbenennen
public class CourseEvaluationController {

    @Autowired
    TestService testService;

    @Autowired
    AttemptService attemptService;

    @Autowired
    EvaluationFeedbackService evaluationFeedbackService;

    @Autowired
    StatisticService statisticService;

    @PostMapping(path="/create") // TODO: leerer path für Post 
    public boolean createCourseEvaluation(@RequestBody CreateCourseEvaluationRequest request) {
        return testService.createCourseEvaluation(request);
    }

    @GetMapping(path="/showQuestions/{id}") // TODO (path="/questions/{id}"
    public List<QuestionResponse> getCourseEvaluationQuestions(@PathVariable ("id") int lvId) {
        return testService.getCourseEvaluationQuestions(lvId);
    }

    @PostMapping(path="/check/{id}") // TODO /attempts/ und alles in RequestBody
    public int createEvaluationAttempt(@PathVariable ("id") int userId,
                                          @RequestBody CreateEvaluationAttemptRequest request) {
        return attemptService.createEvaluationAttempt(userId, request.courseId());
    }

    @PostMapping(path="/createFeedback") // TODO path /feedbacks
    public boolean createEvaluationFeedbackForAttempt(@RequestBody List<CreateEvaluationFeedbackRequest> feedbackRequests) {
        return evaluationFeedbackService.createEvaluationFeedbackForAttempt(feedbackRequests);
    }

    // Erläuterung int filterStatus: -1 nicht bestanden, 0 kein Filter, 1 bestanden
    @PostMapping(path="/createFeedbackStatistics/{id}") // TODO path /statistics (mit testId in RequestBody) Änderung auf GET,  filterStatus als QUeryParameter
    public List<EvaluationStatisticResponse> getEvaluationStatistics(@PathVariable("id") int testId,
                                                   @RequestBody int filterStatus){
        return statisticService.getEvaluationStatistics(testId, filterStatus);
    }

    @PostMapping(path="/checkParticipation/{id}") // TODO path, änderung in GET
    public boolean checkStudentParticipation(@PathVariable("id") int studentId,
                                                 @RequestBody int lvId) {
        return statisticService.checkStudentParticipation(lvId, studentId);
    }

}
