package com.example.To_doAPI.dto;

import org.hibernate.validator.constraints.NotBlank;

public class LoginRequest {

    @NotBlank
    String email;

    @NotBlank
    String password;

    @SuppressWarnings("unused")
    public LoginRequest() {
    }

    @SuppressWarnings("unused")
    public LoginRequest(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    @SuppressWarnings("unused")
    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    @SuppressWarnings("unused")
    public void setPassword(String password) {
        this.password = password;
    }
}
