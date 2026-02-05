package com.example.To_doAPI.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "Tasks")
public class Task {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    @NotBlank
    String title;

    @NotBlank
    String description;

    @ManyToOne(fetch = FetchType.LAZY)
            @JoinColumn(name = "user_id", nullable = false)
    User user;

    /*
    Use a many-to-one relationship
    Join column like user_id
    */


    public Task(){}

    public Task(Long id, String title, String description, User user) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.user = user;
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
    @SuppressWarnings("unused")
    public User getUser() {
        return user;
    }
    @SuppressWarnings("unused")
    public void setUser(User user) {
        this.user = user;
    }
}
