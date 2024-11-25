package com.example.edu.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.edu.model.Classes;

public interface ClassesRepository extends JpaRepository< Classes, Long>{

    @Query(value = "SELECT * FROM class WHERE name = %:name% LIMIT 1", nativeQuery = true)
    Optional<Classes> findByName(@Param("name") String name);

    @Query(value = "SELECT DISTINCT r.id FROM rooms r WHERE r.size >= ( SELECT COUNT(s.id) FROM students s WHERE s.class = %:id%)", nativeQuery = true)
    List<Long> findRoomForId(@Param("id") Long id);
}
