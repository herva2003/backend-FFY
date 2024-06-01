package com.puccampinas.backendp5noname.repositories;

import com.puccampinas.backendp5noname.domain.NutritionalValuesUser;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface NutritionalValuesUserRepository extends MongoRepository<NutritionalValuesUser, String> {

}
