package com.example.To_doAPI.dto;

public class LoginResponse {

    String token;

    public LoginResponse(String token) {
        this.token = token;
    }

    @SuppressWarnings("unused")
    public String getToken() {
        return token;
    }
}
