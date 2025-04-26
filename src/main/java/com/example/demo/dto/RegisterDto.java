package com.example.demo.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public class RegisterDto {
    @NotBlank
    private String username;

    @NotBlank
    @Email
    private String email;

    @NotBlank
    private String password;

    public @NotBlank String getUsername() {
        return username;
    }

    public @NotBlank @Email String getEmail() {
        return email;
    }

    public @NotBlank String getPassword() {
        return password;
    }
}
