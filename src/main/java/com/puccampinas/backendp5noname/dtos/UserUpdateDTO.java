package com.puccampinas.backendp5noname.dtos;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.NonNull;

public record UserUpdateDTO(String login, String fullName, Double height,Double weight) {
}
