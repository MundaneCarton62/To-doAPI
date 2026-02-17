package com.example.To_doAPI.dto;

import jakarta.validation.constraints.NotBlank;

public class CreateTaskRequest {

    @NotBlank
    String title;

    @NotBlank
    String description;

    public CreateTaskRequest(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @SuppressWarnings("unused")
    public CreateTaskRequest() {
    }
    @SuppressWarnings("unused")
    public String getTitle() {
        return title;
    }
    @SuppressWarnings("unused")
    public void setTitle(String title) {
        this.title = title;
    }
    @SuppressWarnings("unused")
    public String getDescription() {
        return description;
    }
    @SuppressWarnings("unused")
    public void setDescription(String description) {
        this.description = description;
    }
}
