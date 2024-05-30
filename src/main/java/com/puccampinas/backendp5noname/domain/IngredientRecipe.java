package com.puccampinas.backendp5noname.domain;


import com.puccampinas.backendp5noname.dtos.IngredientRecipeDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection="ingredients-recipe")
public class IngredientRecipe {
    @Id
    private String id;
    private String name;
    private Double quantity;


    public IngredientRecipe(IngredientRecipeDTO ingredient) {
        this.name = ingredient.name();
        this.quantity = ingredient.quantity();
    }
}
