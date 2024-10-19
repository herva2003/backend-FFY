package com.puccampinas.backendp5noname.domain;

import com.puccampinas.backendp5noname.dtos.ReviewDTO;
import com.puccampinas.backendp5noname.dtos.TopicDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "reviews")
public class Review {

    @Id
    private String id;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private Integer rating;

    public Review() {
        this.createdAt = LocalDateTime.now();
    }

    public Review(ReviewDTO data) {
        this.title = data.title();
        this.description = data.description();
        this.createdAt = LocalDateTime.now();
        this.rating = data.rating();
    }

    // Getters e Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}