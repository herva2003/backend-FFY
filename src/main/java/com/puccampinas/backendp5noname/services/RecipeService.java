package com.puccampinas.backendp5noname.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.puccampinas.backendp5noname.domain.Comment;
import com.puccampinas.backendp5noname.domain.Like;
import com.puccampinas.backendp5noname.domain.Recipe;
import com.puccampinas.backendp5noname.domain.User;
import com.puccampinas.backendp5noname.dtos.CommentDTO;
import com.puccampinas.backendp5noname.dtos.LikeDTO;
import com.puccampinas.backendp5noname.dtos.RecipeDTO;
import com.puccampinas.backendp5noname.dtos.RecipeInfoDTO;
import com.puccampinas.backendp5noname.ResourceNotFoundException;
import com.puccampinas.backendp5noname.repositories.RecipeRepository;
import com.puccampinas.backendp5noname.domain.*;
import com.puccampinas.backendp5noname.repositories.ReviewRepository;
import com.puccampinas.backendp5noname.repositories.UserRepository;
import org.bson.types.ObjectId;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RecipeService {

    @Autowired
    private OpenAiChatClient chatClient;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RecipeRepository recipeRepository;
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private UserRepository userRepository;

    public Recipe addRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public RecipeDTO generateRecipe(RecipeInfoDTO data, User user) throws JsonProcessingException {
        String content = String.format("Você é um assistente de culinária profissional. Crie receitas usando apenas os ingredientes fornecidos. "
                + "Não adicione ingredientes extras. Não é obrigatório usar todos os ingredientes.\n"
                + "Crie 1 receita em inglês, considerando as seguintes informações:\n"
                + "- Ingredientes disponíveis:" + user.getIngredients() + "\n"
                + "- Restrições alimentares:" + user.getDiets() + " " + user.getAllergies() +  " " + user.getIntolerances() + "\n"
                + "- Tipo de refeição: " + data.type() + "\n"
                + "- Observações: " + data.observation() + "\n"
                + "Apresente as receitas em formato JSON, sem comentários. Agrupe os objetos JSON de cada receita "
                + "em um objeto maior chamado 'recipes'.\n"
                + "Cada objeto de receita deve conter os campos:\n"
                + " - 'name': String\n"
                + " - 'ingredients': Lista de objetos, cada objeto contendo 'name': String e 'quantity': Double (em gramas)\n"
                + " - 'preparationMethod': Lista de strings\n"
                + " - 'preparationTime': Double (em minutos)\n"
                + " - 'nutritionalValues':      Double Energy_kcal,\n" +
                "                Double Protein_g,\n" +
                "                Double Saturated_fats_g,\n" +
                "                Double Fat_g,\n" +
                "                Double Carb_g,\n" +
                "                Double Fiber_g,\n" +
                "                Double Sugar_g,\n" +
                "                Double Calcium_mg,\n" +
                "                Double Iron_mg,\n" +
                "                Double Magnesium_mg,\n" +
                "                Double Phosphorus_mg,\n" +
                "                Double Potassium_mg,\n" +
                "                Double Sodium_mg,\n" +
                "                Double Zinc_mg,\n" +
                "                Double Copper_mcg,\n" +
                "                Double Manganese_mg,\n" +
                "                Double Selenium_mcg,\n" +
                "                Double VitC_mg,\n" +
                "                Double Thiamin_mg,\n" +
                "                Double Riboflavin_mg,\n" +
                "                Double Niacin_mg,\n" +
                "                Double VitB6_mg,\n" +
                "                Double Folate_mcg,\n" +
                "                Double VitB12_mcg,\n" +
                "                Double VitA_mcg,\n" +
                "                Double VitE_mg,\n" +
                "                Double VitD2_mcg\n"
                + "Os ingredientes devem ser listados com o nome exato fornecido e as quantidades "
                + "devem ser especificadas em números inteiros ou decimais, representando a quantidade em gramas, "
                + "partindo de uma base de 100 gramas para cada ingrediente.\n");

        ChatResponse response = chatClient.call(
                new Prompt(
                        content,
                        OpenAiChatOptions.builder()
                                .withModel("gpt-3.5-turbo")
                                .withTemperature(0.4F)
                                .build()
                )
        );

        // Obter o conteúdo da resposta JSON
        String jsonResponse = response.getResult().getOutput().getContent();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);

        String recipeContent = jsonNode.get("recipes").get(0).toString();

        RecipeDTO recipe = objectMapper.readValue(recipeContent, RecipeDTO.class);

        recipe.setGeneratedAt(LocalDateTime.now());

        return recipe;
    }

    public Recipe singleRecipe(String id) {
        return this.recipeRepository.findById(id).orElse(null);
    }

    public Recipe saveRecipe(Recipe recipe) {
        return this.recipeRepository.save(recipe);
    }

    public Recipe findRecipeById(String id){
        return this.recipeRepository.findById(id).orElse(null);
    }

    public List<Recipe> getAllRecipes(Pageable pageable) {
        return recipeRepository.findAll(pageable).getContent();
    }

    public Recipe getRecipeById(String recipeId) {
        return recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + recipeId));
    }

    public void addLike(LikeDTO likeDTO) {
        Recipe recipe = recipeRepository.findById(likeDTO.recipeId())
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + likeDTO.recipeId()));
        User user = userRepository.findById(likeDTO.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + likeDTO.userId()));
        recipe.getLikes().add(likeDTO.userId());
        user.getLikedRecipes().add(likeDTO.recipeId());
        recipeRepository.save(recipe);
        userRepository.save(user);
    }

    public void removeLike(LikeDTO likeDTO) {
        Recipe recipe = recipeRepository.findById(likeDTO.recipeId())
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id: " + likeDTO.recipeId()));
        User user = userRepository.findById(likeDTO.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + likeDTO.userId()));
        recipe.getLikes().remove(likeDTO.userId());
        user.getLikedRecipes().remove(likeDTO.recipeId());
        recipeRepository.save(recipe);
        userRepository.save(user);
    }

    public long countReviewsByRecipeId(String recipeId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id " + recipeId));
        return recipe.getReviewsId().size();
    }
    public Recipe findById(String id) {
        return recipeRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Recipe not found"));
    }

    public void addComment(String recipeId, CommentDTO comment) {
        Recipe recipe = getRecipeById(recipeId);
        recipe.getComments().add(new Comment(comment.content(), comment.id()));
        recipeRepository.save(recipe);
    }

    public void addReviewIdToRecipe(String recipeId, String reviewId) {
        Recipe recipe = recipeRepository.findById(recipeId)
                .orElseThrow(() -> new ResourceNotFoundException("Recipe not found with id " + recipeId));
        recipe.addReviewId(reviewId);
        recipeRepository.save(recipe);
    }
}