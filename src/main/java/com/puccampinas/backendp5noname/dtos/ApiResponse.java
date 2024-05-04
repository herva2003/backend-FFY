package com.puccampinas.backendp5noname.dtos;

import org.springframework.http.HttpStatus;

public record ApiResponse<T>(HttpStatus status, String message, T data) {
}
