package com.example.edu.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.edu.model.Field_teacher;

public interface FieldRepository extends JpaRepository<Field_teacher, Long>{

    @Query(value = "SELECT * FROM field WHERE name = %:name% LIMIT 1", nativeQuery = true)
    Optional<Field_teacher> findByName(@Param("name") String name);

}
