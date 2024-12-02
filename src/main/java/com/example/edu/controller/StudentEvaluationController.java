package com.example.edu.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.edu.model.Evaluations;
import com.example.edu.model.Student;
import com.example.edu.service.ClassesService;
import com.example.edu.service.CoursesService;
import com.example.edu.service.StudentService;
import com.example.edu.service.evaluationsServices;
import com.example.edu.tool.template.TableSingle;

@Controller
@RequestMapping("/student")
public class StudentEvaluationController {

    private final evaluationsServices evalService;
    private final ClassesService classesService;
    private final CoursesService coursesService;
    private final StudentService studentService;

    private TableSingle tableTemplate;

    @Autowired
    public StudentEvaluationController(evaluationsServices evalService, ClassesService classesService, CoursesService coursesService, StudentService studentService) {
        this.evalService = evalService;
        this.classesService = classesService;
        this.coursesService = coursesService;
        this.studentService = studentService;
        

        // Configuration des colonnes pour le tableau
        Map<String, String> columnToLabel = Map.of(
            "id", "ID",
            "name", "Nom de l'évaluation",
            "student", "Eleve",
            "score", "Note"
        );
        List<String> columnDisplayed = Arrays.asList("id", "name", "student", "score");

        this.tableTemplate = new TableSingle("Évaluations", columnToLabel, columnDisplayed);
        this.tableTemplate.addLock("course");
        this.tableTemplate.setValuesFor("student", this.studentService::getAllIdxNameSurname);
        // Ajouter les filtres pour "Classe", "Matière", et "Nom de l'évaluation"
        this.tableTemplate.addFilter("student");


    }

    /**
     * GET - Affiche la liste des évaluations avec filtres Classe, Matière, et Nom de l'évaluation.
     */
    @GetMapping("/evaluation")
    public String listEvaluations(@RequestParam(required = false) Long classeId,
                                   @RequestParam(required = false) Long matiereId,
                                   @RequestParam(required = false) String evaluationName,
                                   Model model, Authentication authentication) {
        // Récupérer toutes les évaluations

        Student student = studentService.getByEmail(authentication.getName()).orElse(null);
        if (student == null) {
            String msg = "No student found for email: {}"+ authentication.getName();
            System.out.println(msg);
            return "student/default"; // Vue JSP générique
        } else {
            String msg ="Student found: id={}, email={}"+ student.getId()+ student.getEmail();
            System.out.println(msg);
        }
        Map<Long, List<Evaluations>> evaluationsMap = evalService.getAllEvaluationsByStudent(student.getId());
        List<Evaluations> evaluations = evaluationsMap.getOrDefault(student.getId(), new ArrayList<>());

        // Initialiser les relations nécessaires
        evaluations.forEach(eval -> {
            Hibernate.initialize(eval.getStudent()); // Initialisation explicite de la relation Student
            Hibernate.initialize(eval.getStudent().getStudentClass()); // Initialisation explicite de la relation StudentClass
        });

        // Appliquer les filtres
        if (classeId != null) {
            evaluations = evaluations.stream()
                .filter(eval -> eval.getStudent().getStudentClass() != null &&
                                eval.getStudent().getStudentClass().getId().equals(classeId))
                .toList();
        }

        if (evaluationName != null && !evaluationName.isBlank()) {
            evaluations = evaluations.stream()
                .filter(eval -> eval.getName() != null && eval.getName().toLowerCase().contains(evaluationName.toLowerCase()))
                .toList();
        }

        // Initialiser le tableau avec les données filtrées
        tableTemplate.initModel(model, evaluations, Evaluations.class);

        // Ajouter les valeurs pour les filtres
        model.addAttribute("classes", classesService.getAll());
        model.addAttribute("courses", coursesService.getAll());
        model.addAttribute("selectedClasse", classeId);
        model.addAttribute("selectedMatiere", matiereId);
        model.addAttribute("selectedEvaluationName", evaluationName);

        return "student/default"; // Vue JSP générique
    }


}
