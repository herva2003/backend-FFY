package com.puccampinas.backendp5noname.services;


import com.puccampinas.backendp5noname.domain.IngredientRecipe;
import com.puccampinas.backendp5noname.domain.NutritionalValues;
import com.puccampinas.backendp5noname.dtos.IngredientRecipeDTO;
import com.puccampinas.backendp5noname.dtos.NutritionalValuesDoubleDTO;
import com.puccampinas.backendp5noname.dtos.NutritionalValuesStringDTO;
import com.puccampinas.backendp5noname.repositories.NutritionalValuesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NutritionalValuesService {


    @Autowired
    private NutritionalValuesRepository nutritionalValuesRepository;



    public NutritionalValues saveNutritionalValues(NutritionalValuesDoubleDTO data) {
        return nutritionalValuesRepository.save(new NutritionalValues(data));
    }




}
