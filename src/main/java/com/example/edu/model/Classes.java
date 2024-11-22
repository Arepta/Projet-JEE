package com.example.edu.model;

import java.lang.reflect.Field;
import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "class")
public class Classes {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(length = 100)
    private String name;
    
    @ManyToOne
    @JoinColumn(name = "program", foreignKey = @ForeignKey(name = "fk_class_program"), 
        referencedColumnName = "id", nullable = false)
    private Program program;

    @ManyToOne
    @JoinColumn(name = "level", foreignKey = @ForeignKey(name = "fk_class_level"), 
                referencedColumnName = "id", nullable = true)
    private ClassLevel level; // New relationship for 'level'
    
    // Constructors
    
    public Classes() {
    }
    
    public Classes(Long id, String name, Program program, ClassLevel level) {
        this.id = id;
        this.name = name;
        this.program = program;
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
    
    public Program getProgram() {
        return program;
    }
    
    public void setProgram(Program program) {
        this.program = program;
    }

    public ClassLevel getLevel() {
        return level;
    }

    public void setLevel(ClassLevel level) {
        this.level = level;
    }
    
    @Override
    public String toString(){
        String JSON = "";
        Class<?> clazz = this.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            try{
                field.setAccessible(true);
                if(field.get(this) instanceof Program){
                    JSON = JSON.concat("\"" + field.getName().toLowerCase() + "\":" + ((Program)field.get(this)).getId() + ",");
                }
                else if(field.get(this) == null || (!(field.get(this) instanceof String) && !(field.get(this) instanceof LocalDate)) ){
                    JSON = JSON.concat("\"" + field.getName().toLowerCase() + "\":" + field.get(this) + ",");
                }
                else{
                    JSON = JSON.concat("\"" + field.getName().toLowerCase() + "\": \"" + field.get(this) + "\",");
                }
                
            } 
            catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return "{"+JSON.substring(0, JSON.length()-1)+"}";
    }
}
