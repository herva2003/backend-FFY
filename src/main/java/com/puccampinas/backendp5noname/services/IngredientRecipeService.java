package com.puccampinas.backendp5noname.services;


import com.puccampinas.backendp5noname.domain.IngredientRecipe;
import com.puccampinas.backendp5noname.dtos.IngredientRecipeDTO;
import com.puccampinas.backendp5noname.repositories.IngredientRecipeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class IngredientRecipeService {

    @Autowired
    private IngredientRecipeRepository ingredientRecipeRepository;


    public List<IngredientRecipe> convertAndSave(List<IngredientRecipeDTO> dtos) {
        List<IngredientRecipe> ingredients = dtos.stream()
                .map(IngredientRecipe::new)
                .collect(Collectors.toList());
        return ingredientRecipeRepository.saveAll(ingredients);
    }


}
