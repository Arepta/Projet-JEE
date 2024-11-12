package com.example.edu.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.edu.model.Student;

public interface StudentRepository extends JpaRepository<Student, Long> {
    
    @Query(value = "SELECT * FROM students WHERE email = %:email% LIMIT 1", nativeQuery = true)
    Optional<Student> findStudentByEmail(@Param("email") String email);

    @Query(value = "SELECT * FROM students WHERE email = %:email% AND password = %:password%  LIMIT 1", nativeQuery = true)
    Optional<Student> findStudentByCredential(@Param("email") String email, @Param("password") String password);

}
