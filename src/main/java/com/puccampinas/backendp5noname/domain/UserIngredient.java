package com.puccampinas.backendp5noname.domain;

import lombok.*;

@Setter
@Getter
@Data
public class UserIngredient {
    private String ingredientId;
    private String quantity;

    public UserIngredient() {}

    public UserIngredient(String ingredientId, String quantity) {
        this.ingredientId = ingredientId;
        this.quantity = quantity;
    }

}