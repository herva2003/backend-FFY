package com.puccampinas.backendp5noname.repositories;

import com.puccampinas.backendp5noname.domain.NutritionalValues;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NutritionalValuesRepository  extends MongoRepository<NutritionalValues, String> {
}
