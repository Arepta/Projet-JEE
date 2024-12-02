package com.example.edu.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.edu.model.Classes;
import com.example.edu.repository.ClassesRepository;

@Service
public class ClassesService {

    // Repository dependency for managing class data
    private final ClassesRepository classesRepository;

    // Constructor injection for ClassesRepository
    @Autowired
    public ClassesService(ClassesRepository classesRepository) {
        this.classesRepository = classesRepository;
    }

    // Method to retrieve all classes
    public List<Classes> getAll() {
        List<Classes> classesList = this.classesRepository.findAll();
        Map<String, Integer> prCount = new HashMap<>();
        String key;
        for (Classes cl : classesList) {
            key = (cl.getLevel() == null ? "" : cl.getLevel().getName() + " ") + cl.getProgram().getName();
            if (prCount.get(key) == null) {
                prCount.put(key, 1);
            } else {
                prCount.put(key, prCount.get(key) + 1);
            }

            if (cl.getName() == null) {
                cl.setName(key + " Group " + prCount.get(key));
                System.out.println(cl);
            }
        }
        System.out.println(classesList);
        return classesList;
    }

    // Method to retrieve a mapping of class IDs with their names
    public Map<Long, String> getAllIdxName() {
        List<Classes> classesList = this.getAll();
        Map<Long, String> result = new HashMap<>();
        for (Classes cl : classesList) {
            result.put(cl.getId(), cl.getName());
        }
        return result;
    }

    // Method to retrieve a mapping of class IDs with their room IDs
    public Map<Long, List<Long>> getAllRoom() {
        List<Classes> classesList = this.classesRepository.findAll();
        Map<Long, List<Long>> result = new HashMap<>();
        for (Classes cl : classesList) {
            result.put(cl.getId(), this.classesRepository.findRoomForId(cl.getId()));
        }
        return result;
    }

    // Method to retrieve a class by its ID
    public Optional<Classes> getById(Long id) {
        return this.classesRepository.findById(id);
    }

    // Method to retrieve a class by its name
    public Optional<Classes> getByName(String name) {
        return this.classesRepository.findByName(name);
    }

    // Method to create a new class
    public Classes create(Classes classesDetails) {
        return this.classesRepository.save(classesDetails);
    }

    // Method to update an existing class
    public Classes update(Classes classesDetails) {
        Optional<Classes> optionalClasses = this.classesRepository.findById(classesDetails.getId());

        if (optionalClasses.isPresent()) {
            Classes updatedClasses = optionalClasses.get();
            updatedClasses.setName(classesDetails.getName());
            updatedClasses.setLevel(classesDetails.getLevel());
            updatedClasses.setProgram(classesDetails.getProgram());
            return this.classesRepository.save(updatedClasses);
        }

        return null;
    }

    // Method to delete a class by its ID
    public void delete(Long id) {
        this.classesRepository.deleteById(id);
    }
}
