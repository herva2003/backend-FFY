package com.puccampinas.backendp5noname.domain;


import com.fasterxml.jackson.annotation.JsonFormat;
import com.puccampinas.backendp5noname.dtos.RecipeDTO;
import java.util.Collection;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.DocumentReference;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection="recipes")
public class Recipe {
    @Id
    private String id;
    private String name;
    @DocumentReference
    private List<IngredientRecipe> ingredients;
    private List<String> preparationMethod;
    private int preparationTime;
    @JsonFormat(pattern="dd/MM/yyyy 'as' HH:mm:ss")
    private LocalDateTime createdAt;
    @DocumentReference
    private NutritionalValues nutritionalValues;
    private List<String> likes = new ArrayList<>();
    private List<Comment> comments = new ArrayList<>();
    private List<String> reviewsId = new ArrayList<>();

    public Recipe(RecipeDTO data,List<IngredientRecipe> ingredients, NutritionalValues nutritionalValues) {
        this.name = data.getName();
        this.ingredients =  ingredients;
        this.preparationMethod = data.getPreparationMethod();
        this.preparationTime = data.getPreparationTime();
        this.createdAt = LocalDateTime.now();
        this.nutritionalValues = nutritionalValues;
        this.reviewsId = new ArrayList<>();
    }

    public void addReviewId(String reviewId) {
        this.reviewsId.add(reviewId);
    }
}
