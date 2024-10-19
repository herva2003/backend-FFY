package com.puccampinas.backendp5noname.domain;

import com.puccampinas.backendp5noname.dtos.TopicDTO;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "topics")
public class Topic {

    @Id
    private String id;
    private String title;
    private String description;
    private String createdBy;
    private LocalDateTime createdAt;

    public Topic() {
        this.createdAt = LocalDateTime.now();
    }

    public Topic(TopicDTO data) {
        this.title = data.title();
        this.description = data.description();
        this.createdBy = data.createdBy();
        this.createdAt = LocalDateTime.now();
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

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
}









