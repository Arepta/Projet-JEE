package com.example.edu.model;

import java.lang.reflect.Field;
import java.time.LocalDate;

import jakarta.persistence.*;

@Entity
@Table(name = "courses")
public class Courses {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @ManyToOne
    @JoinColumn(
        name = "level",
        foreignKey = @ForeignKey(name = "fk_courses_level"),
        referencedColumnName = "id",
        nullable = true
    )
    private ClassLevel level;

    @ManyToOne
    @JoinColumn(
        name = "field",
        foreignKey = @ForeignKey(name = "fk_courses_field"),
        referencedColumnName = "id",
        nullable = true
    )
    private FieldTeacher field;

    // Constructors

    public Courses() {
    }

    public Courses(Long id, String name, ClassLevel level, FieldTeacher field) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.field = field;
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

    public ClassLevel getLevel() {
        return level;
    }

    public void setLevel(ClassLevel level) {
        this.level = level;
    }

    public FieldTeacher getField() {
        return field;
    }

    public void setField(FieldTeacher field) {
        this.field = field;
    }

    @Override
    public String toString() {
        String JSON = "";
        Class<?> clazz = this.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            try {
                field.setAccessible(true);
                if (field.get(this) == null ||
                    (!(field.get(this) instanceof String) && !(field.get(this) instanceof LocalDate))) {
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
