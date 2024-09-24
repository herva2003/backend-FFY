package com.puccampinas.backendp5noname.controllers.auth;

import com.puccampinas.backendp5noname.domain.Topic;
import com.puccampinas.backendp5noname.dtos.TopicDTO;
import com.puccampinas.backendp5noname.services.TopicService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/topics")
@SecurityRequirement(name = "bearer-key")
public class TopicController {
    @Autowired
    private  TopicService topicService;


    @GetMapping("/")
    public List<Topic> getAllTopics() {
        return topicService.getAllTopics();
    }

    @PostMapping("/")
    public Topic createTopic(@RequestBody TopicDTO topic) {
        return topicService.createTopic(topic);
    }

    @GetMapping("/{id}")
    public Topic getTopic(@PathVariable String id) {
        return topicService.getTopicById(id);
    }
}