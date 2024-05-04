package com.puccampinas.backendp5noname.services;


import com.puccampinas.backendp5noname.domain.Nutrient;
import com.puccampinas.backendp5noname.repositories.NutrientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NutrientService {


    @Autowired
    private NutrientRepository nutrientRepository;



    public List<Nutrient> allNutrients() {
        return this.nutrientRepository.findAll();
    }

}
