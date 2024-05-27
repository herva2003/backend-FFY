package com.puccampinas.backendp5noname.controllers.auth;

import com.puccampinas.backendp5noname.domain.Ingredient;
import com.puccampinas.backendp5noname.domain.Recipe;
import com.puccampinas.backendp5noname.domain.User;
import com.puccampinas.backendp5noname.domain.vo.IngredientVO;
import com.puccampinas.backendp5noname.dtos.*;
import com.puccampinas.backendp5noname.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/user")
@SecurityRequirement(name = "bearer-key")
public class UserController {


    @Autowired
    private UserService userService;


    @GetMapping("/me")
    public ResponseEntity<User> currentUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(user);
    }



    @PostMapping("/ingredient")
    public ResponseEntity<ListApiResponse<IngredientVO>>
    addIngredient(@AuthenticationPrincipal User user,
                  @RequestBody List<IngredientVO> ingredients) {
        List<IngredientVO> addedIngredients = userService.addIngredientsToUser(user, ingredients);
        return ResponseEntity.ok(new ListApiResponse<IngredientVO>(HttpStatus.OK, "Ingredients added successfully", addedIngredients));

    }

    @GetMapping("/ingredient")
    public ResponseEntity<ListApiResponse<Ingredient>> myIngredients(@AuthenticationPrincipal User user) {
        List<Ingredient> userIngredients = this.userService.ingredientsFromUser(user);
        ListApiResponse<Ingredient> response = new ListApiResponse<>(HttpStatus.OK, "User Ingredients", userIngredients);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/recipe")
    public ResponseEntity<ListApiResponse<Recipe>> myRecipes(@AuthenticationPrincipal User user) {
        List<Recipe> userRecipes = this.userService.recipesFromUser(user);
        ListApiResponse<Recipe> response = new ListApiResponse<>(HttpStatus.OK, "User Recipes", userRecipes);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/")
    public ResponseEntity<ApiResponse<UserUpdateDTO>> updateUser(@AuthenticationPrincipal User user, @RequestBody UserUpdateDTO data) {
        User existingUser = this.userService.existUser(user);
        if( existingUser == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        if(userService.isExistingEmailFromUser(data.login(), existingUser))  return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        UserUpdateDTO updateData = this.userService.updateUser(user, data);
        return  ResponseEntity.ok(new ApiResponse<UserUpdateDTO>(HttpStatus.OK, "updated data from user",updateData));
    }

    @DeleteMapping("/ingredient/{id}")
    public ResponseEntity<Void> deleteIngredient(@AuthenticationPrincipal User user, @PathVariable String id) throws ChangeSetPersister.NotFoundException {
        User existingUser = userService.existUser(user);
        if (existingUser == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        userService.deleteIngredientFromUser(user, id);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/ingredient/")
    public ResponseEntity<Void> deleteListIngredient(@AuthenticationPrincipal User user, @RequestBody List<IngredientIDDTO> ids) throws ChangeSetPersister.NotFoundException {
        User existingUser = userService.existUser(user);
        if (existingUser == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        userService.deleteIngredientsFromUser(user, ids);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/recipe")
    public ResponseEntity<ApiResponse<RecipeDTO>> updateUser(@AuthenticationPrincipal User user, @RequestBody RecipeDTO data) {
        User existingUser = this.userService.existUser(user);
        if( existingUser == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        RecipeDTO recipe = this.userService.addRecipeToUser(user, data);
        return  ResponseEntity.ok(new ApiResponse<RecipeDTO>(HttpStatus.OK, "add recipe in user",recipe));
    }

    @DeleteMapping("/recipe/{recipeId}")
    public ResponseEntity<Void> deleteRecipeFromUser(@AuthenticationPrincipal User user, @PathVariable String recipeId) {
        User existingUser = userService.existUser(user);
        if (existingUser == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        userService.deleteRecipeFromUser(user, recipeId);
        return ResponseEntity.noContent().build();
    }





}
