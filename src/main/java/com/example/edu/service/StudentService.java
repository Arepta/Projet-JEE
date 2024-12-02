package com.example.edu.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.edu.model.Student;
import com.example.edu.repository.StudentRepository;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    private PasswordEncoder passwordEncoder;



    @Autowired // IMPORTANT
    public StudentService(StudentRepository studentRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    
    }

    public List<Student> getAll() {
        return this.studentRepository.findAll(); 
    }

    public Optional<Student> getById(Long id) {
        return this.studentRepository.findById(id);
    }

    public Optional<Student> getByEmail(String email) {
        return this.studentRepository.findByEmail(email);
    }

    public Map<Long, String> getAllIdxName() {

        List<Student> b = this.studentRepository.findAll();
        Map<Long, String> r = new HashMap<>();
        for(Student cl : b){
            r.put(cl.getId(), cl.getName());
        }
        return r;
    }

    public Map<Long, String> getAllIdxNameSurname() {

        List<Student> b = this.studentRepository.findAll();
        Map<Long, String> r = new HashMap<>();
        for(Student cl : b){
            r.put(cl.getId(), cl.getName()+" "+cl.getSurname());
        }
        return r;
    }

    public Student create(Student StudentDetails) {

       

        StudentDetails.setPassword(passwordEncoder.encode(StudentDetails.getPassword()));
        
        return this.studentRepository.save(StudentDetails);
    }

    public Student update(Student StudentDetails) {
        Optional<Student> optionalStudent = this.studentRepository.findById(StudentDetails.getId());

        if (optionalStudent.isPresent()) {
            Student updatedStudent = optionalStudent.get();
            
            updatedStudent.setEmail(StudentDetails.getEmail());
            updatedStudent.setName(StudentDetails.getName());
            updatedStudent.setSurname(StudentDetails.getSurname());
            updatedStudent.setDateOfBirth(StudentDetails.getDateOfBirth());
            updatedStudent.setLevel(StudentDetails.getLevel());
            updatedStudent.setStudentClass(StudentDetails.getStudentClass());

            if(updatedStudent.getConfirm() && !StudentDetails.getConfirm()){
            }
            
            if(!updatedStudent.getConfirm() && StudentDetails.getConfirm()){
            }

            updatedStudent.setConfirm(StudentDetails.getConfirm());

            return this.studentRepository.save(updatedStudent);
        }

        return null;
    }

    public void delete(Long id) {
        this.studentRepository.deleteById(id);
    }
}
