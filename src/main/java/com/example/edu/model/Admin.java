package com.example.edu.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;

@Entity
@Table(name = "admin")
public class Admin {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 255, unique = true)
    private String email;

    @Column(nullable = false, length = 100)
    private String password;

    // Constructeur sans argument requis par JPA
    public Admin() {
    }

    // Constructeur avec arguments pour la création facile d'instances
    public Admin(String email, String password) {
        this.email = email;
        this.password = password;
    }

    // Getters et Setters
    public int getId() {
        return id;
    }

    // disbaled for security
    // public void setId(Long id) {
    //     this.id = id;
    // }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
