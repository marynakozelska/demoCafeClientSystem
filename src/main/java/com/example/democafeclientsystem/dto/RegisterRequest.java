package com.example.democafeclientsystem.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RegisterRequest {
    @Pattern(regexp = "^[a-zA-Zа-яА-Я\\u0456\\u0457єґ\\u0406\\u0407ЄҐ]+$",
            message = "The name must consist of Latin or Cyrillic letters.")
    @NotNull
    private String firstName;

    @Email(regexp = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$",
            message = "Enter your e-mail in the correct format.")
    @NotNull
    private String email;

    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[._-]).{8,}$",
            message = "The password must be longer than 7 characters and must consist of Latin letters, numbers and signs: ., _, -")
    @NotNull
    private String password;
}
