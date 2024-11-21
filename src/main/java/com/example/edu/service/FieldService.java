package com.example.edu.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.edu.model.Field_teacher;
import com.example.edu.repository.FieldRepository;

@Service
public class FieldService {
    private final FieldRepository FieldRepository;

    @Autowired // IMPORTANT
    public FieldService(FieldRepository FieldRepository) {
        this.FieldRepository = FieldRepository;
    
    }

    public List<Field_teacher> getAllField() {
        return this.FieldRepository.findAll(); //build in
    }
    

    public Map<Long, String> getAllFieldIdxName() {

        List<Field_teacher> b = this.FieldRepository.findAll();
        Map<Long, String> r = new HashMap<>();
        for(Field_teacher cl : b){
            r.put(cl.getId(), cl.getName());
        }
        return r; //build in
    }

    public Optional<Field_teacher> getFieldById(Long id) {
        return this.FieldRepository.findById(id);  //build in
    }

    public Optional<Field_teacher> getFieldByName(String name) {
        return this.FieldRepository.findByName(name);  //build in
    }

    public Field_teacher createField(Field_teacher FieldDetails) {
        return this.FieldRepository.save(FieldDetails);  //build in
    }

    public Field_teacher updateField(Field_teacher FieldDetails) {
        Optional<Field_teacher> optionalField = this.FieldRepository.findById(FieldDetails.getId());

        if (optionalField.isPresent()) {
            Field_teacher updatedField = optionalField.get();

            updatedField.setName(FieldDetails.getName());

            return this.FieldRepository.save(updatedField);
        }

        return null;
    }

    public void deleteField(Long id) {
        this.FieldRepository.deleteById(id);
    }
}
