package com.puccampinas.backendp5noname.domain;


import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Recipe {
    private String name;
    private List<String> ingredients;
    private List<Double> ingredientQuantities;
    private List<String> preparationMethod;
    private double preparationTime;
}

