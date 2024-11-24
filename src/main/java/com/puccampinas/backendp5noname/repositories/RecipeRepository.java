package com.puccampinas.backendp5noname.repositories;

import com.puccampinas.backendp5noname.domain.Recipe;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecipeRepository extends MongoRepository<Recipe, String> {
    Page<Recipe> findByNameContainingIgnoreCase(String name, Pageable pageable);
}