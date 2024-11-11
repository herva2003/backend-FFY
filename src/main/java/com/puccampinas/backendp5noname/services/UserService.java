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

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

    public void deleteIngredientFromUser(User user, String ingredientId) throws ChangeSetPersister.NotFoundException {
        List<UserIngredient> userIngredients = user.getIngredients();
        boolean removed = userIngredients.removeIf(ingredient -> ingredient.getIngredientId().equals(ingredientId));

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
        List<UserIngredient> userIngredients = user.getIngredients();


        ingredientIds.stream()
                .map(IngredientIDDTO::id)
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

        ingredientIds.stream()
                .map(IngredientIDDTO::id)
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

    @Transactional
    public ResponseEntity<String> checkAndRemoveIngredients(User user, String recipeId) {
        Recipe recipe = recipeService.findById(recipeId);
        if (recipe == null || recipe.getIngredients() == null) {
            throw new RuntimeException("Recipe or ingredients not found");
        }

        List<String> missingIngredients = new ArrayList<>();
        List<UserIngredient> userIngredients = user.getIngredients();
        List<UserIngredient> shoppingList = user.getShoppingList();

        log.info("Starting ingredient check for recipe: {}", recipeId);

        for (IngredientRecipe ingredient : recipe.getIngredients()) {
            log.info("Checking ingredient: {}", ingredient.getId());
            Optional<UserIngredient> userIngredientOpt = userIngredients.stream()
                    .filter(ui -> ui.getIngredientId().equals(ingredient.getId()))
                    .findFirst();

            if (userIngredientOpt.isPresent()) {
                UserIngredient userIngredient = userIngredientOpt.get();
                double currentQuantity = Double.parseDouble(userIngredient.getQuantity());
                double requiredQuantity = Double.parseDouble(ingredient.getQuantity());

                log.info("Current quantity: {}, Required quantity: {}", currentQuantity, requiredQuantity);

                if (currentQuantity >= requiredQuantity) {
                    userIngredient.setQuantity(String.valueOf(currentQuantity - requiredQuantity));
                    log.info("Updated quantity of ingredient {}: {}", ingredient.getId(), userIngredient.getQuantity());
                } else {
                    log.info("Insufficient quantity for ingredient {}", ingredient.getId());
                    missingIngredients.add(ingredient.getId());
                }
            } else {
                log.info("Ingredient {} not found in user ingredients", ingredient.getId());
                missingIngredients.add(ingredient.getId());
            }
        }

        if (!missingIngredients.isEmpty()) {
            log.info("Missing ingredients: {}", missingIngredients);
            missingIngredients.forEach(missingId -> {
                boolean existsInShoppingList = shoppingList.stream()
                        .anyMatch(sl -> sl.getIngredientId().equals(missingId));
                if (!existsInShoppingList) {
                    shoppingList.add(new UserIngredient(missingId, "0"));
                }
            });
            userRepository.save(user);
            return new ResponseEntity<>("Some ingredients are missing. They have been added to your shopping list.", HttpStatus.BAD_REQUEST);
        }

        userRepository.save(user);
        return new ResponseEntity<>("Ingredients are sufficient and have been deducted.", HttpStatus.OK);
    }
}