package com.example.edu.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.edu.model.FieldTeacher;


public interface FieldRepository extends JpaRepository<FieldTeacher, Long>{

    @Query(value = "SELECT * FROM field WHERE name = %:name% LIMIT 1", nativeQuery = true)
    Optional<FieldTeacher> findByName(@Param("name") String name);

}
