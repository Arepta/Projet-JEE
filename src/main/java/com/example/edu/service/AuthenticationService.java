package com.example.edu.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.edu.model.Admin;
import com.example.edu.model.Student;
import com.example.edu.model.Teacher;
import com.example.edu.repository.AdminRepository;
import com.example.edu.repository.StudentRepository;
import com.example.edu.repository.TeacherRepository;

@Service
public class AuthenticationService implements UserDetailsService {

    // Repositories for admin, student, and teacher data
    private final AdminRepository adminRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;

    // Constructor injection of all required repositories
    public AuthenticationService(AdminRepository adminRepository, StudentRepository studentRepository, TeacherRepository teacherRepository) {
        this.adminRepository = adminRepository;
        this.studentRepository = studentRepository;
        this.teacherRepository = teacherRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Check if email belongs to an admin
        Optional<Admin> admin = adminRepository.findByEmail(email);
        if (admin.isPresent()) {
            return User.builder()
                .username(admin.get().getEmail())
                .password(admin.get().getPassword()) // Hashed password
                .roles("ADMIN") // Assign Admin role
                .build();
        }

        // Check if email belongs to a teacher
        Optional<Teacher> teacher = teacherRepository.findByEmail(email);
        if (teacher.isPresent()) {
            return User.builder()
                .username(teacher.get().getEmail())
                .password(teacher.get().getPassword()) // Hashed password
                .roles("TEACHER") // Assign Teacher role
                .build();
        }

        // Check if email belongs to a student with confirmed status
        Optional<Student> student = studentRepository.findByEmail(email);
        if (student.isPresent() && student.get().getConfirm()) {
            return User.builder()
                .username(student.get().getEmail())
                .password(student.get().getPassword()) // Hashed password
                .roles("STUDENT") // Assign Student role
                .build();
        }

        // Throw exception if user not found
        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}
