package com.puccampinas.backendp5noname.repositories;

import com.puccampinas.backendp5noname.domain.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ReviewRepository extends MongoRepository<Review, String> {
}