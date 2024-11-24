package com.example.edu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.edu.model.Program;

public interface ProgramRepository extends JpaRepository<Program, Long> {
    @Query(value = "SELECT * FROM programs WHERE name = %:name% LIMIT 1", nativeQuery = true)
    Optional<Program> findByName(@Param("name") String name);

    @Query(value = "SELECT DISTINCT cl.id AS class_level_id FROM programs p JOIN programs_content pc ON p.id = pc.program JOIN courses c ON pc.course = c.id JOIN class_level cl ON c.level = cl.id WHERE p.id = %:id% ;", nativeQuery = true)
    List<Long> getLevelForId(@Param("id") Long id);

}
