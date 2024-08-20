package com.puccampinas.backendp5noname.repositories;

import com.puccampinas.backendp5noname.domain.Topic;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface TopicRepository extends MongoRepository<Topic, String> {
}