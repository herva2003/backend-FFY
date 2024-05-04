package com.puccampinas.backendp5noname.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignupDTO {
    @NotBlank
    @Size(min = 3, max = 30)
    private String email;
    @NotBlank
    @Size(min = 6, max = 60)
    private String password;
    @NotBlank
    private String document;
    @NotBlank
    private String fullName;
    @NotBlank
    private String weight;
}