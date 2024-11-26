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

    //private final EmailService emailService;


    @Autowired // IMPORTANT
    public StudentService(StudentRepository studentRepository, PasswordEncoder passwordEncoder/* , EmailService emailService*/) {
        this.studentRepository = studentRepository;
        this.passwordEncoder = passwordEncoder;
        //this.emailService = emailService;
    
    }

    public List<Student> getAll() {
        return this.studentRepository.findAll(); 
    }

    public Optional<Student> getStudentById(Long id) {
        return this.studentRepository.findById(id);
    }

    public Optional<Student> getByEmail(String email) {
        return this.studentRepository.findByEmail(email);
    }

    public Student create(Student StudentDetails) {

        //emailService.sendEmail("inscription@school.com", StudentDetails.getEmail(), "Compte", "Un compte a été créé à votre nom. utilisé cette adresse mail et votre date de naissance en mot de passe pour vous connecter.");

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
                //emailService.sendEmail("inscription@school.com", updatedStudent.getEmail(), "Compte", "Votre compte a été desactivé.");
            }
            
            if(!updatedStudent.getConfirm() && StudentDetails.getConfirm()){
                //emailService.sendEmail("inscription@school.com", updatedStudent.getEmail(), "Compte", "Votre compte a été activé.");
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
