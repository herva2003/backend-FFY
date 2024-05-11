package com.puccampinas.backendp5noname.repositories;


import com.puccampinas.backendp5noname.domain.Ingredient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IngredientRepository extends MongoRepository<Ingredient, String> {

    @Query(value= "{}", fields= "{ 'id' : 1, 'Descrip' : 1}")
    List<Ingredient> findAllIdAndDescription();

    List<Ingredient> findAllByIdIn(List<String> ids);


}
