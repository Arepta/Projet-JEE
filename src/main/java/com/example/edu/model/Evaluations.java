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

    @Column(nullable = false, length = 100, unique = true)
    private String name;

    @Column(nullable = false, length = 100, unique = true)
    private int MinScore;

    @Column(nullable = false, length = 100, unique = true)
    private int MaxScore;

    @Column(nullable = false, length = 100, unique = true)
    private int Score;

    @ManyToOne
    @JoinColumn(name = "course", nullable = false,  referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_schedule_course"))
    private Courses course;

    @ManyToOne
    @JoinColumn(name = "student", nullable = false,  referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_schedule_course"))
    private Student student;

    // Constructeur sans argument requis par JPA
    public Evaluations() {
    }

    // Constructeur avec arguments pour la création facile d'instances
    public Evaluations(Long id, String name, int MaxScore, int MinScore, int Score, Courses course, Student student) {
        this.id = id;
        this.name = name;
        this.MaxScore = MaxScore;
        this.MinScore = MinScore;
        this.Score = Score;
        this.course = course;
        this.student = student;
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

    public int getMaxScore() {
        return MaxScore;
    }

    public void setMaxScore(int MaxScore) {
        this.MaxScore = MaxScore;
    }

    public int getMinScore() {
        return MinScore;
    }

    public void setMinScore(int MinScore) {
        this.MinScore = MinScore;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int Score) {
        this.Score = Score;
    }

    public Courses getCourse() {
        return course;
    }

    public void setCourse(Courses course) {
        this.course = course;
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
                if(field.get(this) instanceof Room){
                    JSON = JSON.concat("\"" + field.getName().toLowerCase() + "\":" + ((Room)field.get(this)).getId() + ",");
                }
                else if(field.get(this) instanceof Classes){
                    JSON = JSON.concat("\"class\":" + ((Classes)field.get(this)).getId() + ",");
                }
                else if(field.get(this) instanceof Teacher){
                    JSON = JSON.concat("\"" + field.getName().toLowerCase() + "\":" + ((Teacher)field.get(this)).getId() + ",");
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
