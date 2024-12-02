package com.example.edu.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.edu.model.Classes;
import com.example.edu.model.Room;
import com.example.edu.model.Schedule;
import com.example.edu.model.Student;
import com.example.edu.model.Teacher;
import com.example.edu.repository.ScheduleRepository;

@Service
public class ScheduleService {
    
    // Repository dependency for accessing schedule data
    private final ScheduleRepository scheduleRepository;

    // Constructor injection for ScheduleRepository
    @Autowired // IMPORTANT
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    // Method to get all schedules
    public List<Schedule> getAll() {
        return this.scheduleRepository.findAll();
    }

    // Method to get all schedules for a specific teacher
    public List<Schedule> getAllForTeacher(Teacher teacher) {
        return this.scheduleRepository.findByTeacherId(teacher.getId());
    }

    // Method to get all schedules for a specific student
    public List<Schedule> getAllForStudent(Student student) {
        return this.scheduleRepository.findByClassId(student.getStudentClass().getId());
    }

    // Method to check if a teacher is available within a specific time frame
    public boolean isTeacherAvailable(Teacher teacher, LocalDateTime start, LocalDateTime end) {
        return this.scheduleRepository.countTeacherUseBetween(teacher.getId(), start, end) == 0; 
    }

    // Method to check if a room is available within a specific time frame
    public boolean isRoomAvailable(Room room, LocalDateTime start, LocalDateTime end) {
        return this.scheduleRepository.countRoomUseBetween(room.getId(), start, end) == 0; 
    }

    // Method to check if a class is available within a specific time frame
    public boolean isClassAvailable(Classes classes, LocalDateTime start, LocalDateTime end) {
        return this.scheduleRepository.countClassBetween(classes.getId(), start, end) == 0; 
    }

    // Method to check if a teacher is available within a specific time frame, excluding a given schedule ID
    public boolean isTeacherAvailableId(Teacher teacher, LocalDateTime start, LocalDateTime end, Long id) {
        return this.scheduleRepository.countTeacherUseBetweenId(teacher.getId(), start, end, id) == 0; 
    }

    // Method to check if a room is available within a specific time frame, excluding a given schedule ID
    public boolean isRoomAvailableId(Room room, LocalDateTime start, LocalDateTime end, Long id) {
        return this.scheduleRepository.countRoomUseBetweenId(room.getId(), start, end, id) == 0; 
    }

    // Method to check if a class is available within a specific time frame, excluding a given schedule ID
    public boolean isClassAvailableId(Classes classes, LocalDateTime start, LocalDateTime end, Long id) {
        return this.scheduleRepository.countClassBetweenId(classes.getId(), start, end, id) == 0; 
    }

    // Method to get a schedule by its ID
    public Optional<Schedule> getById(Long id) {
        return this.scheduleRepository.findById(id); 
    }

    // Method to create a new schedule
    public Schedule create(Schedule details) {
        return this.scheduleRepository.save(details); 
    }

    // Method to update an existing schedule
    public Schedule update(Schedule details) {
        Optional<Schedule> optional = this.scheduleRepository.findById(details.getId());

        if (optional.isPresent()) {
            Schedule updated = optional.get();

            // Update schedule details
            updated.setCourse(details.getCourse());
            updated.setClasses(details.getClasses());
            updated.setTeacher(details.getTeacher());
            updated.setRoom(details.getRoom());
            updated.setStart(details.getStart());
            updated.setEnd(details.getEnd());

            return this.scheduleRepository.save(updated);
        }

        return null;
    }

    // Method to delete a schedule by its ID
    public void delete(Long id) {
        this.scheduleRepository.deleteById(id);
    }
}
