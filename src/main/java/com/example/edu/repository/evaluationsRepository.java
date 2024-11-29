package com.example.edu.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.edu.model.Evaluations;

public interface EvaluationsRepository extends JpaRepository<Evaluations, Long> {
    
    // Requête JPQL pour récupérer les évaluations par ID de l'étudiant
    @Query("SELECT e FROM Evaluations e WHERE e.student.id = :studentId")
    List<Evaluations> findEvaluationsByStudentId(@Param("studentId") Long studentId);
}
