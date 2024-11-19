package com.example.edu.model;

import jakarta.persistence.*;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@Entity
@Table(name = "students")
public class Student {
    
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
    
    @Column(name = "dateofbirth", nullable = false)
    private LocalDate dateOfBirth;
    
    @ManyToOne
    @JoinColumn(name = "level", foreignKey = @ForeignKey(name = "fk_students_level"), 
        referencedColumnName = "id", nullable = true)
    private ClassLevel level;
    
    @ManyToOne
    @JoinColumn(name = "class", foreignKey = @ForeignKey(name = "fk_students_class"), 
        referencedColumnName = "id", nullable = true)
    private Classes studentClass;

    @Column(name = "confirm", nullable = false)
    private boolean confirm;
    
    // Constructors
    
    public Student() {
    }
    
    public Student(Long id, String email, String password, String name, String surname, ClassLevel level, String dateOfBirth, Classes studentClass) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.id = id;
        this.email = email;
        this.password = password;
        this.name = name;
        this.surname = surname;
        this.dateOfBirth =  LocalDate.parse(dateOfBirth, formatter);
        this.level = level;
        this.studentClass = studentClass;
        this.confirm = false;
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
    
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }
    
    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }
    
    public ClassLevel getLevel() {
        return level;
    }
    
    public void setLevel(ClassLevel level) {
        this.level = level;
    }
    
    public Classes getStudentClass() {
        return studentClass;
    }
    
    public void setStudentClass(Classes studentClass) {
        this.studentClass = studentClass;
    }

    public boolean getConfirm() {
        return confirm;
    }
    
    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
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
