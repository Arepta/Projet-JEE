package com.example.edu.model;

import jakarta.persistence.*;

@Entity
@Table(name = "programs")
public class Program {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 100)
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "level", foreignKey = @ForeignKey(name = "fk_programs_level"), 
        referencedColumnName = "id", nullable = true)
    private ClassLevel level;
    
    // Constructors
    
    public Program() {
    }
    
    public Program(Long id, String name, ClassLevel level) {
        this.id = id;
        this.name = name;
        this.level = level;
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
    
    public ClassLevel getLevel() {
        return level;
    }
    
    public void setLevel(ClassLevel level) {
        this.level = level;
    }
}
