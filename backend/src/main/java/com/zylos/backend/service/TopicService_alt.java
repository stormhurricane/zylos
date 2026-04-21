package com.zylos.backend.service;

import org.springframework.stereotype.Service;

import com.zylos.backend.database.ArbeitsThema;
import com.zylos.backend.model.dto.TopicResponse;
import com.zylos.backend.model.dto.CreateTopicRequest; 
import com.zylos.backend.repository.ArbeitsThemaRepository;

import java.util.List;

@Service
public class TopicService_alt {

    private final ArbeitsThemaRepository topicRepository;

    public TopicService_alt(ArbeitsThemaRepository topicRepository) {
        this.topicRepository = topicRepository;
    }

    public boolean createTopic(CreateTopicRequest request){
        ArbeitsThema topic = new ArbeitsThema();
        topic.setTitel(request.title());
        topic.setBeschreibung(request.description());
        topic.setLehrendenId(request.teacherId()); 
        topic.setLiteraturliste(request.literatureList());
        
        topicRepository.save(topic);
        return true;
    }

    public List<TopicResponse> getTopicsByTeacherId(int teacherId){
        return topicRepository.findAllByLehrendenId(teacherId)
                .stream()
                .map(TopicResponse::new)
                .toList();
    }
}
