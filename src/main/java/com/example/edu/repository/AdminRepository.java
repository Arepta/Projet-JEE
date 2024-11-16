package com.example.edu.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.edu.model.Admin;

public interface AdminRepository extends JpaRepository<Admin, Long>{

    @Query(value = "SELECT * FROM admin WHERE email = %:email% LIMIT 1", nativeQuery = true)
    Optional<Admin> findByEmail(@Param("email") String email);
}
