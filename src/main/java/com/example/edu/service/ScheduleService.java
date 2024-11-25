package com.example.edu.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.edu.model.Room;
import com.example.edu.model.Schedule;
import com.example.edu.model.Teacher;
import com.example.edu.repository.ScheduleRepository;

@Service
public class ScheduleService {
    private final ScheduleRepository scheduleRepository;

    @Autowired // IMPORTANT
    public ScheduleService(ScheduleRepository scheduleRepository) {
        this.scheduleRepository = scheduleRepository;
    }

    public List<Schedule> getAll() {
        return this.scheduleRepository.findAll(); //build in
    }

    public boolean isTeacherAvailable(Teacher teacher, LocalDateTime start, LocalDateTime end){
        return this.scheduleRepository.countTeacherUseBetween(teacher.getId(), start, end) == 0; 
    }

    public boolean isRoomAvailable(Room room, LocalDateTime start, LocalDateTime end){
        return this.scheduleRepository.countTeacherUseBetween(room.getId(), start, end) == 0; 
    }

    public Optional<Schedule> getById(Long id) {
        return this.scheduleRepository.findById(id);  //build in
    }

    public Schedule create(Schedule details) {
        return this.scheduleRepository.save(details);  //build in
    }

    public Schedule update(Schedule details) {
        Optional<Schedule> optional = this.scheduleRepository.findById(details.getId());

        if (optional.isPresent()) {
            Schedule updated = optional.get();

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

    public void delete(Long id) {
        this.scheduleRepository.deleteById(id);
    }
}
