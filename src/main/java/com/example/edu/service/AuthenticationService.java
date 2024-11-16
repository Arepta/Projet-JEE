package com.example.edu.service;

import java.util.Optional;

import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.edu.model.Admin;
import com.example.edu.model.Student;
import com.example.edu.repository.AdminRepository;
import com.example.edu.repository.StudentRepository;

@Service
public class AuthenticationService implements UserDetailsService {
    
    private final AdminRepository adminRepository;
    private final StudentRepository studentRepository;
    
    public AuthenticationService(AdminRepository adminRepository, StudentRepository studentRepository) {
        this.adminRepository = adminRepository;
        this.studentRepository = studentRepository;
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        // Check Admin Repository
        Optional<Admin> admin = adminRepository.findByEmail(email);
        if (admin.isPresent()) {
            return User.builder()
                .username(admin.get().getEmail())
                .password(admin.get().getPassword()) // Password from the database (hashed)
                .roles("ADMIN") // Assign Admin role
                .build();
        }
        
        // Check Student Repository
        Optional<Student> student = studentRepository.findByEmail(email);
        if (student.isPresent() && student.get().getConfirm()) {
            return User.builder()
                .username(student.get().getEmail())
                .password(student.get().getPassword()) // Password from the database (hashed)
                .roles("STUDENT") // Assign Student role
                .build();
        }
        
        // If neither is found, throw exception
        throw new UsernameNotFoundException("User not found with email: " + email);
    }
}