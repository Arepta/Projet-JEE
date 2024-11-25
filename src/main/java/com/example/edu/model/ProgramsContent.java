package com.example.edu.model;

import jakarta.persistence.*;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.Objects;

@Entity
@Table(name = "programs_content")
public class ProgramsContent {

    @Embeddable
    public static class ProgramsContentId implements Serializable {
        private Long program;
        private Long course;

        public ProgramsContentId() {}

        public ProgramsContentId(Long program, Long course) {
            this.program = program;
            this.course = course;
        }

        public Long getProgram() {
            return program;
        }

        public void setProgram(Long program) {
            this.program = program;
        }

        public Long getCourse() {
            return course;
        }

        public void setCourse(Long course) {
            this.course = course;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ProgramsContentId that = (ProgramsContentId) o;
            return Objects.equals(program, that.program) && Objects.equals(course, that.course);
        }

        @Override
        public int hashCode() {
            return Objects.hash(program, course);
        }

        @Override
        public String toString() {
           
            return "\"id\":\"" + this.program + "x" + this.course + "\"";
        }
    }

    @EmbeddedId
    private ProgramsContentId id;

    @ManyToOne
    @MapsId("program")
    @JoinColumn(name = "program", nullable = false)
    private Program program;

    @ManyToOne
    @MapsId("course")
    @JoinColumn(name = "course", nullable = false)
    private Courses course;

    public ProgramsContent() {}

    public ProgramsContent(Program program, Courses course) {
        this.id = new ProgramsContentId(program.getId(), course.getId());
        this.program = program;
        this.course = course;
    }

    public ProgramsContentId getId() {
        return id;
    }

    public void setId(ProgramsContentId id) {
        this.id = id;
    }

    public Program getProgram() {
        return program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public Courses getCourse() {
        return course;
    }

    public void setCourse(Courses course) {
        this.course = course;
    }

    @Override
    public String toString() {
        String JSON = "";
        Class<?> clazz = this.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            try {
                field.setAccessible(true);

                if(field.get(this) instanceof Courses){
                    JSON = JSON.concat("\"" + field.getName().toLowerCase() + "\":" + ((Courses)field.get(this)).getId() + ",");
                }
                else if(field.get(this) instanceof Program){
                    JSON = JSON.concat("\"" + field.getName().toLowerCase() + "\":" + ((Program)field.get(this)).getId() + ",");
                } 
                else if(field.get(this) instanceof ProgramsContentId){
                    JSON = JSON.concat(field.get(this).toString()+",");
                } 
                
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }

        return "{" + JSON.substring(0, JSON.length() - 1) + "}";
    }
}
