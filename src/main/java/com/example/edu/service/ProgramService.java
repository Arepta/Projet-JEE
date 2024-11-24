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
    private final ProgramRepository programRepository;

    @Autowired // IMPORTANT
    public ProgramService(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    public List<Program> getAll() {
        return this.programRepository.findAll(); //build in
    }

    public Map<Long, String> getAllIdxName() {

        List<Program> b = this.programRepository.findAll();
        Map<Long, String> r = new HashMap<>();
        for(Program cl : b){
            r.put(cl.getId(), cl.getName());
        }
        return r; //build in
    }

    public Map<Long, List<Long>> getAllLevel() {

        List<Program> b = this.programRepository.findAll();

        Map<Long, List<Long>> r = new HashMap<>();
        for(Program cl : b){
            r.put(cl.getId(), this.programRepository.getLevelForId(cl.getId()));
        }
        return r; //build in
    }

    public Optional<Program> getById(Long id) {
        return this.programRepository.findById(id);  //build in
    }

    public Optional<Program> getByName(String name) {
        return this.programRepository.findByName(name);  //build in
    }

    public Program create(Program ProgramDetails) {
        return this.programRepository.save(ProgramDetails);  //build in
    }

    public Program update(Program ProgramDetails) {
        Optional<Program> optionalProgram = this.programRepository.findById(ProgramDetails.getId());

        if (optionalProgram.isPresent()) {
            Program updatedProgram = optionalProgram.get();

            updatedProgram.setName(ProgramDetails.getName());

            return this.programRepository.save(updatedProgram);
        }

        return null;
    }

    public void delete(Long id) {
        this.programRepository.deleteById(id);
    }
}
