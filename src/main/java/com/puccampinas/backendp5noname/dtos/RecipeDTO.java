package com.puccampinas.backendp5noname.dtos;

import com.fasterxml.jackson.annotation.JsonGetter;
import lombok.*;

import java.util.List;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class RecipeDTO {
    private String name;
    private List<String> ingredients;
    private List<String> preparationMethod;
    private double preparationTime;
}

