package com.example.democafeclientsystem.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserDTO {
    private int id;
    private String firstName;
    private String email;
    private String password;
    private String role;

    public UserDTO(String firstName, String email, String password, String role) {
        this.firstName = firstName;
        this.email = email;
        this.password = password;
        this.role = role;
    }
}
