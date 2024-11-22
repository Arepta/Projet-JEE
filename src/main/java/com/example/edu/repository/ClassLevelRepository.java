package com.example.edu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.edu.model.ClassLevel;

public interface ClassLevelRepository  extends JpaRepository<ClassLevel, Long> {

    @Query(value = "SELECT * FROM class_level WHERE name = %:name% LIMIT 1", nativeQuery = true)
    Optional<ClassLevel> findByName(@Param("name") String name);

    @Query(value = "SELECT class.id FROM class_level JOIN class ON class_level.id = class.level WHERE class.level = %:id%", nativeQuery = true)
    List<Long> getClassesForId(@Param("id") Long id);
}
