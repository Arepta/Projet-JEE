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

    // Repository dependency for managing courses data
    private final CoursesReposiroty coursesRepository;

    // Constructor injection of CoursesRepository
    @Autowired
    public CoursesService(CoursesReposiroty coursesRepository) {
        this.coursesRepository = coursesRepository;
    }

    // Method to retrieve all courses
    public List<Courses> getAll() {
        return this.coursesRepository.findAll();
    }

    // Method to retrieve a mapping of course IDs with their names
    public Map<Long, String> getAllIdxName() {
        List<Courses> coursesList = this.coursesRepository.findAll();
        Map<Long, String> result = new HashMap<>();
        for (Courses course : coursesList) {
            result.put(course.getId(), course.getName());
        }
        return result;
    }

    // Method to retrieve a mapping of course IDs with the list of teacher IDs
    public Map<Long, List<Long>> getAllTeachers() {
        List<Courses> coursesList = this.coursesRepository.findAll();
        Map<Long, List<Long>> result = new HashMap<>();
        for (Courses course : coursesList) {
            result.put(course.getId(), this.coursesRepository.getTeacherForId(course.getId()));
        }
        return result;
    }

    // Method to retrieve a course by its ID
    public Optional<Courses> getById(Long id) {
        return this.coursesRepository.findById(id);
    }

    // Method to create a new course
    public Courses create(Courses details) {
        return this.coursesRepository.save(details);
    }

    // Method to update an existing course
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

    // Method to delete a course by its ID
    public void delete(Long id) {
        this.coursesRepository.deleteById(id);
    }
}
