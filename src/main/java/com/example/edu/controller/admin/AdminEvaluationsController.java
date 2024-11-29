package com.example.edu.controller.admin;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.edu.model.Evaluations;
import com.example.edu.service.ClassesService;
import com.example.edu.service.CoursesService;
import com.example.edu.service.EvaluationsServices;
import com.example.edu.tool.Validator.Validator;
import com.example.edu.tool.Validator.Exceptions.unknownRuleException;
import com.example.edu.tool.template.TableSingle;

@Controller
@RequestMapping("/admin")
public class AdminEvaluationsController {

    private final EvaluationsServices evalService;
    private final ClassesService classesService;
    private final CoursesService coursesService;

    private final String absoluteURL = "admin/evaluation";
    private TableSingle tableTemplate;

    @Autowired
    public AdminEvaluationsController(EvaluationsServices evalService, ClassesService classesService, CoursesService coursesService) {
        this.evalService = evalService;
        this.classesService = classesService;
        this.coursesService = coursesService;

        // Configuration des colonnes pour le tableau
        Map<String, String> columnToLabel = Map.of(
            "id", "ID",
            "name", "Nom de l'évaluation",
            "student.name", "Nom",
            "student.surname", "Prénom",
            "score", "Note"
        );
        List<String> columnDisplayed = Arrays.asList("id", "name", "student.name", "student.surname", "score");

        this.tableTemplate = new TableSingle("Évaluations", columnToLabel, columnDisplayed);

        // Ajouter les filtres pour "Classe", "Matière", et "Nom de l'évaluation"
        this.tableTemplate.addFilter("Classe");
        this.tableTemplate.addFilter("Matière");
        this.tableTemplate.addFilter("Nom de l'évaluation");
    }

    /**
     * GET - Affiche la liste des évaluations avec filtres Classe, Matière, et Nom de l'évaluation.
     */
    @GetMapping("/evaluation")
    public String listEvaluations(@RequestParam(required = false) Long classeId,
                                   @RequestParam(required = false) Long matiereId,
                                   @RequestParam(required = false) String evaluationName,
                                   Model model) {
        // Récupérer toutes les évaluations
        List<Evaluations> evaluations = evalService.getAll();

        // Initialiser les relations nécessaires
        evaluations.forEach(eval -> {
            Hibernate.initialize(eval.getCourse()); // Initialisation explicite de la relation Course
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

        if (matiereId != null) {
            evaluations = evaluations.stream()
                .filter(eval -> eval.getCourse() != null && eval.getCourse().getId().equals(matiereId))
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

        return "admin/default"; // Vue JSP générique
    }

    /**
     * PUT - Met à jour la note d'une évaluation.
     */
    @PutMapping("/evaluation")
    public String updateEvaluation(@RequestParam MultiValueMap<String, String> request,
                                    Model model,
                                    RedirectAttributes redirectAttributes) throws unknownRuleException {
        // Validation des données reçues
        Validator requestContentValidator = new Validator(Map.of(
            "id", "required|int|min=0",
            "score", "required|int|min=0|max=20"
        ));

        if (requestContentValidator.validateRequest(request)) {
            Long id = Long.parseLong(request.getFirst("id"));
            int newScore = Integer.parseInt(request.getFirst("score"));

            // Récupérer l'évaluation et mettre à jour la note
            Evaluations evaluation = evalService.getById(id).orElse(null);

            if (evaluation != null) {
                evaluation.setScore(newScore);
                evalService.update(evaluation);

                model.addAttribute("message", "La note a été mise à jour.");
                model.addAttribute("messageType", "success");
            } else {
                model.addAttribute("message", "Évaluation introuvable.");
                model.addAttribute("messageType", "error");
            }
        } else {
            model.addAttribute("message", "Des champs sont incorrects ou incomplets.");
            model.addAttribute("messageType", "error");
        }

        tableTemplate.prepareRedirect(model, redirectAttributes);
        return "redirect:/" + absoluteURL;
    }
}
