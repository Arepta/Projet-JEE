package com.example.edu.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.edu.model.FieldTeacher;
import com.example.edu.repository.FieldRepository;

@Service
public class FieldService {
    private final FieldRepository FieldRepository;

    @Autowired // IMPORTANT
    public FieldService(FieldRepository FieldRepository) {
        this.FieldRepository = FieldRepository;
    
    }

    public List<FieldTeacher> getAll() {
        return this.FieldRepository.findAll();
    }
    

    public Map<Long, String> getAllIdxName() {

        List<FieldTeacher> b = this.FieldRepository.findAll();
        Map<Long, String> r = new HashMap<>();
        for(FieldTeacher cl : b){
            r.put(cl.getId(), cl.getName());
        }
        return r;
    }

    public Optional<FieldTeacher> getById(Long id) {
        return this.FieldRepository.findById(id); 
    }

    public Optional<FieldTeacher> getByName(String name) {
        return this.FieldRepository.findByName(name); 
    }

    public FieldTeacher create(FieldTeacher FieldDetails) {
        return this.FieldRepository.save(FieldDetails); 
    }

    public FieldTeacher update(FieldTeacher FieldDetails) {
        Optional<FieldTeacher> optionalField = this.FieldRepository.findById(FieldDetails.getId());

        if (optionalField.isPresent()) {
            FieldTeacher updatedField = optionalField.get();

            updatedField.setName(FieldDetails.getName());

            return this.FieldRepository.save(updatedField);
        }

        return null;
    }

    public void delete(Long id) {
        this.FieldRepository.deleteById(id);
    }
}
