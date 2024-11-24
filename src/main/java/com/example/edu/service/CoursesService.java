package com.example.edu.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.edu.model.Courses;
import com.example.edu.repository.CoursesReposiroty;

@Service
public class CoursesService {
    private final CoursesReposiroty coursesRepository;

    @Autowired // IMPORTANT
    public CoursesService(CoursesReposiroty coursesRepository) {
        this.coursesRepository = coursesRepository;
    
    }

    public List<Courses> getAll() {
        return this.coursesRepository.findAll(); //build in
    }

    public Map<Long, String> getAllIdxName() {

        List<Courses> b = this.coursesRepository.findAll();
        Map<Long, String> r = new HashMap<>();
        for(Courses cl : b){
            r.put(cl.getId(), cl.getName());
        }
        return r; //build in
    }

    public Map<Long, List<Long>> getAllTeachers() {

        List<Courses> b = this.coursesRepository.findAll();
        Map<Long, List<Long>> r = new HashMap<>();
        for(Courses cl : b){
            r.put(cl.getId(), this.coursesRepository.getTeacherForId(cl.getId()));
        }
        return r; //build in
    }

    public Optional<Courses> getById(Long id) {
        return this.coursesRepository.findById(id);  //build in
    }

    public Courses create(Courses details) {
        return this.coursesRepository.save(details);  //build in
    }

    public Courses update(Courses details) {
        Optional<Courses> optionalCourses = this.coursesRepository.findById(details.getId());

        if (optionalCourses.isPresent()) {
            Courses updatedCourses = optionalCourses.get();

            updatedCourses.setName(details.getName());
            updatedCourses.setLevel(details.getLevel());
            updatedCourses.setField(details.getField());

            return this.coursesRepository.save(updatedCourses);
        }

        return null;
    }

    public void delete(Long id) {
        this.coursesRepository.deleteById(id);
    }
}
