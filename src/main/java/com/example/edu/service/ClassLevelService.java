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
    private final ClassLevelRepository classLevelRepository;

    @Autowired // IMPORTANT
    public ClassLevelService(ClassLevelRepository classLevelRepository) {
        this.classLevelRepository = classLevelRepository;
    
    }

    public List<ClassLevel> getAll() {
        return this.classLevelRepository.findAll();
    }

    public Map<Long, String> getAllIdxName() {

        List<ClassLevel> b = this.classLevelRepository.findAll();
        Map<Long, String> r = new HashMap<>();
        for(ClassLevel cl : b){
            r.put(cl.getId(), cl.getName());
        }
        return r;
    }

    public Map<Long, List<Long>> getAllClasses() {

        List<ClassLevel> b = this.classLevelRepository.findAll();
        Map<Long, List<Long>> r = new HashMap<>();
        for(ClassLevel cl : b){
            r.put(cl.getId(), this.classLevelRepository.getClassesForId(cl.getId()));
        }
        return r;
    }

    public Optional<ClassLevel> getById(Long id) {
        return this.classLevelRepository.findById(id);
    }

    public Optional<ClassLevel> getByName(String name) {
        return this.classLevelRepository.findByName(name);
    }

    public ClassLevel create(ClassLevel ClassLevelDetails) {
        return this.classLevelRepository.save(ClassLevelDetails);
    }

    public ClassLevel update(ClassLevel ClassLevelDetails) {
        Optional<ClassLevel> optionalClassLevel = this.classLevelRepository.findById(ClassLevelDetails.getId());

        if (optionalClassLevel.isPresent()) {
            ClassLevel updatedClassLevel = optionalClassLevel.get();

            updatedClassLevel.setName(ClassLevelDetails.getName());

            return this.classLevelRepository.save(updatedClassLevel);
        }

        return null;
    }

    public void delete(Long id) {
        this.classLevelRepository.deleteById(id);
    }
}
