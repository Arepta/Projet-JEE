package com.example.edu.model;

import jakarta.persistence.*;

@Entity
@Table(name = "class")
public class ClassM {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    @Column(length = 100)
    private String title;
    
    @Column(nullable = false)
    private int size = 30;
    
    @ManyToOne
    @JoinColumn(name = "program", foreignKey = @ForeignKey(name = "fk_class_program"), 
        referencedColumnName = "id", nullable = false)
    private Program program;
    
    // Constructors
    
    public ClassM() {
    }
    
    public ClassM(String title, int size) {
        this.id = null;
        this.title = title;
        this.size = size;
    }
    
    // Getters et Setters
    
    public int getId() {
        return id;
    }
    
    // disabled for security
    // public void setId(Long id) {
    //     this.id = id;
    // }
    
    public String getTitle() {
        return title;
    }
    
    public void setTitle(String title) {
        this.title = title;
    }
    
    public int getSize() {
        return size;
    }
    
    public void setSize(int size) {
        this.size = size;
    }
    
    public Program getProgram() {
        return program;
    }
    
    public void setProgram(Program program) {
        this.program = program;
    }
}
