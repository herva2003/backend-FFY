package com.puccampinas.backendp5noname.repositories;

import com.puccampinas.backendp5noname.domain.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findByRecipeId(String recipeId);
}