package com.example.edu.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.edu.model.Courses;
import com.example.edu.model.Evaluations;
import com.example.edu.model.Student;
import com.example.edu.model.Teacher;
import com.example.edu.service.ClassesService;
import com.example.edu.service.CoursesService;
import com.example.edu.service.StudentService;
import com.example.edu.service.evaluationsServices;
import com.example.edu.tool.Validator.Validator;
import com.example.edu.tool.Validator.Exceptions.unknownRuleException;
import com.example.edu.tool.template.TableSingle;

@Controller
@RequestMapping("/teacher")
public class TeacherEvaluationController {

    private final evaluationsServices evalService;
    private final ClassesService classesService;
    private final CoursesService coursesService;
    private final StudentService studentService;

    private TableSingle tableTemplate;

    @Autowired
    public TeacherEvaluationController(evaluationsServices evalService, ClassesService classesService, CoursesService coursesService, StudentService studentService) {
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
                                   Model model) {
        // Récupérer toutes les évaluations
        List<Evaluations> evaluations = evalService.getAll();

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

        return "teacher/default"; // Vue JSP générique
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
        return "redirect:/teacher/evaluation";
    }

    /*
     * POST - http://localhost:8080/teacher/evalutaion
     * Page to manage evaluation datas.
     * 
     */
    @PostMapping("/evaluation")
    public String createEvaluations(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException{

        //Create validator to validate request content
        Validator requestContentValidator = new Validator(Map.of(
            "name", "max=100",
            "score", "required|int|min=0|max=20",
            "minscore", "required|int|min=0|max=20",
            "maxscore", "required|int|min=0|max=20"
            )
        );

        if( requestContentValidator.validateRequest(request) ){ //validate the request

            Optional<Student> ocl = studentService.getById(Long.parseLong(request.getFirst("student")));
            Student cl = ocl.isPresent() ? ocl.get() : null;

            Optional<Courses> ucl = coursesService.getById(Long.parseLong(request.getFirst("course")));
            Courses dl = ucl.isPresent() ? ucl.get() : null;

            Evaluations details = new Evaluations(
                null,
                request.getFirst("name"), 
                Integer.parseInt(request.getFirst("minscore")) , 
                Integer.parseInt(request.getFirst("maxscore")), 
                Integer.parseInt(request.getFirst("score")),
                dl,
                cl
            );

            if(evalService.createEvaluations(details) != null){
                //processed
                model.addAttribute("message", "L'éval a été ajouté.");
                model.addAttribute("messageType", "success");
            }
            else{
                //unexcepted error in process
                model.addAttribute("message", "Une erreur est survenue :/");
                model.addAttribute("messageType", "warning");
            }
        } 
        else{
            //failed validation
            model.addAttribute("message", "Des champs sont incorrect ou incomplet.");
            model.addAttribute("messageType", "error");

            tableTemplate.initModel(model, null, Teacher.class, true, requestContentValidator);
        }

        // Transfer all attributes from the Model to RedirectAttributes
        for (String attributeName : model.asMap().keySet()) {
            redirectAttributes.addFlashAttribute(attributeName, model.asMap().get(attributeName));
        }

        return "redirect:/teacher/evaluation";  
    }

    @DeleteMapping("/evaluation")
    public String deleteEvaluation(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException{

        //Create validator to validate request content
        Validator requestContentValidator = new Validator(Map.of(
                "id", "required|int|min=0" //must be a number 0 min, MANDATORY
            )
        );

        if( requestContentValidator.validateRequest(request) ){ //validate the request

            evalService.delete(Long.parseLong(request.getFirst("id")));
            model.addAttribute("message", "Le prof a été supprimé.");
            model.addAttribute("messageType", "success");
 
        } 
        else{
            //failed validation
            model.addAttribute("message", "Des champs sont incorrect ou incomplet.");
            model.addAttribute("messageType", "error");
        }

        // Transfer all attributes from the Model to RedirectAttributes
        for (String attributeName : model.asMap().keySet()) {
            redirectAttributes.addFlashAttribute(attributeName, model.asMap().get(attributeName));
        }

        return "redirect:/teacher/evaluation";  
    }
}
