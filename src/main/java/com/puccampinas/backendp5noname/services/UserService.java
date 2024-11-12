package com.puccampinas.backendp5noname.services;


import com.puccampinas.backendp5noname.ResourceNotFoundException;
import com.puccampinas.backendp5noname.domain.*;
import com.puccampinas.backendp5noname.domain.vo.IngredientVO;
import com.puccampinas.backendp5noname.dtos.*;
import com.puccampinas.backendp5noname.repositories.RefreshTokenRepository;
import com.puccampinas.backendp5noname.repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RefreshTokenRepository refreshTokenRepository;

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

        List<UserIngredient> userIngredients = ingredientsVO.stream()
                .map(vo -> new UserIngredient(vo.getId(), vo.getQuantity() != null ? vo.getQuantity() : "0"))
                .collect(Collectors.toList());

        for (UserIngredient userIngredient : userIngredients) {
            boolean exists = existingUser.getIngredients().stream()
                    .anyMatch(i -> i.getIngredientId().equals(userIngredient.getIngredientId()));
            if (!exists) {
                existingUser.getIngredients().add(userIngredient);
            } else {
                existingUser.getIngredients().stream()
                        .filter(i -> i.getIngredientId().equals(userIngredient.getIngredientId()))
                        .forEach(i -> i.setQuantity(userIngredient.getQuantity()));
            }
        }

        userRepository.save(existingUser);

        return ingredientsVO;
    }

    public void deleteRecipeFromUser(User user, String recipeId) {
        user.getRecipes().removeIf(id -> id.equals(recipeId));
        userRepository.save(user);
    }

    public NutritionalValuesUser addNutritionalValuesFromRecipe(User user, String recipeId) {
        Recipe recipe = recipeService.findById(recipeId);
        if (recipe == null || recipe.getNutritionalValues() == null) {
            throw new RuntimeException("Recipe or nutritional values not found");
        }

        NutritionalValuesDoubleDTO nutritionalValuesDoubleDTO = convertToDTO(recipe.getNutritionalValues());
        NutritionalValuesUser nutritionalValuesUser = nutritionalValuesUserService.saveNutritionalValuesUserFromRecipe(nutritionalValuesDoubleDTO);

        user.getNutritionalValuesUser().add(nutritionalValuesUser);
        userRepository.save(user);

        return nutritionalValuesUser;
    }

    private NutritionalValuesDoubleDTO convertToDTO(NutritionalValues nutritionalValues) {
        return new NutritionalValuesDoubleDTO(
                nutritionalValues.getEnergy_kcal(),
                nutritionalValues.getProtein_g(),
                nutritionalValues.getSaturated_fats_g(),
                nutritionalValues.getFat_g(),
                nutritionalValues.getCarb_g(),
                nutritionalValues.getFiber_g(),
                nutritionalValues.getSugar_g(),
                nutritionalValues.getCalcium_mg(),
                nutritionalValues.getIron_mg(),
                nutritionalValues.getMagnesium_mg(),
                nutritionalValues.getPhosphorus_mg(),
                nutritionalValues.getPotassium_mg(),
                nutritionalValues.getSodium_mg(),
                nutritionalValues.getZinc_mg(),
                nutritionalValues.getCopper_mcg(),
                nutritionalValues.getManganese_mg(),
                nutritionalValues.getSelenium_mcg(),
                nutritionalValues.getVitC_mg(),
                nutritionalValues.getThiamin_mg(),
                nutritionalValues.getRiboflavin_mg(),
                nutritionalValues.getNiacin_mg(),
                nutritionalValues.getVitB6_mg(),
                nutritionalValues.getFolate_mcg(),
                nutritionalValues.getVitB12_mcg(),
                nutritionalValues.getVitA_mcg(),
                nutritionalValues.getVitE_mg(),
                nutritionalValues.getVitD2_mcg()
        );
    }

    @Transactional
    public RecipeDTO addRecipeToUser(User user, RecipeDTO data) {
        Recipe recipe = createRecipe(data);
        addRecipeIdToUser(user, recipe.getId());

        return data;
    }

    private Recipe createRecipe(RecipeDTO data) {
        List<IngredientRecipe> ingredients = this.ingredientRecipeService.convertAndSave(data.getIngredients());

        NutritionalValues nutritionalValues = null;
        if (data.getNutritionalValues() != null) {
            nutritionalValues = this.nutritionalValuesService.saveNutritionalValues(data.getNutritionalValues());
        }

        Recipe recipe = new Recipe(data, ingredients, nutritionalValues);
        recipeService.addRecipe(recipe);

        return recipe;
    }


    public void addRecipeIdToUser(User user, String recipeId) {
        user.getRecipes().add(recipeId);
        userRepository.save(user);
    }

    public List<UserIngredient> ingredientsFromUser(User user) {
        return user.getIngredients();
    }

    public List<NutritionalValuesUser> nutritionalValuesFromUser(User user) {
        return user.getNutritionalValuesUser();
    }

    public List<Recipe> recipesFromUser(User user) {
        List<String> recipeIds = user.getRecipes();
        return recipeIds.stream()
                .map(recipeId -> recipeService.findById(recipeId))
                .collect(Collectors.toList());
    }

    public void deleteIngredients(User user, List<String> ingredientIds) {
        List<UserIngredient> ingredients = user.getIngredients();

        ingredients.removeIf(ingredient -> ingredientIds.contains(ingredient.getIngredientId()));

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
        List<UserIngredient> userIngredients = user.getIngredients();


        ingredientIds.stream()
                .map(IngredientIDDTO::ingredientId)
                .forEach(ingredientId -> {
                    boolean removed = userIngredients.removeIf(ingredient -> ingredient.getIngredientId().equals(ingredientId));
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

    public List<IngredientVO> addIngredientsToShoppList(User user, List<IngredientVO> ingredientsVO) {
        User existingUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<UserIngredient> userIngredients = ingredientsVO.stream()
                .map(vo -> new UserIngredient(vo.getId(), vo.getQuantity() != null ? vo.getQuantity() : "0"))
                .collect(Collectors.toList());

        for (UserIngredient userIngredient : userIngredients) {
            boolean exists = existingUser.getShoppingList().stream()
                    .anyMatch(i -> i.getIngredientId().equals(userIngredient.getIngredientId()));
            if (!exists) {
                existingUser.getShoppingList().add(userIngredient);
            } else {
                existingUser.getShoppingList().stream()
                        .filter(i -> i.getIngredientId().equals(userIngredient.getIngredientId()))
                        .forEach(i -> i.setQuantity(userIngredient.getQuantity()));
            }
        }

        userRepository.save(existingUser);

        return ingredientsVO;
    }

    public List<UserIngredient> ingredientsFromShoppList(User user) {
        return user.getShoppingList();
    }

    public void deleteIngredientsFromShoppList(User user, List<IngredientIDDTO> ingredientIds)  {
        List<UserIngredient> userIngredients = user.getShoppingList();
        log.info("userIngredients: {}", userIngredients);

        ingredientIds.stream()
                .map(IngredientIDDTO::ingredientId)
                .forEach(ingredientId -> {
                    boolean removed = userIngredients.removeIf(ingredient -> ingredient.getIngredientId().equals(ingredientId));
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

    public void addReviewIdToUser(String userId, String reviewId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id " + userId));
        user.addReviewId(reviewId);
        userRepository.save(user);
    }

    public ResponseEntity<Map<String, Object>> checkAndRemoveIngredients(User user, String recipeId) {
        Recipe recipe = recipeService.findById(recipeId);
        if (recipe == null || recipe.getIngredients() == null) {
            throw new RuntimeException("Recipe or ingredients not found");
        }

        List<String> missingIngredients = new ArrayList<>();
        List<UserIngredient> userIngredients = user.getIngredients();

        for (IngredientRecipe ingredient : recipe.getIngredients()) {
            Optional<UserIngredient> userIngredientOpt = userIngredients.stream()
                    .filter(ui -> ui.getIngredientId().equals(ingredient.getId()))
                    .findFirst();

            if (userIngredientOpt.isPresent()) {
                UserIngredient userIngredient = userIngredientOpt.get();
                double currentQuantity = Double.parseDouble(userIngredient.getQuantity());
                double requiredQuantity = Double.parseDouble(ingredient.getQuantity());

                if (currentQuantity >= requiredQuantity) {
                    userIngredient.setQuantity(String.valueOf(currentQuantity - requiredQuantity));
                } else {
                    missingIngredients.add(ingredient.getId());
                }
            } else {
                missingIngredients.add(ingredient.getId());
            }
        }

        if (!missingIngredients.isEmpty()) {
            Map<String, Object> response = new HashMap<>();
            response.put("message", "Some ingredients are missing.");
            response.put("missingIngredients", missingIngredients);
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }

        userRepository.save(user);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    public void addToShoppingList(User user, List<UserIngredient> ingredients) {
        List<UserIngredient> shoppingList = user.getShoppingList();

        for (UserIngredient ingredient : ingredients) {
            boolean existsInShoppingList = shoppingList.stream()
                    .anyMatch(sl -> sl.getIngredientId().equals(ingredient.getIngredientId()));
            if (!existsInShoppingList) {
                shoppingList.add(ingredient);
            }
        }

        userRepository.save(user);
    }

    public void moveIngredientsFromShoppingListToIngredients(User user, List<UserIngredient> ingredientsToMove) {
        List<UserIngredient> shoppingList = user.getShoppingList();
        List<UserIngredient> ingredients = user.getIngredients();

        System.out.println("Shopping List: " + shoppingList);
        System.out.println("Ingredients to Move: " + ingredientsToMove);

        for (UserIngredient ingredientToMove : ingredientsToMove) {
            Optional<UserIngredient> optionalShoppingIngredient = shoppingList.stream()
                    .filter(ingredient -> ingredient.getIngredientId().equals(ingredientToMove.getIngredientId()))
                    .findFirst();

            if (optionalShoppingIngredient.isPresent()) {
                UserIngredient shoppingIngredient = optionalShoppingIngredient.get();

                shoppingList.remove(shoppingIngredient);

                Optional<UserIngredient> optionalUserIngredient = ingredients.stream()
                        .filter(ingredient -> ingredient.getIngredientId().equals(ingredientToMove.getIngredientId()))
                        .findFirst();

                if (optionalUserIngredient.isPresent()) {
                    UserIngredient userIngredient = optionalUserIngredient.get();
                    double currentQuantity = Double.parseDouble(userIngredient.getQuantity());
                    double addedQuantity = Double.parseDouble(ingredientToMove.getQuantity());
                    userIngredient.setQuantity(String.valueOf(currentQuantity + addedQuantity));
                } else {
                    ingredients.add(new UserIngredient(ingredientToMove.getIngredientId(), ingredientToMove.getQuantity()));
                }
            } else {
                System.out.println("Ingredient not found in shopping list: " + ingredientToMove.getIngredientId());
                throw new RuntimeException("Ingredient not found in shopping list");
            }
        }

        userRepository.save(user);
    }
}