package com.puccampinas.backendp5noname.dtos;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.beans.factory.annotation.Qualifier;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Data
public class ErrorMessage {
    private int statusCode;
    @JsonFormat(pattern="dd/MM/yyyy 'as' HH:mm:ss")
    private LocalDateTime timestamp;
    private String message;
    private String path;

    public ErrorMessage(int statusCode, String message, String path) {
        this.statusCode = statusCode;
        this.timestamp =  LocalDateTime.now();
        this.message = message;
        this.path = path;
    }

}