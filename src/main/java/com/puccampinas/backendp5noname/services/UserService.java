package com.puccampinas.backendp5noname.services;


import com.puccampinas.backendp5noname.domain.Ingredient;
import com.puccampinas.backendp5noname.domain.NutritionalValues;
import com.puccampinas.backendp5noname.domain.Recipe;
import com.puccampinas.backendp5noname.domain.User;
import com.puccampinas.backendp5noname.domain.vo.IngredientVO;
import com.puccampinas.backendp5noname.dtos.IngredientIDDTO;
import com.puccampinas.backendp5noname.dtos.NutritionalValuesStringDTO;
import com.puccampinas.backendp5noname.dtos.RecipeDTO;
import com.puccampinas.backendp5noname.dtos.UserUpdateDTO;
import com.puccampinas.backendp5noname.repositories.RefreshTokenRepository;
import com.puccampinas.backendp5noname.repositories.UserRepository;
import com.puccampinas.backendp5noname.services.auth.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {


    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

    @Autowired
    private TokenService tokenService;

    @Autowired
    private IngredientService ingredientService;

    @Autowired
    private RecipeService recipeService;

    @Autowired
    private NutritionalValuesService nutritionalValuesService;





    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByLogin(username).orElseThrow(() -> new UsernameNotFoundException("username not found") );
    }

    public User findById(String id) {
        return userRepository.findById(id).orElseThrow(() -> new UsernameNotFoundException("user id not found")  );
    }


    public User existUser(User user) {
        return userRepository.findByLogin(user.getLogin())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    public List<IngredientVO> addIngredientsToUser(User user, List<IngredientVO> ingredientsVO) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<String> ingredientIds = ingredientsVO.stream()
                .map(IngredientVO::getId)
                .collect(Collectors.toList());

        List<Ingredient> ingredients = ingredientService.allIngredientsById(ingredientIds);

        existingUser.getIngredients().addAll(ingredients);

        userRepository.save(existingUser);

        return ingredientsVO;
    }

    public void deleteRecipeFromUser(User user, String recipeId) {
        List<Recipe> userRecipes = user.getRecipes();
        userRecipes.removeIf(recipe -> recipe.getId().equals(recipeId));
        userRepository.save(user);
    }
    public RecipeDTO addRecipeToUser(User user, RecipeDTO data) {
        Recipe recipe = new Recipe(data);
        recipeService.addRecipe(recipe);
        user.getRecipes().add(recipe);
        userRepository.save(user);
        return data;
    }

    public List<Ingredient> ingredientsFromUser(User user) {
        return user.getIngredients();
    }

    public List<Recipe> recipesFromUser(User user) {
        return user.getRecipes();
    }



    public void deleteIngredientFromUser(User user, String ingredientId) throws ChangeSetPersister.NotFoundException {


        List<Ingredient> userIngredients = user.getIngredients();
        boolean removed = userIngredients.removeIf(ingredient -> ingredient.getId().equals(ingredientId));

        if (!removed) {
            throw new ChangeSetPersister.NotFoundException();
        }

        userRepository.save(user);
    }

    public UserUpdateDTO updateUser(User user, UserUpdateDTO userUpdateDTO) {
        user.setLogin(userUpdateDTO.login());
        user.setDocument(userUpdateDTO.document());
        user.setFullName(userUpdateDTO.fullName());
        user.setWeight(userUpdateDTO.weight());
        this.userRepository.save(user);
        return userUpdateDTO;
    }

    public void deleteIngredientsFromUser(User user, List<IngredientIDDTO> ingredientIds)  {
        List<Ingredient> userIngredients = user.getIngredients();


        ingredientIds.stream()
                .map(IngredientIDDTO::id)
                .forEach(ingredientId -> {
                    boolean removed = userIngredients.removeIf(ingredient -> ingredient.getId().equals(ingredientId));
                    if (!removed) {
                        try {
                            throw new ChangeSetPersister.NotFoundException();
                        } catch (ChangeSetPersister.NotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                });

        userRepository.save(user);
    }

    public boolean isExistingEmailFromUser(String email, User user){
        Optional<User> userQuery = this.userRepository.findByLogin(email);
        return userQuery.isPresent() && !userQuery.get().getId().equals(user.getId());

    }



    public NutritionalValues addNutritionalValuesOnRecipe(Recipe recipe, NutritionalValuesStringDTO data) {
        NutritionalValues nutritionalValues = this.nutritionalValuesService.saveNutritionalValues(data);
        recipe.setNutritionalValues(nutritionalValues);
        recipeService.saveRecipe(recipe);
        return nutritionalValues;
    }

}
