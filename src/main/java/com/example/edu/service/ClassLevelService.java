package com.example.edu.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.edu.model.ClassLevel;
import com.example.edu.repository.ClassLevelRepository;

@Service
public class ClassLevelService {

    // Repository dependency for managing class level data
    private final ClassLevelRepository classLevelRepository;

    // Constructor injection for ClassLevelRepository
    @Autowired
    public ClassLevelService(ClassLevelRepository classLevelRepository) {
        this.classLevelRepository = classLevelRepository;
    }

    // Method to retrieve all class levels
    public List<ClassLevel> getAll() {
        return this.classLevelRepository.findAll();
    }

    // Method to retrieve a mapping of class level IDs with their names
    public Map<Long, String> getAllIdxName() {
        List<ClassLevel> classLevelList = this.classLevelRepository.findAll();
        Map<Long, String> result = new HashMap<>();
        for (ClassLevel cl : classLevelList) {
            result.put(cl.getId(), cl.getName());
        }
        return result;
    }

    // Method to retrieve a mapping of class level IDs with their classes
    public Map<Long, List<Long>> getAllClasses() {
        List<ClassLevel> classLevelList = this.classLevelRepository.findAll();
        Map<Long, List<Long>> result = new HashMap<>();
        for (ClassLevel cl : classLevelList) {
            result.put(cl.getId(), this.classLevelRepository.getClassesForId(cl.getId()));
        }
        return result;
    }

    // Method to retrieve a class level by its ID
    public Optional<ClassLevel> getById(Long id) {
        return this.classLevelRepository.findById(id);
    }

    // Method to retrieve a class level by its name
    public Optional<ClassLevel> getByName(String name) {
        return this.classLevelRepository.findByName(name);
    }

    // Method to create a new class level
    public ClassLevel create(ClassLevel classLevelDetails) {
        return this.classLevelRepository.save(classLevelDetails);
    }

    // Method to update an existing class level
    public ClassLevel update(ClassLevel classLevelDetails) {
        Optional<ClassLevel> optionalClassLevel = this.classLevelRepository.findById(classLevelDetails.getId());

        if (optionalClassLevel.isPresent()) {
            ClassLevel updatedClassLevel = optionalClassLevel.get();
            updatedClassLevel.setName(classLevelDetails.getName());
            return this.classLevelRepository.save(updatedClassLevel);
        }

        return null;
    }

    // Method to delete a class level by its ID
    public void delete(Long id) {
        this.classLevelRepository.deleteById(id);
    }
}
