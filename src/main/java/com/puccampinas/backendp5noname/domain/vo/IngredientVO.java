package com.puccampinas.backendp5noname.domain.vo;

import com.puccampinas.backendp5noname.domain.Ingredient;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Data
public class IngredientVO {
    String name;
    String id;


    public IngredientVO(Ingredient ingredient){
        this.id = ingredient.getId();
        this.name  = ingredient.getDescrip();
    }
}
