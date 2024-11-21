package com.example.edu.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.edu.model.Teacher;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    
    @Query(value = "SELECT * FROM teachers WHERE email = %:email% LIMIT 1", nativeQuery = true)
    Optional<Teacher> findByEmail(@Param("email") String email);

}
