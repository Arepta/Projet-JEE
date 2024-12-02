package com.example.edu.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.edu.model.Program;
import com.example.edu.repository.ProgramRepository;

@Service
public class ProgramService {

    // Repository dependency for managing program data
    private final ProgramRepository programRepository;

    // Constructor injection for ProgramRepository
    @Autowired
    public ProgramService(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    // Method to retrieve all programs
    public List<Program> getAll() {
        return this.programRepository.findAll();
    }

    // Method to retrieve a mapping of program IDs with their names
    public Map<Long, String> getAllIdxName() {
        List<Program> programList = this.programRepository.findAll();
        Map<Long, String> result = new HashMap<>();
        for (Program program : programList) {
            result.put(program.getId(), program.getName());
        }
        return result;
    }

    // Method to retrieve a mapping of program IDs with their associated levels
    public Map<Long, List<Long>> getAllLevel() {
        List<Program> programList = this.programRepository.findAll();
        Map<Long, List<Long>> result = new HashMap<>();
        for (Program program : programList) {
            result.put(program.getId(), this.programRepository.getLevelForId(program.getId()));
        }
        return result;
    }

    // Method to retrieve a program by its ID
    public Optional<Program> getById(Long id) {
        return this.programRepository.findById(id);
    }

    // Method to retrieve a program by its name
    public Optional<Program> getByName(String name) {
        return this.programRepository.findByName(name);
    }

    // Method to create a new program
    public Program create(Program programDetails) {
        return this.programRepository.save(programDetails);
    }

    // Method to update an existing program
    public Program update(Program programDetails) {
        Optional<Program> optionalProgram = this.programRepository.findById(programDetails.getId());

        if (optionalProgram.isPresent()) {
            Program updatedProgram = optionalProgram.get();
            updatedProgram.setName(programDetails.getName());
            return this.programRepository.save(updatedProgram);
        }

        return null;
    }

    // Method to delete a program by its ID
    public void delete(Long id) {
        this.programRepository.deleteById(id);
    }
}
