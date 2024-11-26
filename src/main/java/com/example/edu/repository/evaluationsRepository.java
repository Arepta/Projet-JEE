package com.example.edu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.edu.model.Evaluations;

public interface evaluationsRepository extends JpaRepository<Evaluations, Long>{
    @Query(value = "SELECT * FROM evalutaions WHERE name = %:name% LIMIT 1", nativeQuery = true)
    List<Long> getSudentById(@Param("id") Long id);
}
