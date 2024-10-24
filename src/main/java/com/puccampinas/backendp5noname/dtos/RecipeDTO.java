package com.puccampinas.backendp5noname.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
import com.puccampinas.backendp5noname.domain.IngredientRecipe;
import lombok.*;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class RecipeDTO {
    private String name;
    private List<IngredientRecipeDTO> ingredients;
    private List<String> preparationMethod;
    private int preparationTime;
    private NutritionalValuesDoubleDTO nutritionalValues;
    @JsonFormat(pattern="dd/MM/yyyy 'as' HH:mm:ss")
    private LocalDateTime generatedAt;
    private List<String> reviewsId;
    private int rating;
}

