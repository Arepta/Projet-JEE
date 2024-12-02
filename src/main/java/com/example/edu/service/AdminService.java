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
    private final AdminRepository adminRepository;
    private PasswordEncoder passwordEncoder;

    @Autowired
    public AdminService(AdminRepository adminRepository, PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public List<Admin> getAllAdmins() {
        return this.adminRepository.findAll();
    }

    public Optional<Admin> getAdminsById(Long id) {
        return this.adminRepository.findById(id);
    }

    public Optional<Admin> getAdminsByEmail(String email) {
        return this.adminRepository.findByEmail(email);
    }

    public Admin createAdmin(Admin AdminDetails) {
        AdminDetails.setPassword(passwordEncoder.encode(AdminDetails.getPassword()));
        return this.adminRepository.save(AdminDetails);
    }

    public Admin updateAdmin(Long id, Admin AdminDetails) {
        Optional<Admin> optionalAdmin = this.adminRepository.findById(id);

        if (optionalAdmin.isPresent()) {
            Admin updatedAdmin = optionalAdmin.get();

            updatedAdmin.setEmail(AdminDetails.getEmail());
            updatedAdmin.setPassword(passwordEncoder.encode(AdminDetails.getPassword()));

            return this.adminRepository.save(updatedAdmin);
        }

        return null;
    }

    public void deleteAdmin(Long id) {
        this.adminRepository.deleteById(id);
    }
    
}
