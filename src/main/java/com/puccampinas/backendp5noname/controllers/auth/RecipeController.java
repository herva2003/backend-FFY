package com.puccampinas.backendp5noname.controllers.auth;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.puccampinas.backendp5noname.domain.Recipe;
import com.puccampinas.backendp5noname.dtos.RecipeDTO;
import com.puccampinas.backendp5noname.dtos.RecipeInfoDTO;
import com.puccampinas.backendp5noname.services.RecipeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/recipe/")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;



    @PostMapping("/generate")
    public RecipeDTO generateRecipe(@RequestBody RecipeInfoDTO data) throws JsonProcessingException {
        return recipeService.generateRecipe(data);
    }
}
