package com.puccampinas.backendp5noname.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NonNull;

import java.util.List;

public record UserUpdateDTO(String fullName, Double height, Double weight, List<String> diets, List<String> allergies, List<String> intolerances) {
}
