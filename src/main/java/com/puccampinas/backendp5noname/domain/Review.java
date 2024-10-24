package com.puccampinas.backendp5noname.domain;

import com.puccampinas.backendp5noname.dtos.ReviewDTO;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Data
@Document(collection = "reviews")
public class Review {

    @Id
    private String id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private Integer rating;
    private String recipeId;

    public Review(ReviewDTO data) {
        this.title = data.title();
        this.description = data.description();
        this.createdAt = LocalDateTime.now();
        this.rating = data.rating();
        this.recipeId = data.recipeId();
    }
}