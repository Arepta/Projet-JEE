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

    private final TeacherRepository TeacherRepository;

    private PasswordEncoder passwordEncoder;


    @Autowired // IMPORTANT
    public TeacherService(TeacherRepository TeacherRepository, PasswordEncoder passwordEncoder) {
        this.TeacherRepository = TeacherRepository;
        this.passwordEncoder = passwordEncoder;
    
    }

    public List<Teacher> getAllTeachers() {
        return this.TeacherRepository.findAll(); //build in
    }

    public Optional<Teacher> getTeachersById(Long id) {
        return this.TeacherRepository.findById(id);  //build in
    }

    public Map<Long, String> getAllIdxName() {

        List<Teacher> b = this.TeacherRepository.findAll();
        Map<Long, String> r = new HashMap<>();
        for(Teacher cl : b){
            r.put(cl.getId(), cl.getSurname() + " " + cl.getName());
        }
        return r; //build in
    }

    public Optional<Teacher> getByEmail(String email) {
        return this.TeacherRepository.findByEmail(email);  //build in
    }

    public Teacher createTeacher(Teacher TeacherDetails) {
        TeacherDetails.setPassword(passwordEncoder.encode(TeacherDetails.getPassword()));
        
        return this.TeacherRepository.save(TeacherDetails);  //build in
    }

    public Teacher updateTeacher(Teacher TeacherDetails) {
        Optional<Teacher> optionalTeacher = this.TeacherRepository.findById(TeacherDetails.getId());

        if (optionalTeacher.isPresent()) {
            Teacher updatedTeacher = optionalTeacher.get();

            updatedTeacher.setEmail(TeacherDetails.getEmail());
            updatedTeacher.setName(TeacherDetails.getName());
            updatedTeacher.setSurname(TeacherDetails.getSurname());
            updatedTeacher.setField(TeacherDetails.getField());

            return this.TeacherRepository.save(updatedTeacher);
        }

        return null;
    }

    public void deleteTeacher(Long id) {
        this.TeacherRepository.deleteById(id);
    }
}
