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
    private final ClassesRepository classesRepository;

    @Autowired // IMPORTANT
    public ClassesService(ClassesRepository classesRepository) {
        this.classesRepository = classesRepository;
    
    }

    public List<Classes> getAll() {
        List<Classes> b = this.classesRepository.findAll();
        Map<String, Integer> prCount = new HashMap<>();
        String key;
        for(Classes cl : b){

            key = (cl.getLevel() == null ? "" : cl.getLevel().getName()+" ")+ cl.getProgram().getName();
            if(prCount.get(key) == null){
                prCount.put(key, 1);
            }
            else{
                prCount.put(key, prCount.get(key)+1);
            }

            if(cl.getName() == null){
                cl.setName(key+" Groupe "+ prCount.get(key));
                System.out.println(cl);
            }

            
        }
        System.out.println(b);
        return b; //build in
    }

    public Map<Long, String> getAllIdxName() {

        List<Classes> b = this.getAll();
        Map<Long, String> r = new HashMap<>();
        for(Classes cl : b){
            r.put(cl.getId(), cl.getName());
        }
        return r; //build in
    }

    public Map<Long, List<Long>> getAllRoom() {

        List<Classes> b = this.classesRepository.findAll();

        Map<Long, List<Long>> r = new HashMap<>();
        for(Classes cl : b){
            r.put(cl.getId(), this.classesRepository.findRoomForId(cl.getId()));
        }
        return r; //build in
    }

    public Optional<Classes> getById(Long id) {
        return this.classesRepository.findById(id);  //build in
    }

    public Optional<Classes> getByName(String name) {
        return this.classesRepository.findByName(name);  //build in
    }

    public Classes create(Classes ClassesDetails) {
        return this.classesRepository.save(ClassesDetails);  //build in
    }

    public Classes update(Classes ClassesDetails) {
        Optional<Classes> optionalClasses = this.classesRepository.findById(ClassesDetails.getId());

        if (optionalClasses.isPresent()) {
            Classes updatedClasses = optionalClasses.get();

            updatedClasses.setName(ClassesDetails.getName());
            updatedClasses.setLevel(ClassesDetails.getLevel());
            updatedClasses.setProgram(ClassesDetails.getProgram());

            return this.classesRepository.save(updatedClasses);
        }

        return null;
    }

    public void delete(Long id) {
        this.classesRepository.deleteById(id);
    }
}

