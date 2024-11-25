package com.example.edu.controller.admin;

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

import com.example.edu.model.Teacher;
import com.example.edu.model.FieldTeacher;
import com.example.edu.service.FieldService;
import com.example.edu.service.TeacherService;

import com.example.edu.tool.Security;
import com.example.edu.tool.template.TableSingle;
import com.example.edu.tool.Validator.Validator;
import com.example.edu.tool.Validator.Exceptions.unknownRuleException;

@Controller
@RequestMapping("/admin")
public class AdminTeacherController {
    private final TeacherService TeacherService;
    private final FieldService FieldService;

    private TableSingle tableTemplate;

    @Autowired
    public AdminTeacherController(TeacherService TeacherService, FieldService FieldService) {
        this.TeacherService = TeacherService;
        this.FieldService = FieldService;

        Map<String, String> columnToLabel = Map.of( //translate attribute name to label name (More readable) [OPTIONAL]
            "id","ID",
            "email","E-mail",
            "surname","Nom",
            "name","Prénom",
            "field","matière"
        );
        List<String> columnDisplayed = Arrays.asList("id", "email", "surname", "name", "confirm");

        this.tableTemplate = new TableSingle("Professeur", columnToLabel, columnDisplayed);

        this.tableTemplate.setValuesFor("field", this.FieldService::getAllIdxName);
        this.tableTemplate.addFilter("field");
    }

    /*
     * GET - http://localhost:8080/admin/
     * Default page for Admin
     * 
     */
    @GetMapping("/teacher/dashboard")
    public String dashboard(Model model) {
        model.addAttribute("email", Security.getLoggedEmail());
        return "admin/dashboard";  
    }

    /*
     * GET - http://localhost:8080/admin/teachers
     * Page to manage teachers datas.
     * 
     */
    @GetMapping("/teacher")
    public String teacher(Model model) {

        List<FieldTeacher> allLevel = FieldService.getAll();
        model.addAttribute("FieldListe", allLevel);

        tableTemplate.initModel(model, TeacherService.getAllTeachers(), Teacher.class);
        return "admin/teacher";  
    }

    
    /*
     * PUT - http://localhost:8080/admin/teachers
     * update a teacher.
     * 
     */
    @PutMapping("/teacher")
    public String updateTeacher(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException{

        //Create validator to validate request content
        Validator requestContentValidator = new Validator(Map.of(
            "id", "required|int|min=0", //must be a number 0 min, MANDATORY
            "email", "required|email|max=100", //must be an email 100 char max, MANDATORY
            "surname", "max=100", //must be a string 100 char max, OPTIONAL
            "name", "max=100", //must be a string 100 char max, OPTIONAL
            "field", "default=0|int" //must be a date yyyy-mm-dd, MANDATORY
            )
        );

        if( requestContentValidator.validateRequest(request) ){ //validate the request

            Optional<FieldTeacher> ocl = FieldService.getById(Long.parseLong(request.getFirst("field")));
            FieldTeacher cl = ocl.isPresent() ? ocl.get() : null;
            

            Teacher details = new Teacher(
                Long.parseLong(request.getFirst("id")),
                request.getFirst("email"), 
                "", 
                request.getFirst("name"), 
                request.getFirst("surname"), 
                cl
            );

            if(TeacherService.updateTeacher(details) != null){
                //processed
                model.addAttribute("message", "Le prof a été mit à jour.");
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

            tableTemplate.initModel(model, null, Teacher.class, false, requestContentValidator);
        }

        // Transfer all attributes from the Model to RedirectAttributes
        for (String attributeName : model.asMap().keySet()) {
            redirectAttributes.addFlashAttribute(attributeName, model.asMap().get(attributeName));
        }

        return "redirect:/admin/teacher";  
    }

    /*
     * POST - http://localhost:8080/admin/teacher
     * Page to manage teachers datas.
     * 
     */
    @PostMapping("/teacher")
    public String createTeacher(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException{

        //Create validator to validate request content
        Validator requestContentValidator = new Validator(Map.of(
            "email", "required|email|max=100", //must be an email 100 char max, MANDATORY
            "surname", "max=100", //must be a string 100 char max, OPTIONAL
            "name", "max=100", //must be a string 100 char max, OPTIONAL
            "field", "default=0|int" //must be a date yyyy-mm-dd, MANDATORY
            )
        );

        if( requestContentValidator.validateRequest(request) ){ //validate the request

            Optional<FieldTeacher> ocl = FieldService.getById(Long.parseLong(request.getFirst("field")));
            FieldTeacher cl = ocl.isPresent() ? ocl.get() : null;



            Teacher details = new Teacher(
                null,
                request.getFirst("email"), 
                "", 
                request.getFirst("name"), 
                request.getFirst("surname"), 
                cl
            );

            if(TeacherService.createTeacher(details) != null){
                //processed
                model.addAttribute("message", "Le prof a été ajouté.");
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

        return "redirect:/admin/teacher";  
    }

    /*
     * POST - http://localhost:8080/admin/teacher
     * Page to manage teachers datas.
     * 
     */
    @DeleteMapping("/teacher")
    public String deleteTeacher(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException{

        //Create validator to validate request content
        Validator requestContentValidator = new Validator(Map.of(
                "id", "required|int|min=0" //must be a number 0 min, MANDATORY
            )
        );

        if( requestContentValidator.validateRequest(request) ){ //validate the request

            TeacherService.deleteTeacher(Long.parseLong(request.getFirst("id")));
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

        return "redirect:/admin/teacher";  
    }

}
