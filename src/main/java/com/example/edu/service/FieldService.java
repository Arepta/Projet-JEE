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

    // Repository dependency for managing field data
    private final FieldRepository fieldRepository;

    // Constructor injection for FieldRepository
    @Autowired
    public FieldService(FieldRepository fieldRepository) {
        this.fieldRepository = fieldRepository;
    }

    // Method to retrieve all fields
    public List<FieldTeacher> getAll() {
        return this.fieldRepository.findAll();
    }

    // Method to retrieve a mapping of field IDs with their names
    public Map<Long, String> getAllIdxName() {
        List<FieldTeacher> fieldsList = this.fieldRepository.findAll();
        Map<Long, String> result = new HashMap<>();
        for (FieldTeacher field : fieldsList) {
            result.put(field.getId(), field.getName());
        }
        return result;
    }

    // Method to retrieve a field by its ID
    public Optional<FieldTeacher> getById(Long id) {
        return this.fieldRepository.findById(id);
    }

    // Method to retrieve a field by its name
    public Optional<FieldTeacher> getByName(String name) {
        return this.fieldRepository.findByName(name);
    }

    // Method to create a new field
    public FieldTeacher create(FieldTeacher fieldDetails) {
        return this.fieldRepository.save(fieldDetails);
    }

    // Method to update an existing field
    public FieldTeacher update(FieldTeacher fieldDetails) {
        Optional<FieldTeacher> optionalField = this.fieldRepository.findById(fieldDetails.getId());

        if (optionalField.isPresent()) {
            FieldTeacher updatedField = optionalField.get();
            updatedField.setName(fieldDetails.getName());
            return this.fieldRepository.save(updatedField);
        }

        return null;
    }

    // Method to delete a field by its ID
    public void delete(Long id) {
        this.fieldRepository.deleteById(id);
    }
}
