package com.example.edu.service;

import org.springframework.stereotype.Service;

import com.example.edu.model.ProgramsContent;
import com.example.edu.repository.ProgramsContentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProgramsContentService {

    // Repository dependency for managing programs content data
    private final ProgramsContentRepository repository;

    // Constructor injection for ProgramsContentRepository
    public ProgramsContentService(ProgramsContentRepository repository) {
        this.repository = repository;
    }

    // Method to retrieve all program contents
    public List<ProgramsContent> getAll() {
        return repository.findAll();
    }

    // Method to retrieve a program content by its composite ID
    public Optional<ProgramsContent> getById(ProgramsContent.ProgramsContentId id) {
        return repository.findById(id);
    }

    // Method to create a new program content
    public ProgramsContent create(ProgramsContent programsContent) {
        try {
            return repository.save(programsContent);
        } catch (Exception e) {
            return null; // Return null if an error occurs
        }
    }

    // Method to delete a program content by its composite ID
    public void delete(ProgramsContent.ProgramsContentId id) {
        this.repository.deleteById(id);
    }
}
