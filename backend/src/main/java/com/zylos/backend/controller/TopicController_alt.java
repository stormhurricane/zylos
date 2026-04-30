package com.zylos.backend.controller;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.zylos.backend.model.dto.TopicResponse;
import com.zylos.backend.model.dto.CreateTopicRequest;
import com.zylos.backend.service.TopicService_alt;

import java.util.List;

@RestController
@Deprecated(since = "2026-04", forRemoval = true)
@RequestMapping(path="api/v3/topic") //TODO: path anpassen /api/topics
public class TopicController_alt {

    private final TopicService_alt topicService;

    @Autowired
    public TopicController_alt(TopicService_alt topicService) {
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
