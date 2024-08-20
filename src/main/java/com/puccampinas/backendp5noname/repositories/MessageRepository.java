package com.puccampinas.backendp5noname.repositories;

import com.puccampinas.backendp5noname.domain.Message;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface MessageRepository extends MongoRepository<Message, String> {
    List<Message> findByTopicId(String topicId);
}