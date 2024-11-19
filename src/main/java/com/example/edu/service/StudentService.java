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

        if(StudentDetails.getStudentClass() != null){

            if(StudentDetails.getStudentClass().getProgram() == null){
                StudentDetails.setLevel(null);
            }
            else{
                StudentDetails.setLevel(StudentDetails.getStudentClass().getProgram().getLevel());
            }
        }
        
        return this.studentRepository.save(StudentDetails);  //build in
    }

    public Student updateStudent(Student StudentDetails) {
        Optional<Student> optionalStudent = this.studentRepository.findById(StudentDetails.getId());

        if (optionalStudent.isPresent()) {
            Student updatedStudent = optionalStudent.get();

            if(StudentDetails.getStudentClass() != null){

                if(StudentDetails.getStudentClass().getProgram() == null){
                    StudentDetails.setLevel(null);
                }
                else{
                    StudentDetails.setLevel(StudentDetails.getStudentClass().getProgram().getLevel());
                }
            }

            updatedStudent.setEmail(StudentDetails.getEmail());
            updatedStudent.setName(StudentDetails.getName());
            updatedStudent.setSurname(StudentDetails.getSurname());
            updatedStudent.setDateOfBirth(StudentDetails.getDateOfBirth());
            updatedStudent.setLevel(StudentDetails.getLevel());
            updatedStudent.setStudentClass(StudentDetails.getStudentClass());
            updatedStudent.setConfirm(StudentDetails.getConfirm());

            return this.studentRepository.save(updatedStudent);
        }

        return null;
    }

    public void deleteStudent(Long id) {
        this.studentRepository.deleteById(id);
    }
}
