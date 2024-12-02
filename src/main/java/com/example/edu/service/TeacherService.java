package com.example.edu.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.edu.model.Teacher;
import com.example.edu.repository.TeacherRepository;

@Service
public class TeacherService {

    // Repository and password encoder dependencies for managing teacher data
    private final TeacherRepository teacherRepository;
    private PasswordEncoder passwordEncoder;

    // Constructor injection for TeacherRepository and PasswordEncoder
    @Autowired
    public TeacherService(TeacherRepository teacherRepository, PasswordEncoder passwordEncoder) {
        this.teacherRepository = teacherRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Method to retrieve all teachers
    public List<Teacher> getAllTeachers() {
        return this.teacherRepository.findAll();
    }

    // Method to retrieve a teacher by its ID
    public Optional<Teacher> getTeachersById(Long id) {
        return this.teacherRepository.findById(id);
    }

    // Method to retrieve a mapping of teacher IDs with their names
    public Map<Long, String> getAllIdxName() {
        List<Teacher> teacherList = this.teacherRepository.findAll();
        Map<Long, String> result = new HashMap<>();
        for (Teacher teacher : teacherList) {
            result.put(teacher.getId(), teacher.getSurname() + " " + teacher.getName());
        }
        return result;
    }

    // Method to retrieve a teacher by email
    public Optional<Teacher> getByEmail(String email) {
        return this.teacherRepository.findByEmail(email);
    }

    // Method to create a new teacher with hashed password
    public Teacher createTeacher(Teacher teacherDetails) {
        teacherDetails.setPassword(passwordEncoder.encode(teacherDetails.getPassword()));
        return this.teacherRepository.save(teacherDetails);
    }

    // Method to update an existing teacher
    public Teacher updateTeacher(Teacher teacherDetails) {
        Optional<Teacher> optionalTeacher = this.teacherRepository.findById(teacherDetails.getId());

        if (optionalTeacher.isPresent()) {
            Teacher updatedTeacher = optionalTeacher.get();
            updatedTeacher.setEmail(teacherDetails.getEmail());
            updatedTeacher.setName(teacherDetails.getName());
            updatedTeacher.setSurname(teacherDetails.getSurname());
            updatedTeacher.setField(teacherDetails.getField());

            return this.teacherRepository.save(updatedTeacher);
        }

        return null;
    }

    // Method to delete a teacher by its ID
    public void deleteTeacher(Long id) {
        this.teacherRepository.deleteById(id);
    }
}
