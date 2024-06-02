package com.puccampinas.backendp5noname.services;

import com.puccampinas.backendp5noname.domain.NutritionalValues;
import com.puccampinas.backendp5noname.domain.NutritionalValuesUser;
import com.puccampinas.backendp5noname.dtos.NutritionalValuesDoubleDTO;
import com.puccampinas.backendp5noname.dtos.NutritionalValuesStringDTO;
import com.puccampinas.backendp5noname.dtos.NutritionalValuesUserDoubleDTO;
import com.puccampinas.backendp5noname.dtos.NutritionalValuesUserStringDTO;
import com.puccampinas.backendp5noname.repositories.NutritionalValuesUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class NutritionalValuesUserService {


    @Autowired
    private NutritionalValuesUserRepository nutritionalValuesUserRepository;


    public NutritionalValuesUser saveNutritionalValuesUser(NutritionalValuesUserDoubleDTO data) {
        return nutritionalValuesUserRepository.save(new NutritionalValuesUser(data));
    }

    public NutritionalValuesUser saveNutritionalValuesUserFromRecipe(NutritionalValuesDoubleDTO data) {
        return nutritionalValuesUserRepository.save(new NutritionalValuesUser(data));
    }

}
