package com.example.To_doAPI.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Entity
@Table(name = "Users")
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    Long id;

    @NotBlank
    String name;

    @Column(unique = true, nullable = false)
    @NotBlank
    String email;

    @NotBlank
    String password;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    List<Task> tasks;


    public User(){}

    public User(Long id, String name, String email, String password) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
    }

    //UserDetails methods
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.emptyList(); // no roles yet
    }

    @Override
    public String getUsername() {
        return email; // IMPORTANT
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Long getId() {
        return id;
    }

    @SuppressWarnings("unused")
    public String getName() {
        return name;
    }
    @SuppressWarnings("unused")
    public void setName(String name) {
        this.name = name;
    }
    @SuppressWarnings("unused")
    public String getEmail() {
        return email;
    }
    @SuppressWarnings("unused")
    public void setEmail(String email) {
        this.email = email;
    }
    @SuppressWarnings("unused")
    public String getPassword() {
        return password;
    }
    @SuppressWarnings("unused")
    public void setPassword(String password) {
        this.password = password;
    }

    @SuppressWarnings("unused")
    public List<Task> getTasks() {
        return tasks;
    }

    @SuppressWarnings("unused")
    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
    }
}
