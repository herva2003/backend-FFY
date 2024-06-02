package com.puccampinas.backendp5noname.services;


import com.puccampinas.backendp5noname.domain.*;
import com.puccampinas.backendp5noname.domain.vo.IngredientVO;
import com.puccampinas.backendp5noname.dtos.*;
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
    private NutritionalValuesUserService nutritionalValuesUserService;

    @Autowired
    private NutritionalValuesService nutritionalValuesService;

    @Autowired
    private IngredientRecipeService ingredientRecipeService;





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
        List<IngredientRecipe> ingredients = this.ingredientRecipeService.convertAndSave(data.getIngredients());

        NutritionalValues nutritionalValues = null;
        NutritionalValuesUser nv = null;

        if (data.getNutritionalValues() != null) {
            nutritionalValues = this.nutritionalValuesService.saveNutritionalValues(data.getNutritionalValues());
            nv = this.nutritionalValuesUserService.saveNutritionalValuesUserFromRecipe(data.getNutritionalValues());
        }

        Recipe recipe = new Recipe(data, ingredients, nutritionalValues);
        recipeService.addRecipe(recipe);

        if (nv != null) {
            List<NutritionalValuesUser> list = user.getNutritionalValuesUser();
            list.add(nv);
            user.setNutritionalValuesUser(list);
        }

        user.getRecipes().add(recipe);
        userRepository.save(user);

        return data;
    }

    public List<Ingredient> ingredientsFromUser(User user) {
        return user.getIngredients();
    }

    public List<NutritionalValuesUser> nutritionalValuesFromUser(User user) {
        return user.getNutritionalValuesUser();
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
        user.setHeight(userUpdateDTO.height());
        user.setFullName(userUpdateDTO.fullName());
        user.setWeight(userUpdateDTO.weight());
        user.setDiets(userUpdateDTO.diets());
        user.setIntolerances(userUpdateDTO.intolerances());
        user.setAllergies(userUpdateDTO.allergies());
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



    public NutritionalValuesUser addNutritionalValuesInUser(User user, NutritionalValuesUserDoubleDTO data) {
       NutritionalValuesUser nutritionalValues = this.nutritionalValuesUserService.saveNutritionalValuesUser(data);
       List<NutritionalValuesUser> list = user.getNutritionalValuesUser();
       list.add(nutritionalValues);
        user.setNutritionalValuesUser(list);
        this.userRepository.save(user);
       return nutritionalValues;
    }

}
