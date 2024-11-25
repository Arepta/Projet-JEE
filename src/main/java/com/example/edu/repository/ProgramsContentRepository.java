package com.example.edu.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.edu.model.ProgramsContent;

@Repository
public interface ProgramsContentRepository extends JpaRepository<ProgramsContent, ProgramsContent.ProgramsContentId> {
    
}