package com.example.edu.model;

import java.lang.reflect.Field;

import jakarta.persistence.*;

@Entity
@Table(name = "programs")
public class Program {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 100)
    private String name;
    
    // Constructors
    
    public Program() {
    }
    
    public Program(Long id, String name) {
        this.id = id;
        this.name = name;
    }
    
    // Getters et Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public String toString() {
        String JSON = "";
        Class<?> clazz = this.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                if (field.get(this) == null || !(field.get(this) instanceof String)) {
                    JSON = JSON.concat("\"" + field.getName().toLowerCase() + "\":" + field.get(this) + ",");
                } else {
                    JSON = JSON.concat("\"" + field.getName().toLowerCase() + "\": \"" + field.get(this) + "\",");
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return "{" + JSON.substring(0, JSON.length() - 1) + "}";
    }
}
