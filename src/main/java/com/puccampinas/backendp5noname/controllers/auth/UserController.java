package com.puccampinas.backendp5noname.controllers.auth;

import com.puccampinas.backendp5noname.domain.Ingredient;
import com.puccampinas.backendp5noname.domain.User;
import com.puccampinas.backendp5noname.domain.vo.IngredientVO;
import com.puccampinas.backendp5noname.dtos.ApiResponse;
import com.puccampinas.backendp5noname.dtos.ListApiResponse;
import com.puccampinas.backendp5noname.services.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user")
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

    @DeleteMapping("/ingredient/{id}")
    public ResponseEntity<Void> deleteIngredient(@AuthenticationPrincipal User user, @PathVariable String id) {
        this.userService.deleteIngredientFromUser(user, id);
        return ResponseEntity.noContent().build();
    }

}
