package com.example.edu.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.edu.model.Evaluations;
import com.example.edu.repository.EvaluationsRepository;

@Service
public class EvaluationsServices {

    private final EvaluationsRepository evaluationsRepository;

    @Autowired // Annotation pour l'injection automatique
    public EvaluationsServices(EvaluationsRepository evaluationsRepository) {
        this.evaluationsRepository = evaluationsRepository;
    }

    /**
     * Récupérer toutes les évaluations
     *
     * @return Liste des évaluations
     */
    public List<Evaluations> getAll() {
        return this.evaluationsRepository.findAll();
    }

    /**
     * Récupérer un mapping d'ID d'évaluation avec leurs noms
     *
     * @return Map contenant les ID comme clé et les noms comme valeur
     */
    public Map<Long, String> getAllIdxName() {
        List<Evaluations> evaluations = this.evaluationsRepository.findAll();
        Map<Long, String> result = new HashMap<>();
        for (Evaluations eval : evaluations) {
            result.put(eval.getId(), eval.getName());
        }
        return result;
    }

    /**
     * Récupérer toutes les évaluations pour un étudiant spécifique
     *
     * @param studentId ID de l'étudiant
     * @return Map contenant les évaluations de l'étudiant
     */
    public Map<Long, List<Evaluations>> getAllEvaluationsByStudent(Long studentId) {
        List<Evaluations> evaluations = this.evaluationsRepository.findEvaluationsByStudentId(studentId);
        Map<Long, List<Evaluations>> result = new HashMap<>();
        result.put(studentId, evaluations);
        return result;
    }

    /**
     * Récupérer une évaluation par son ID
     *
     * @param id ID de l'évaluation
     * @return Évaluation correspondante (Optional)
     */
    public Optional<Evaluations> getById(Long id) {
        return this.evaluationsRepository.findById(id);
    }

    /**
     * Créer une nouvelle évaluation
     *
     * @param details Détails de l'évaluation
     * @return Évaluation créée
     */
    public Evaluations create(Evaluations details) {
        return this.evaluationsRepository.save(details);
    }

    /**
     * Mettre à jour une évaluation existante
     *
     * @param details Détails mis à jour de l'évaluation
     * @return Évaluation mise à jour ou null si non trouvée
     */
    public Evaluations update(Evaluations details) {
        Optional<Evaluations> optionalEvaluation = this.evaluationsRepository.findById(details.getId());

        if (optionalEvaluation.isPresent()) {
            Evaluations updatedEvaluation = optionalEvaluation.get();
            updatedEvaluation.setName(details.getName());
            updatedEvaluation.setMaxScore(details.getMaxScore());
            updatedEvaluation.setMinScore(details.getMinScore());
            updatedEvaluation.setScore(details.getScore());
            updatedEvaluation.setCourse(details.getCourse());
            updatedEvaluation.setStudent(details.getStudent());
            return this.evaluationsRepository.save(updatedEvaluation);
        }

        return null;
    }

    /**
     * Supprimer une évaluation par ID
     *
     * @param id ID de l'évaluation à supprimer
     */
    public void delete(Long id) {
        this.evaluationsRepository.deleteById(id);
    }
}
