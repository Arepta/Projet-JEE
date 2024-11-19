package com.example.edu.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.edu.model.Classes;
import com.example.edu.repository.ClassesRepository;


@Service
public class ClassesService {
    private final ClassesRepository classesRepository;

    @Autowired // IMPORTANT
    public ClassesService(ClassesRepository classesRepository) {
        this.classesRepository = classesRepository;
    
    }

    public List<Classes> getAllClasses() {
        return this.classesRepository.findAll(); //build in
    }

    public Optional<Classes> getClassesById(Long id) {
        return this.classesRepository.findById(id);  //build in
    }

    public Optional<Classes> getClassesByName(String name) {
        return this.classesRepository.findByName(name);  //build in
    }

    public Classes createClasses(Classes ClassesDetails) {
        return this.classesRepository.save(ClassesDetails);  //build in
    }

    public Classes updateClasses(Classes ClassesDetails) {
        // Optional<Classes> optionalClasses = this.classesRepository.findById(ClassesDetails.getId());

        // if (optionalClasses.isPresent()) {
        //     Classes updatedClasses = optionalClasses.get();

        //     updatedClasses.setName(ClassesDetails.getName());

        //     return this.classesRepository.save(updatedClasses);
        // }

        return null;
    }

    public void deleteClasses(Long id) {
        this.classesRepository.deleteById(id);
    }
}

