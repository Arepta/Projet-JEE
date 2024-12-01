package com.example.edu.model;

import jakarta.persistence.*;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

@Entity
@Table(name = "evaluations")
public class Evaluations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 10")
    private int score;

    @ManyToOne
    @JoinColumn(
        name = "student",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_evaluations_student")
    )
    private Student student;

    public Evaluations() {
    }

    public Evaluations(Long id, String name, int minScore, int maxScore, int score, Courses course, Student student) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.student = student;
    }

    // Getters and Setters
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

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }


    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }

    @Override
    public String toString(){
        String JSON = "";
        Class<?> clazz = this.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            try{
                field.setAccessible(true);
                if(field.get(this) instanceof Student){
                    JSON = JSON.concat("\"" + field.getName().toLowerCase() + "\":" + ((Student)field.get(this)).getId() + ",");
                }
                else if(field.get(this) instanceof Courses){
                    JSON = JSON.concat("\"" + field.getName().toLowerCase() + "\":" + ((Courses)field.get(this)).getId() + ",");
                }
                else if(field.get(this) == null || (!(field.get(this) instanceof String) && !(field.get(this) instanceof LocalDateTime)) ){
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
