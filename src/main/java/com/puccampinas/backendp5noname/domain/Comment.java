package com.puccampinas.backendp5noname.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
@Document(collection="comments")
public class Comment {
    @Id
    private String id;
    private String content;
    private String createdBy;
    private LocalDateTime createdAt;


    public Comment(String content, String createdBy) {
        this.content = content;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now();
    }
}