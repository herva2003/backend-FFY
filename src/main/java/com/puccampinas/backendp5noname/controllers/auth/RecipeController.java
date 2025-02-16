package com.puccampinas.backendp5noname.controllers.auth;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.puccampinas.backendp5noname.domain.Recipe;
import com.puccampinas.backendp5noname.domain.User;
import com.puccampinas.backendp5noname.dtos.CommentDTO;
import com.puccampinas.backendp5noname.dtos.LikeDTO;
import com.puccampinas.backendp5noname.dtos.RecipeDTO;
import com.puccampinas.backendp5noname.dtos.RecipeInfoDTO;
import com.puccampinas.backendp5noname.services.RecipeService;
import com.puccampinas.backendp5noname.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/recipe")
@SecurityRequirement(name = "bearer-key")
@CrossOrigin(origins = {"http://localhost:8081", "http://localhost:3000"})
public class RecipeController {

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private UserService userService;

    private static final Logger logger = LoggerFactory.getLogger(RecipeController.class);

    @Autowired
    public RecipeController(RecipeService recipeService) {
        this.recipeService = recipeService;
    }

    @GetMapping("/getRecipes")
    public List<Recipe> getAllRecipes(
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "9") int limit,
            @RequestParam(value = "search", defaultValue = "") String search
    ) {
        int offset = (page - 1) * limit;
        return recipeService.getAllRecipes(offset, limit, search);
    }

    @PostMapping("/generate")
    public RecipeDTO generateRecipe(@RequestBody RecipeInfoDTO data, @AuthenticationPrincipal User user) throws JsonProcessingException {
        return recipeService.generateRecipe(data, user);
    }

    @GetMapping("/{recipeId}")
    public Recipe getRecipe(@PathVariable String recipeId) {
        return recipeService.getRecipeById(recipeId);
    }

    @PostMapping("/like")
    public void addLike(@RequestBody LikeDTO likeDTO) {
        recipeService.addLike(likeDTO);
    }
    @PostMapping("/unlike")
    public void removeLike(@RequestBody LikeDTO likeDTO) {
        recipeService.removeLike(likeDTO);
    }

    @PostMapping("/comment")
    public void addComment(@RequestBody CommentDTO content) {
        recipeService.addComment(content.id(), content);
    }
}