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

    // Repository dependency for managing evaluations data
    private final evaluationsRepository evaluationsRepository;

    // Constructor injection for evaluationsRepository
    @Autowired
    public evaluationsServices(evaluationsRepository evaluationsRepository) {
        this.evaluationsRepository = evaluationsRepository;
    }

    // Method to retrieve all evaluations
    public List<Evaluations> getAll() {
        return this.evaluationsRepository.findAll();
    }

    // Method to retrieve a mapping of evaluation IDs with their names
    public Map<Long, String> getAllIdxName() {
        List<Evaluations> evaluationsList = this.evaluationsRepository.findAll();
        Map<Long, String> result = new HashMap<>();
        for (Evaluations eval : evaluationsList) {
            result.put(eval.getId(), eval.getName());
        }
        return result;
    }

    // Method to retrieve all evaluations for a specific student
    public Map<Long, List<Evaluations>> getAllEvaluationsByStudent(Long studentId) {
        List<Evaluations> evaluations = this.evaluationsRepository.findEvaluationsByStudentId(studentId);
        Map<Long, List<Evaluations>> result = new HashMap<>();
        result.put(studentId, evaluations);
        return result;
    }

    // Method to retrieve an evaluation by its ID
    public Optional<Evaluations> getById(Long id) {
        return this.evaluationsRepository.findById(id);
    }

    // Method to create a new evaluation
    public Evaluations createEvaluations(Evaluations details) {
        return this.evaluationsRepository.save(details);
    }

    // Method to update an existing evaluation
    public Evaluations update(Evaluations details) {
        Optional<Evaluations> optionalEvaluation = this.evaluationsRepository.findById(details.getId());

        if (optionalEvaluation.isPresent()) {
            Evaluations updatedEvaluation = optionalEvaluation.get();
            updatedEvaluation.setName(details.getName());
            updatedEvaluation.setScore(details.getScore());
            updatedEvaluation.setStudent(details.getStudent());
            return this.evaluationsRepository.save(updatedEvaluation);
        }

        return null;
    }

    // Method to delete an evaluation by its ID
    public void delete(Long id) {
        this.evaluationsRepository.deleteById(id);
    }
}
