package com.puccampinas.backendp5noname.dtos;

import org.springframework.http.HttpStatus;

import java.util.List;

public record ListApiResponse<T>(HttpStatus status, String message, List<T> data) {}
