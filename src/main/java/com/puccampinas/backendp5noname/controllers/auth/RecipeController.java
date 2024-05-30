package com.puccampinas.backendp5noname.controllers.auth;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.puccampinas.backendp5noname.domain.Recipe;
import com.puccampinas.backendp5noname.domain.User;
import com.puccampinas.backendp5noname.dtos.RecipeDTO;
import com.puccampinas.backendp5noname.dtos.RecipeInfoDTO;
import com.puccampinas.backendp5noname.services.RecipeService;
import com.puccampinas.backendp5noname.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/recipe/")
@SecurityRequirement(name = "bearer-key")
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UserService userService;



    @PostMapping("/generate")
    public RecipeDTO generateRecipe(@RequestBody RecipeInfoDTO data,@AuthenticationPrincipal User user) throws JsonProcessingException {
        return recipeService.generateRecipe(data,user);
    }
}
