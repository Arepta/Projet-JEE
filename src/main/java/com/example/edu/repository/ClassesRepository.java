package com.example.edu.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.edu.model.Classes;

public interface ClassesRepository extends JpaRepository< Classes, Long>{

    @Query(value = "SELECT * FROM class WHERE name = %:name% LIMIT 1", nativeQuery = true)
    Optional<Classes> findByName(@Param("name") String name);

}
