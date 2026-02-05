package com.example.To_doAPI.dto;

public class TaskResponse {


    Long id;

    String title;

    String description;

    @SuppressWarnings("unused")
    public TaskResponse(Long id, String title, String description) {
        this.id = id;
        this.title = title;
        this.description = description;
    }
    @SuppressWarnings("unused")
    public Long getId() {
        return id;
    }
    @SuppressWarnings("unused")
    public void setId(Long id) {
        this.id = id;
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
