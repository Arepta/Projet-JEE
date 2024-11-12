package com.example.edu.model;

import jakarta.persistence.*;

@Entity
@Table(name = "class_level")
public class ClassLevel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    // Constructors

    public ClassLevel() {
    }

    public ClassLevel(String name) {
        this.id = null;
        this.name = name;
    }

    // Getters et Setters

    public int getId() {
        return id;
    }

    // disabled for security
    // public void setId(Long id) {
    //     this.id = id;
    // }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
