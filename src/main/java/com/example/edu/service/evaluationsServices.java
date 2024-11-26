package com.example.edu.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.edu.model.Evaluations;
import com.example.edu.repository.evaluationsRepository;

@Service
public class evaluationsServices {
    private final evaluationsRepository evaluationsRepository;

    @Autowired // IMPORTANT
    public evaluationsServices(evaluationsRepository evaluationsRepository) {
        this.evaluationsRepository = evaluationsRepository;
    
    }

    public List<Evaluations> getAll() {
        return this.evaluationsRepository.findAll(); //build in
    }

    public Map<Long, String> getAllIdxName() {

        List<Evaluations> b = this.evaluationsRepository.findAll();
        Map<Long, String> r = new HashMap<>();
        for(Evaluations cl : b){
            r.put(cl.getId(), cl.getName());
        }
        return r; //build in
    }

    public Map<Long, List<Long>> getAllEvaluations() {

        List<Evaluations> b = this.evaluationsRepository.findAll();
        Map<Long, List<Long>> r = new HashMap<>();
        for(Evaluations cl : b){
            r.put(cl.getId(), this.evaluationsRepository.getSudentById(cl.getId()));
        }
        return r; //build in
    }

    public Optional<Evaluations> getById(Long id) {
        return this.evaluationsRepository.findById(id);  //build in
    }

    public Evaluations create(Evaluations details) {
        return this.evaluationsRepository.save(details);  //build in
    }

    public Evaluations update(Evaluations details) {
        Optional<Evaluations> optionalEvaluation = this.evaluationsRepository.findById(details.getId());

        if (optionalEvaluation.isPresent()) {
            Evaluations updatedCourses = optionalEvaluation.get();

            updatedCourses.setName(details.getName());
            updatedCourses.setMaxScore(details.getMaxScore());
            updatedCourses.setMinScore(details.getMinScore());
            updatedCourses.setScore(details.getScore());
            updatedCourses.setCourse(details.getCourse());
            updatedCourses.setStudent(details.getStudent());

            return this.evaluationsRepository.save(updatedCourses);
        }

        return null;
    }

    public void delete(Long id) {
        this.evaluationsRepository.deleteById(id);
    }
}
