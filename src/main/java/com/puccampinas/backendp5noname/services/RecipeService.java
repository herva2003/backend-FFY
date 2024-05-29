package com.puccampinas.backendp5noname.services;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.puccampinas.backendp5noname.domain.Recipe;
import com.puccampinas.backendp5noname.dtos.RecipeDTO;
import com.puccampinas.backendp5noname.dtos.RecipeInfoDTO;
import com.puccampinas.backendp5noname.repositories.RecipeRepository;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.openai.OpenAiChatClient;
import org.springframework.ai.openai.OpenAiChatOptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;

@Service
public class RecipeService {

    @Autowired
    private OpenAiChatClient chatClient;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RecipeRepository recipeRepository;



    public Recipe addRecipe(Recipe recipe) {
        return recipeRepository.save(recipe);
    }

    public RecipeDTO generateRecipe(RecipeInfoDTO data) throws JsonProcessingException {
        String content = "Você é um assistente de culinária profissional. Crie receitas usando apenas os ingredientes fornecidos. "
                + "Não adicione ingredientes extras. Não é obrigatório usar todos os ingredientes.\n"
                + "Crie 1 receita em inglês, considerando as seguintes informações:\n"
                + "- Ingredientes disponíveis: %s\n"
                + "- Restrições alimentares: \n"
                + "- Tipo de refeição: " + data.type() + "\n"
                + "- Observações: " + data.observation() + "\n"
                + "Apresente as receitas em formato JSON, sem comentários. Agrupe os objetos JSON de cada receita "
                + "em um objeto maior chamado 'recipes'.\n"
                + "Cada objeto de receita deve conter os campos:\n"
                + " - 'name': String\n"
                + " - 'ingredients': Lista de strings\n"
                + " - 'preparationMethod': Lista de strings\n"
                + " - 'preparationTime': Double\n"
                + "Os ingredientes devem ser listados com o nome exato fornecido e as quantidades "
                + "devem ser especificadas em números inteiros ou decimais, representando a quantidade em gramas, "
                + "partindo de uma base de 100 gramas para cada ingrediente.\n";

        ChatResponse response = chatClient.call(
                new Prompt(
                        content,
                        OpenAiChatOptions.builder()
                                .withModel("gpt-3.5-turbo")
                                .withTemperature(0.4F)
                                .build()
                ));



        // Obter o conteúdo da resposta JSON
        String jsonResponse = response.getResult().getOutput().getContent();

        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonNode = objectMapper.readTree(jsonResponse);

        String recipeContent = jsonNode.get("recipes").get(0).toString();

        RecipeDTO recipe = objectMapper.readValue(recipeContent, RecipeDTO.class);

        recipe.setGeneratedAt(LocalDateTime.now());

        return recipe;
    }



}
