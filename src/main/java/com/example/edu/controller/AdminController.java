package com.example.edu.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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

import com.example.edu.model.ClassLevel;
import com.example.edu.model.Classes;
import com.example.edu.model.Student;
import com.example.edu.service.ClassLevelService;
import com.example.edu.service.ClassesService;
import com.example.edu.service.StudentService;
import com.example.edu.tool.Security;
import com.example.edu.tool.Table;
import com.example.edu.tool.Validator.Validator;
import com.example.edu.tool.Validator.Exceptions.unknownRuleException;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final StudentService studentService;
    private final ClassLevelService classLevelService;
    private final ClassesService classesService;


    private final  Map<String, String> fieldToLabel = Map.of( //translate attribute name to label name (More readable) [OPTIONAL]
        "id","ID",
        "email","E-mail",
        "surname","Nom",
        "name","Prénom",
        "dateofbirth","Date de naissance",
        "level","Promotion",
        "studentclass","Classe",
        "confirm","Inscription confirmée"
    );

    private final List<String> fieldDisplayedOnLine = Arrays.asList("id", "email", "surname", "name", "confirm"); // fields displayed on the line that represent the data [OPTIONAL]

    @Autowired
    public AdminController(StudentService studentService, ClassLevelService classLevelService, ClassesService classesService) {
        this.studentService = studentService;
        this.classLevelService = classLevelService;
        this.classesService = classesService;
    }

    /*
     * GET - http://localhost:8080/admin/
     * Default page for Admin
     * 
     */
    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("email", Security.getLoggedEmail());
        return "admin/dashboard";  
    }

    /*
     * GET - http://localhost:8080/admin/student
     * Page to manage students datas.
     * 
     */
    @GetMapping("/student")
    public String student(Model model) {

        List<ClassLevel> allLevel = classLevelService.getAllClassLevels();
        List<Classes> allClasses = classesService.getAllClasses();
        model.addAttribute("levelListe", allLevel);
        model.addAttribute("classesListe", allClasses);

        Table.setup(model, "Élèves", new ArrayList<Object>(studentService.getAllStudents()), this.fieldToLabel, this.fieldDisplayedOnLine);
        return "admin/student";  
    }

    
    /*
     * PUT - http://localhost:8080/admin/student
     * update a student.
     * 
     */
    @PutMapping("/student")
    public String updateStudent(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException{

        //Create validator to validate request content
        Validator requestContentValidator = new Validator(Map.of(
            "id", "required|int|min=0", //must be a number 0 min, MANDATORY
            "email", "required|email|max=100", //must be an email 100 char max, MANDATORY
            "surname", "max=100", //must be a string 100 char max, OPTIONAL
            "name", "max=100", //must be a string 100 char max, OPTIONAL
            "dateofbirth", "required|date", //must be a date yyyy-mm-dd, MANDATORY
            "confirm", "required|boolean", //must be a true or false, MANDATORY
            "level", "default=0|int", // default set 0
            "studentclass", "default=0|int" //default set 0
            )
        );

        if( requestContentValidator.validateRequest(request) ){ //validate the request

            Optional<ClassLevel> ocl = classLevelService.getClassLevelsById(Long.parseLong(request.getFirst("level")));
            ClassLevel cl = ocl.isPresent() ? ocl.get() : null;
            Optional<Classes> ocs = classesService.getClassesById(Long.parseLong(request.getFirst("studentclass")));
            Classes oc = ocs.isPresent() ? ocs.get() : null;
            

            Student details = new Student(
                Long.parseLong(request.getFirst("id")),
                request.getFirst("email"), 
                "", 
                request.getFirst("surname"), 
                request.getFirst("name"), 
                cl, 
                request.getFirst("dateofbirth"), 
                oc
            );
            details.setConfirm(Boolean.parseBoolean(request.getFirst("confirm")));

            if(studentService.updateStudent(details) != null){
                //processed
                model.addAttribute("message", "L'élève a été mit à jour.");
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
            Table.setupWithError(model, "", new ArrayList<Object>(studentService.getAllStudents()), this.fieldToLabel, false, requestContentValidator); //setup table to build with error
        }

        // Transfer all attributes from the Model to RedirectAttributes
        for (String attributeName : model.asMap().keySet()) {
            redirectAttributes.addFlashAttribute(attributeName, model.asMap().get(attributeName));
        }

        return "redirect:/admin/student";  
    }

    /*
     * POST - http://localhost:8080/admin/student
     * Page to manage students datas.
     * 
     */
    @PostMapping("/student")
    public String createStudent(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException{

        //Create validator to validate request content
        Validator requestContentValidator = new Validator(Map.of(
            "email", "required|email|max=100", //must be an email 100 char max, MANDATORY
            "surname", "max=100", //must be a string 100 char max, OPTIONAL
            "name", "max=100", //must be a string 100 char max, OPTIONAL
            "dateofbirth", "required|date", //must be a date yyyy-mm-dd, MANDATORY
            "confirm", "required|boolean", //must be a true or false, MANDATORY
            "level", "default=0|int", // default set -1
            "studentclass", "default=0|int" //default set -1
            )
        );

        if( requestContentValidator.validateRequest(request) ){ //validate the request

            Optional<ClassLevel> ocl = classLevelService.getClassLevelsById(Long.parseLong(request.getFirst("level")));
            ClassLevel cl = ocl.isPresent() ? ocl.get() : null;
            Optional<Classes> ocs = classesService.getClassesById(Long.parseLong(request.getFirst("studentclass")));
            Classes oc = ocs.isPresent() ? ocs.get() : null;



            Student details = new Student(
                null,
                request.getFirst("email"), 
                "dateofbirth", 
                request.getFirst("surname"), 
                request.getFirst("name"), 
                cl, 
                request.getFirst("dateofbirth"), 
                oc
            );

            if(studentService.createStudent(details) != null){
                //processed
                model.addAttribute("message", "L'élève a été ajouté.");
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
            Table.setupWithError(model, "", new ArrayList<Object>(studentService.getAllStudents()), this.fieldToLabel, true, requestContentValidator); //setup table to build with error
        }

        // Transfer all attributes from the Model to RedirectAttributes
        for (String attributeName : model.asMap().keySet()) {
            redirectAttributes.addFlashAttribute(attributeName, model.asMap().get(attributeName));
        }

        return "redirect:/admin/student";  
    }

    /*
     * POST - http://localhost:8080/admin/student
     * Page to manage students datas.
     * 
     */
    @DeleteMapping("/student")
    public String deleteStudent(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException{

        //Create validator to validate request content
        Validator requestContentValidator = new Validator(Map.of(
                "id", "required|int|min=0" //must be a number 0 min, MANDATORY
            )
        );

        if( requestContentValidator.validateRequest(request) ){ //validate the request

            studentService.deleteStudent(Long.parseLong(request.getFirst("id")));
            model.addAttribute("message", "L'élève a été supprimé.");
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

        return "redirect:/admin/student";  
    }

    @GetMapping("/ce/lien/nest/pas/suspect")
    public String displayAllStudent(Model model) { 
        return "security";  
    }

}
