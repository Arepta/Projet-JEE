package com.example.edu.model;

import jakarta.persistence.*;

import java.lang.reflect.Field;

@Entity
@Table(name = "evaluations")
public class Evaluations {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(name = "min_score", nullable = false, columnDefinition = "INT DEFAULT 0")
    private int minScore;

    @Column(name = "max_score", nullable = false, columnDefinition = "INT DEFAULT 20")
    private int maxScore;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 10")
    private int score;

    @ManyToOne
    @JoinColumn(
        name = "course",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_evaluations_course")
    )
    private Courses course;

    @ManyToOne
    @JoinColumn(
        name = "student",
        nullable = false,
        foreignKey = @ForeignKey(name = "fk_evaluations_student")
    )
    private Student student;

    public Evaluations() {
    }

    public Evaluations(String name, int minScore, int maxScore, int score, Courses course, Student student) {
        this.name = name;
        this.minScore = minScore;
        this.maxScore = maxScore;
        this.score = score;
        this.course = course;
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

    public int getMinScore() {
        return minScore;
    }

    public void setMinScore(int minScore) {
        this.minScore = minScore;
    }

    public int getMaxScore() {
        return maxScore;
    }

    public void setMaxScore(int maxScore) {
        this.maxScore = maxScore;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
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
    public String toString() {
        StringBuilder sb = new StringBuilder("{");
        for (Field field : this.getClass().getDeclaredFields()) {
            try {
                field.setAccessible(true);
                sb.append("\"")
                    .append(field.getName())
                    .append("\": ")
                    .append(field.get(this) instanceof String ? "\"" + field.get(this) + "\"" : field.get(this))
                    .append(", ");
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return sb.substring(0, sb.length() - 2) + "}";
    }
}
