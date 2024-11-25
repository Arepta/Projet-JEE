package com.example.edu.model;

import jakarta.persistence.*;

import java.lang.reflect.Field;
import java.time.LocalDateTime;

@Entity
@Table(name = "schedule")
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "room", nullable = false, referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_schedule_room"))
    private Room room;

    @ManyToOne
    @JoinColumn(name = "class", nullable = false, referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_schedule_class"))
    private Classes classes;

    @ManyToOne
    @JoinColumn(name = "teacher", nullable = false,  referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_schedule_teacher"))
    private Teacher teacher;

    @ManyToOne
    @JoinColumn(name = "course", nullable = false,  referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_schedule_course"))
    private Courses course;

    @Column(name = "start", nullable = false)
    private LocalDateTime start;

    @Column(name = "end", nullable = false)
    private LocalDateTime end;

    // Constructors
    public Schedule() {}

    public Schedule(Long id, Room room, Classes classes, Teacher teacher, Courses course, LocalDateTime start, LocalDateTime end) {
        this.id = id;
        this.room = room;
        this.classes = classes;
        this.teacher = teacher;
        this.course = course;
        this.start = start;
        this.end = end;
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Room getRoom() {
        return room;
    }

    public void setRoom(Room room) {
        this.room = room;
    }

    public Classes getClasses() {
        return classes;
    }

    public void setClasses(Classes classes) {
        this.classes = classes;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public Courses getCourse() {
        return course;
    }

    public void setCourse(Courses course) {
        this.course = course;
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
