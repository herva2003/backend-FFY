package com.puccampinas.backendp5noname.repositories;

import com.puccampinas.backendp5noname.domain.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RecipeRepository extends MongoRepository<Recipe, String> {
}
