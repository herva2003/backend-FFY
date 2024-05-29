package com.puccampinas.backendp5noname.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonGetter;
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
    private List<String> ingredients;
    private List<String> preparationMethod;
    private int preparationTime;
    @JsonFormat(pattern="dd/MM/yyyy 'as' HH:mm:ss")
    private LocalDateTime generatedAt;
}

