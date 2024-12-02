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

    // Dependency for accessing student data
    private final StudentRepository studentRepository;

    // Password encoder for securely storing passwords
    private PasswordEncoder passwordEncoder;

    // Constructor to inject dependencies
    @Autowired // IMPORTANT
    public StudentService(StudentRepository studentRepository, PasswordEncoder passwordEncoder) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Method to get all students
    public List<Student> getAll() {
        return this.studentRepository.findAll();
    }

    // Method to get a student by their ID
    public Optional<Student> getById(Long id) {
        return this.studentRepository.findById(id);
    }

    // Method to get a student by their email
    public Optional<Student> getByEmail(String email) {
        return this.studentRepository.findByEmail(email);
    }

    // Method to get a mapping of student IDs to their names
    public Map<Long, String> getAllIdxName() {
        List<Student> b = this.studentRepository.findAll();
        Map<Long, String> r = new HashMap<>();
        for (Student cl : b) {
            r.put(cl.getId(), cl.getName());
        }
        return r;
    }

    // Method to get a mapping of student IDs to their names and surnames
    public Map<Long, String> getAllIdxNameSurname() {
        List<Student> b = this.studentRepository.findAll();
        Map<Long, String> r = new HashMap<>();
        for (Student cl : b) {
            r.put(cl.getId(), cl.getName() + " " + cl.getSurname());
        }
        return r;
    }

    // Method to create a new student
    public Student create(Student StudentDetails) {
        // Hash the password before saving
        StudentDetails.setPassword(passwordEncoder.encode(StudentDetails.getPassword()));
        return this.studentRepository.save(StudentDetails);
    }

    // Method to update an existing student's details
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

            // If confirmed status is changed, update it
            if (updatedStudent.getConfirm() && !StudentDetails.getConfirm()) {
                // Logic to handle change from confirmed to not confirmed (if needed)
            }

            if (!updatedStudent.getConfirm() && StudentDetails.getConfirm()) {
                // Logic to handle change from not confirmed to confirmed (if needed)
            }

            updatedStudent.setConfirm(StudentDetails.getConfirm());

            return this.studentRepository.save(updatedStudent);
        }

        return null;
    }

    // Method to delete a student by their ID
    public void delete(Long id) {
        this.studentRepository.deleteById(id);
    }
}
