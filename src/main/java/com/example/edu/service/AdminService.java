package com.example.edu.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.edu.model.Admin;
import com.example.edu.repository.AdminRepository;

@Service
public class AdminService {

    // Repository and password encoder dependencies for managing admin data
    private final AdminRepository adminRepository;
    private PasswordEncoder passwordEncoder;

    // Constructor injection of AdminRepository and PasswordEncoder
    @Autowired
    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Method to retrieve all admins
    public List<Admin> getAllAdmins() {
        return this.adminRepository.findAll();
    }

    // Method to retrieve an admin by its ID
    public Optional<Admin> getAdminsById(Long id) {
        return this.adminRepository.findById(id);
    }

    // Method to retrieve an admin by email
    public Optional<Admin> getAdminsByEmail(String email) {
        return this.adminRepository.findByEmail(email);
    }

    // Method to create a new admin with hashed password
    public Admin createAdmin(Admin adminDetails) {
        adminDetails.setPassword(passwordEncoder.encode(adminDetails.getPassword()));
        return this.adminRepository.save(adminDetails);
    }

    // Method to update an existing admin
    public Admin updateAdmin(Long id, Admin adminDetails) {
        Optional<Admin> optionalAdmin = this.adminRepository.findById(id);

        if (optionalAdmin.isPresent()) {
            Admin updatedAdmin = optionalAdmin.get();
            updatedAdmin.setEmail(adminDetails.getEmail());
            updatedAdmin.setPassword(passwordEncoder.encode(adminDetails.getPassword()));
            return this.adminRepository.save(updatedAdmin);
        }

        return null;
    }

    // Method to delete an admin by its ID
    public void deleteAdmin(Long id) {
        this.adminRepository.deleteById(id);
    }
}
