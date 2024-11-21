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

    public List<FieldTeacher> getAllField() {
        return this.FieldRepository.findAll(); //build in
    }
    

    public Map<Long, String> getAllFieldIdxName() {

        List<FieldTeacher> b = this.FieldRepository.findAll();
        Map<Long, String> r = new HashMap<>();
        for(FieldTeacher cl : b){
            r.put(cl.getId(), cl.getName());
        }
        return r; //build in
    }

    public Optional<FieldTeacher> getFieldById(Long id) {
        return this.FieldRepository.findById(id);  //build in
    }

    public Optional<FieldTeacher> getFieldByName(String name) {
        return this.FieldRepository.findByName(name);  //build in
    }

    public FieldTeacher createField(FieldTeacher FieldDetails) {
        return this.FieldRepository.save(FieldDetails);  //build in
    }

    public FieldTeacher updateField(FieldTeacher FieldDetails) {
        Optional<FieldTeacher> optionalField = this.FieldRepository.findById(FieldDetails.getId());

        if (optionalField.isPresent()) {
            FieldTeacher updatedField = optionalField.get();

            updatedField.setName(FieldDetails.getName());

            return this.FieldRepository.save(updatedField);
        }

        return null;
    }

    public void deleteField(Long id) {
        this.FieldRepository.deleteById(id);
    }
}
