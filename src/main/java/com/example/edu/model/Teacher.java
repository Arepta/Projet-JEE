package com.example.edu.model;

import jakarta.persistence.*;

import java.lang.reflect.Field;
import java.time.LocalDate;

@Entity
@Table(name = "teachers")
public class Teacher {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true, length = 100)
    private String email;
    
    @Column(nullable = false, length = 100)
    private String password;
    
    @Column(length = 100)
    private String name;
    
    @Column(length = 100)
    private String surname;
    
    @ManyToOne
    @JoinColumn(name = "field", foreignKey = @ForeignKey(name = "fk_teachers_field"), 
        referencedColumnName = "id", nullable = true)
    private Field_teacher field;

    
    public Teacher() {
    }
    
    public Teacher(Long id, String email, String password, String name, String surname, Field_teacher field) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.field = field;
    }
    
    // Getters and Setters
    
    public Long getId() {
        return id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
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
    
    public String getName() {
        return name;
    }
    
    public void setName(String name) {
        this.name = name;
    }
    
    public String getSurname() {
        return surname;
    }
    
    public void setSurname(String surname) {
        this.surname = surname;
    }

    public Field_teacher getField(){
        return field;
    }

    public void setField(Field_teacher field){
        this.field = field;
    }

    @Override
    public String toString(){
        String JSON = "";
        Class<?> clazz = this.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            try{
                field.setAccessible(true);
                if(field.get(this) instanceof ClassLevel){
                    JSON = JSON.concat("\"" + field.getName().toLowerCase() + "\":" + ((ClassLevel)field.get(this)).getId() + ",");
                }
                else if(field.get(this) instanceof Classes){
                    JSON = JSON.concat("\"" + field.getName().toLowerCase() + "\":" + ((Classes)field.get(this)).getId() + ",");
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
