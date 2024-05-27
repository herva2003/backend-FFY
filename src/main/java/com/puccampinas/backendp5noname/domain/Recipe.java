package com.puccampinas.backendp5noname.domain;


import com.puccampinas.backendp5noname.dtos.RecipeDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

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
    private List<String> ingredients;
    private List<String> preparationMethod;
    private double preparationTime;


    public Recipe(RecipeDTO data) {
        this.name = data.getName();
        this.ingredients = data.getIngredients();
        this.preparationMethod = data.getPreparationMethod();
        this.preparationTime = data.getPreparationTime();
    }
}

