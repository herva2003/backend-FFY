package com.puccampinas.backendp5noname.repositories;


import com.puccampinas.backendp5noname.domain.Nutrient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NutrientRepository extends MongoRepository<Nutrient, String> {

}
