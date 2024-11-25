package com.example.edu.service;

import org.springframework.stereotype.Service;

import com.example.edu.model.ProgramsContent;
import com.example.edu.repository.ProgramsContentRepository;

import java.util.List;
import java.util.Optional;

@Service
public class ProgramsContentService {

    private final ProgramsContentRepository repository;

    public ProgramsContentService(ProgramsContentRepository repository) {
        this.repository = repository;
    }


    public List<ProgramsContent> getAll() {
        return repository.findAll();
    }

    public Optional<ProgramsContent> getById(ProgramsContent.ProgramsContentId id) {
        return repository.findById(id);
    }

    public ProgramsContent create(ProgramsContent programsContent) {
        try{
            return repository.save(programsContent);
        }
        catch(Exception e){
            return null;
        }
    }

    public void delete(ProgramsContent.ProgramsContentId id) {
        this.repository.deleteById(id);
    }
}
