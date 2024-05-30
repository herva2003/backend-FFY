package com.puccampinas.backendp5noname.repositories;


import com.puccampinas.backendp5noname.domain.IngredientRecipe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IngredientRecipeRepository extends MongoRepository<IngredientRecipe, String> {

}
