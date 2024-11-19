package com.example.edu.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.edu.model.ClassLevel;
import com.example.edu.repository.ClassLevelRepository;


@Service
public class ClassLevelService {
    private final ClassLevelRepository classLevelRepository;

    @Autowired // IMPORTANT
    public ClassLevelService(ClassLevelRepository classLevelRepository) {
        this.classLevelRepository = classLevelRepository;
    
    }

        public List<ClassLevel> getAllClassLevels() {
        return this.classLevelRepository.findAll(); //build in
    }

    public Optional<ClassLevel> getClassLevelsById(Long id) {
        return this.classLevelRepository.findById(id);  //build in
    }

    public Optional<ClassLevel> getClassLevelsByName(String name) {
        return this.classLevelRepository.findByName(name);  //build in
    }

    public ClassLevel createClassLevel(ClassLevel ClassLevelDetails) {
        return this.classLevelRepository.save(ClassLevelDetails);  //build in
    }

    public ClassLevel updateClassLevel(ClassLevel ClassLevelDetails) {
        Optional<ClassLevel> optionalClassLevel = this.classLevelRepository.findById(ClassLevelDetails.getId());

        if (optionalClassLevel.isPresent()) {
            ClassLevel updatedClassLevel = optionalClassLevel.get();

            updatedClassLevel.setName(ClassLevelDetails.getName());

            return this.classLevelRepository.save(updatedClassLevel);
        }

        return null;
    }

    public void deleteClassLevel(Long id) {
        this.classLevelRepository.deleteById(id);
    }
}
