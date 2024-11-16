package com.example.edu.service;

import java.util.List;
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

    public List<Student> getAllStudents() {
        return this.studentRepository.findAll(); //build in
    }

    public Optional<Student> getStudentsById(Long id) {
        return this.studentRepository.findById(id);  //build in
    }

    public Optional<Student> getStudentsByEmail(String email) {
        return this.studentRepository.findByEmail(email);  //build in
    }

    public Student createStudent(Student StudentDetails) {
        StudentDetails.setPassword(passwordEncoder.encode(StudentDetails.getPassword()));
        return this.studentRepository.save(StudentDetails);  //build in
    }

    public Student updateStudent(Long id, Student StudentDetails) {
        Optional<Student> optionalStudent = this.studentRepository.findById(id);

        if (optionalStudent.isPresent()) {
            Student updatedStudent = optionalStudent.get();

            updatedStudent.setEmail(StudentDetails.getEmail());
            updatedStudent.setPassword(passwordEncoder.encode(StudentDetails.getPassword()));
            updatedStudent.setName(StudentDetails.getName());
            updatedStudent.setSurname(StudentDetails.getSurname());
            updatedStudent.setDateOfBirth(StudentDetails.getDateOfBirth());
            updatedStudent.setLevel(StudentDetails.getLevel());
            updatedStudent.setStudentClass(StudentDetails.getStudentClass());

            return this.studentRepository.save(updatedStudent);
        }

        return null;
    }

    public void deleteStudent(Long id) {
        this.studentRepository.deleteById(id);
    }
}
