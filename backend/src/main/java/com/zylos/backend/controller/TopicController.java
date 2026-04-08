package com.zylos.backend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zylos.backend.database.ArbeitsThema;
import com.zylos.backend.model.dto.TopicResponse;
import com.zylos.backend.model.dto.CreateTopicRequest;
import com.zylos.backend.service.TopicService;

import java.util.List;

@RestController
@RequestMapping(path="api/v3/topic") //TODO: path anpassen /api/topics
public class TopicController {

    private final TopicService topicService;

    @Autowired
    public TopicController(TopicService topicService) {
        this.topicService = topicService;
    }


    @PostMapping(path="/addTopic") //TODO: path anpassen (KEIN PATH)
    public boolean createTopic(@RequestBody CreateTopicRequest request){
        return topicService.createTopic(request);
    }

    @GetMapping(path="/showAllTopics/{id}") // TODO path anpassen ?teacherId={id}
    public List<TopicResponse> getTopicsByTeacherId(@PathVariable("id") int teacherId) {
        return topicService.getTopicsByTeacherId(teacherId);
    }

}
