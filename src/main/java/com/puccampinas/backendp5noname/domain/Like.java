package com.puccampinas.backendp5noname.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Data
@Document(collection="likes")
public class Like {
    @Id
    private String userId;


    public Like(String userId) {
        this.userId = userId;
    }
}