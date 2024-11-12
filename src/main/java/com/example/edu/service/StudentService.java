package com.example.edu.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.edu.model.Student;
import com.example.edu.repository.StudentRepository;

@Service
public class StudentService {

    private final StudentRepository studentRepository;

    @Autowired // IMPORTANT
    public StudentService(StudentRepository studentRepository) {
        this.studentRepository = studentRepository;
    }

    public List<Student> getAllStudents() {
        return this.studentRepository.findAll(); //build in
    }

    public Optional<Student> getStudentsById(Long id) {
        return this.studentRepository.findById(id);  //build in
    }

    public Optional<Student> getStudentsByEmail(String email) {
        return this.studentRepository.findStudentByEmail(email);  //build in
    }

    public Optional<Student> getStudentsByCredential(String email, String password) {
        return this.studentRepository.findStudentByCredential(email, password);  //build in
    }

    public Student createStudent(Student Student) {
        return this.studentRepository.save(Student);  //build in
    }

    public Student updateStudent(Long id, Student StudentDetails) {
        Optional<Student> optionalStudent = this.studentRepository.findById(id);

        if (optionalStudent.isPresent()) {
            Student Student = optionalStudent.get();

            Student.setEmail(StudentDetails.getEmail());
            Student.setPassword(StudentDetails.getPassword());
            Student.setName(StudentDetails.getName());
            Student.setSurname(StudentDetails.getSurname());
            Student.setDateOfBirth(StudentDetails.getDateOfBirth());
            Student.setLevel(StudentDetails.getLevel());
            Student.setStudentClass(StudentDetails.getStudentClass());

            return this.studentRepository.save(Student);
        }

        return null;
    }

    public void deleteStudent(Long id) {
        this.studentRepository.deleteById(id);
    }
}
