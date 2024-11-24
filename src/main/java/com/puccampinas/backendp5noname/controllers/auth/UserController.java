package com.puccampinas.backendp5noname.controllers.auth;

import com.puccampinas.backendp5noname.domain.*;
import com.puccampinas.backendp5noname.domain.vo.IngredientVO;
import com.puccampinas.backendp5noname.dtos.*;
import com.puccampinas.backendp5noname.services.RecipeService;
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
import java.util.Map;

@RestController
@RequestMapping("/api/v1/user")
@SecurityRequirement(name = "bearer-key")
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RecipeService recipeService;

    @GetMapping("/me")
    public ResponseEntity<User> currentUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(user);
    }

    @PostMapping("/ingredient")
    public ResponseEntity<ListApiResponse<IngredientVO>>
    addIngredient(@AuthenticationPrincipal User user, @RequestBody List<IngredientVO> ingredients) {
        List<IngredientVO> addedIngredients = userService.addIngredientsToUser(user, ingredients);
        return ResponseEntity.ok(new ListApiResponse<IngredientVO>(HttpStatus.OK, "Ingredients added successfully", addedIngredients));
    }

    @GetMapping("/ingredient")
    public ResponseEntity<ListApiResponse<UserIngredient>> myIngredients(@AuthenticationPrincipal User user) {
        List<UserIngredient> userIngredients = this.userService.ingredientsFromUser(user);
        ListApiResponse<UserIngredient> response = new ListApiResponse<>(HttpStatus.OK, "User Ingredients", userIngredients);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/ingredient/")
    public ResponseEntity<Void> deleteListIngredient(@AuthenticationPrincipal User user, @RequestBody List<IngredientIDDTO> ids) throws ChangeSetPersister.NotFoundException {
        User existingUser = userService.existUser(user);
        if (existingUser ==  null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        userService.deleteIngredientsFromUser(user, ids);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/shoppList")
    public ResponseEntity<ListApiResponse<IngredientVO>>
    addIngredientShoppList(@AuthenticationPrincipal User user, @RequestBody List<IngredientVO> ingredients) {
        List<IngredientVO> addedIngredients = userService.addIngredientsToShoppList(user, ingredients);
        return ResponseEntity.ok(new ListApiResponse<IngredientVO>(HttpStatus.OK, "Ingredients added to Shopping list successfully", addedIngredients));
    }

    @GetMapping("/shoppList")
    public ResponseEntity<ListApiResponse<UserIngredient>> myShoppList(@AuthenticationPrincipal User user) {
        List<UserIngredient> userIngredients = this.userService.ingredientsFromShoppList(user);
        ListApiResponse<UserIngredient> response = new ListApiResponse<>(HttpStatus.OK, "User Shopping list", userIngredients);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/shoppList")
    public ResponseEntity<Void> deleteIngredientShoppList(@AuthenticationPrincipal User user, @RequestBody List<IngredientIDDTO> ingredientIds) {
        User existingUser = userService.existUser(user);
        if (existingUser == null)
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();

        userService.deleteIngredientsFromShoppList(existingUser, ingredientIds);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/nv")
    public ResponseEntity<ListApiResponse<NutritionalValuesUser>> nutritionalValuesFromUser(@AuthenticationPrincipal User user) {
        List<NutritionalValuesUser> userNv = this.userService.nutritionalValuesFromUser(user);
        ListApiResponse<NutritionalValuesUser> response = new ListApiResponse<>(HttpStatus.OK, "User nutritional Values", userNv);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/recipe")
    public ResponseEntity<ListApiResponse<Recipe>> myRecipes(
            @AuthenticationPrincipal User user,
            @RequestParam(value = "page", defaultValue = "1") int page,
            @RequestParam(value = "limit", defaultValue = "10") int limit,
            @RequestParam(value = "search", required = false) String searchQuery
    ) {
        List<Recipe> userRecipes = userService.recipesFromUser(user, searchQuery, page, limit);
        ListApiResponse<Recipe> response = new ListApiResponse<>(HttpStatus.OK, "User Recipes", userRecipes);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/")
    public ResponseEntity<ApiResponse<UserUpdateDTO>> updateUser(@AuthenticationPrincipal User user, @RequestBody UserUpdateDTO data) {
        User existingUser = this.userService.existUser(user);
        if( existingUser == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        UserUpdateDTO updateData = this.userService.updateUser(user, data);
        return  ResponseEntity.ok(new ApiResponse<UserUpdateDTO>(HttpStatus.OK, "updated data from user",updateData));
    }

    @DeleteMapping("/ingredient")
    public ResponseEntity<Void> deleteIngredients(@AuthenticationPrincipal User user, @RequestBody List<String> ingredientIds) {
        User existingUser = userService.existUser(user);
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        userService.deleteIngredients(existingUser, ingredientIds);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/recipe")
    public ResponseEntity<ApiResponse<RecipeDTO>> addRecipeToUser(@AuthenticationPrincipal User user, @RequestBody RecipeDTO data) {
        User existingUser = this.userService.existUser(user);
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        RecipeDTO recipe = this.userService.addRecipeToUser(user, data);
        return ResponseEntity.ok(new ApiResponse<RecipeDTO>(HttpStatus.OK, "Recipe added to user", recipe));
    }

    @DeleteMapping("/recipe/{recipeId}")
    public ResponseEntity<Void> deleteRecipeFromUser(@AuthenticationPrincipal User user, @PathVariable String recipeId) {
        User existingUser = userService.existUser(user);
        if (existingUser == null) return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        userService.deleteRecipeFromUser(user, recipeId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/nv/")
    public ResponseEntity<ApiResponse<NutritionalValuesUser>> addNutrientValuesFromRecipe(@AuthenticationPrincipal User user, @RequestBody NutritionalValuesUserDoubleDTO data) {

        System.out.println(data);
        User existingUser = this.userService.existUser(user);
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        NutritionalValuesUser updatedNv = this.userService.addNutritionalValuesInUser(existingUser, data);
        return ResponseEntity.ok(new ApiResponse<NutritionalValuesUser>(HttpStatus.OK, "Nv updated with nutrient values", updatedNv));
    }

    @PostMapping("/nutritionalValuesFromRecipe/{recipeId}")
    public ResponseEntity<ApiResponse<NutritionalValuesUser>> addNutritionalValuesFromRecipe(@AuthenticationPrincipal User user, @PathVariable String recipeId) {
        User existingUser = userService.existUser(user);
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        NutritionalValuesUser nutritionalValuesUser = userService.addNutritionalValuesFromRecipe(existingUser, recipeId);
        return ResponseEntity.ok(new ApiResponse<NutritionalValuesUser>(HttpStatus.OK, "Nutritional values added", nutritionalValuesUser));
    }

    @PostMapping("/checkAndRemoveIngredients/{recipeId}")
    public ResponseEntity<Map<String, Object>> checkAndRemoveIngredients(@AuthenticationPrincipal User user, @PathVariable String recipeId) {
        User existingUser = userService.existUser(user);
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return userService.checkAndRemoveIngredients(existingUser, recipeId);
    }

    @PostMapping("/addToShoppingList")
    public ResponseEntity<Void> addToShoppingList(@AuthenticationPrincipal User user, @RequestBody List<UserIngredient> ingredients) {
        User existingUser = userService.existUser(user);
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        userService.addToShoppingList(existingUser, ingredients);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/moveIngredients")
    public ResponseEntity<Void> moveIngredients(@AuthenticationPrincipal User user, @RequestBody List<UserIngredient> ingredientsToMove) {
        User existingUser = userService.existUser(user);
        if (existingUser == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        userService.moveIngredientsFromShoppingListToIngredients(existingUser, ingredientsToMove);
        return ResponseEntity.ok().build();
    }
}
