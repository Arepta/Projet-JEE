package com.example.edu.model;


import java.time.LocalDateTime;
import java.lang.reflect.Field;

import jakarta.persistence.*;

@Entity
@Table(name = "schedule")
@IdClass(ScheduleId.class)
public class Schedule {

    @Id
    @ManyToOne
    @JoinColumn(
        name = "course",
        foreignKey = @ForeignKey(name = "fk_schedule_course"),
        referencedColumnName = "id",
        nullable = false
    )
    private Courses course;

    @Id
    @ManyToOne
    @JoinColumn(
        name = "class",
        foreignKey = @ForeignKey(name = "fk_schedule_class"),
        referencedColumnName = "id",
        nullable = false
    )
    private ClassLevel classLevel;

    @Id
    @Column(nullable = false)
    private LocalDateTime start;

    @Column(nullable = false)
    private LocalDateTime end;

    @ManyToOne
    @JoinColumn(
        name = "room",
        foreignKey = @ForeignKey(name = "fk_schedule_room"),
        referencedColumnName = "id",
        nullable = false
    )
    private Room room;

    @ManyToOne
    @JoinColumn(
        name = "teacher",
        foreignKey = @ForeignKey(name = "fk_schedule_teacher"),
        referencedColumnName = "id",
        nullable = false
    )
    private Teacher teacher;

    // Constructors

    public Schedule() {
    }

    public Schedule(Courses course, ClassLevel classLevel, LocalDateTime start, LocalDateTime end, Room room, Teacher teacher) {
        this.course = course;
        this.classLevel = classLevel;
        this.start = start;
        this.end = end;
        this.room = room;
        this.teacher = teacher;
    }

    // Getters and Setters

    public Courses getCourse() {
        return course;
    }

    public void setCourse(Courses course) {
        this.course = course;
    }

    public ClassLevel getClassLevel() {
        return classLevel;
    }

    public void setClassLevel(ClassLevel classLevel) {
        this.classLevel = classLevel;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    public LocalDateTime getEnd() {
        return end;
    }

    public void setEnd(LocalDateTime end) {
        this.end = end;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
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
