package com.example.edu.model;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

public class ScheduleId implements Serializable {

    private Long course;
    private Long classLevel;
    private LocalDateTime start;

    // Constructors

    public ScheduleId() {
    }

    public ScheduleId(Long course, Long classLevel, LocalDateTime start) {
        this.course = course;
        this.classLevel = classLevel;
        this.start = start;
    }

    // Getters and Setters

    public Long getCourse() {
        return course;
    }

    public void setCourse(Long course) {
        this.course = course;
    }

    public Long getClassLevel() {
        return classLevel;
    }

    public void setClassLevel(Long classLevel) {
        this.classLevel = classLevel;
    }

    public LocalDateTime getStart() {
        return start;
    }

    public void setStart(LocalDateTime start) {
        this.start = start;
    }

    // hashCode and equals

    @Override
    public int hashCode() {
        return Objects.hash(course, classLevel, start);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        ScheduleId that = (ScheduleId) obj;
        return Objects.equals(course, that.course) &&
               Objects.equals(classLevel, that.classLevel) &&
               Objects.equals(start, that.start);
    }
}
