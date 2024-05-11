package com.puccampinas.backendp5noname.services;


import com.puccampinas.backendp5noname.domain.Ingredient;
import com.puccampinas.backendp5noname.repositories.IngredientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
public class IngredientService {


    @Autowired
    private IngredientRepository ingredientRepository;



    public List<Ingredient> allIngredients() {
        return this.ingredientRepository.findAllIdAndDescription();
    }

    public List<Ingredient> allIngredientsById(List<String> ids) {
        return ingredientRepository.findAllById(ids);
    }

}
