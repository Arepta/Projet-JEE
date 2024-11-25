package com.example.edu.controller.admin.courses;

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
import com.example.edu.model.Courses;
import com.example.edu.model.FieldTeacher;
import com.example.edu.service.ClassLevelService;
import com.example.edu.service.CoursesService;
import com.example.edu.service.FieldService;
import com.example.edu.tool.Validator.Validator;
import com.example.edu.tool.Validator.Exceptions.unknownRuleException;
import com.example.edu.tool.template.TableSingle;

@Controller
@RequestMapping("/admin/courses")
public class AdminCoursesController {

    private final CoursesService coursesService;
    private final FieldService fieldService;
    private final ClassLevelService clService;

    private final String absoluteURL = "admin/courses/courses";
    private TableSingle tableTemplate;


    @Autowired
    public AdminCoursesController(CoursesService coursesService, FieldService fieldService, ClassLevelService clService) {
        this.coursesService = coursesService;
        this.fieldService = fieldService;
        this.clService = clService;

        Map<String, String> columnToLabel = Map.of( //translate attribute name to label name (More readable) [OPTIONAL]
            "id","ID",
            "name","Nom",
            "level","Promotion",
            "field","Domaine"
        );
        List<String> columnDisplayed = Arrays.asList("id", "name", "level", "field");
        this.tableTemplate = new TableSingle("Cours", columnToLabel, columnDisplayed);

        this.tableTemplate.setValuesFor("level", this.clService::getAllIdxName);
        this.tableTemplate.setValuesFor("field", this.fieldService::getAllIdxName);
        this.tableTemplate.addFilter("level");
        this.tableTemplate.addFilter("field");
    }

    /*
     * GET - http://localhost:8080/admin/student
     * Page to manage students datas.
     * 
     */
    @GetMapping("/courses")
    public String get(Model model) {

        tableTemplate.initModel(model, this.coursesService.getAll(), Courses.class);
        return "admin/default";  
    }

    
    /*
     * PUT - http://localhost:8080/admin/student
     * update a student.
     * 
     */
    @PutMapping("/courses")
    public String put(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException{

        //Create validator to validate request content
        Validator requestContentValidator = new Validator(Map.of(
            "id", "required|int|min=0",
            "name", "default=NULL|max=100",
            "field", "default=0|int",
            "level", "default=0|int"
            )
        );

        if( requestContentValidator.validateRequest(request) ){ //validate the request

            Optional<FieldTeacher> fieldOp = this.fieldService.getById(Long.parseLong(request.getFirst("field")));
            FieldTeacher field = fieldOp.isPresent() ? fieldOp.get() : null;
            Optional<ClassLevel> levelOp = this.clService.getById(Long.parseLong(request.getFirst("level")));
            ClassLevel level = levelOp.isPresent() ? levelOp.get() : null;

            Courses details = new Courses(
                Long.parseLong(request.getFirst("id")),
                request.getFirst("name"), 
                level,
                field
            );


            if(this.coursesService.update(details) != null){
                //processed
                model.addAttribute("message", "Le cours a été mit à jour.");
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

            tableTemplate.initModel(model, null, Courses.class, false, requestContentValidator);
        }

        tableTemplate.prepareRedirect(model, redirectAttributes);
        return "redirect:/"+absoluteURL;  
    }

    /*
     * POST - http://localhost:8080/admin/student
     * Page to manage students datas.
     * 
     */
    @PostMapping("/courses")
    public String post(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException{

        //Create validator to validate request content
        Validator requestContentValidator = new Validator(Map.of(
            "name", "default=NULL|max=100",
            "field", "default=0|int",
            "level", "default=0|int"
            )
        );

        if( requestContentValidator.validateRequest(request) ){ //validate the request

            Optional<FieldTeacher> fieldOp = this.fieldService.getById(Long.parseLong(request.getFirst("field")));
            FieldTeacher field = fieldOp.isPresent() ? fieldOp.get() : null;
            Optional<ClassLevel> levelOp = this.clService.getById(Long.parseLong(request.getFirst("level")));
            ClassLevel level = levelOp.isPresent() ? levelOp.get() : null;

            Courses details = new Courses(
                null,
                request.getFirst("name"), 
                level,
                field
            );
    
    
            if(this.coursesService.create(details) != null){
                //processed
                model.addAttribute("message", "Le cours a été ajouté.");
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

            tableTemplate.initModel(model, null, Courses.class, false, requestContentValidator);
        }

        tableTemplate.prepareRedirect(model, redirectAttributes);
        return "redirect:/"+absoluteURL;  
    }

    /*
     * POST - http://localhost:8080/admin/student
     * Page to manage students datas.
     * 
     */
    @DeleteMapping("/courses")
    public String delete(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException{

        //Create validator to validate request content
        Validator requestContentValidator = new Validator(Map.of(
                "id", "required|int|min=0" //must be a number 0 min, MANDATORY
            )
        );

        if( requestContentValidator.validateRequest(request) ){ //validate the request

            this.coursesService.delete(Long.parseLong(request.getFirst("id")));
            model.addAttribute("message", "Le cours a été supprimé.");
            model.addAttribute("messageType", "success");
 
        } 
        else{
            //failed validation
            model.addAttribute("message", "Des champs sont incorrect ou incomplet.");
            model.addAttribute("messageType", "error");
        }

        tableTemplate.prepareRedirect(model, redirectAttributes);
        return "redirect:/"+absoluteURL;  
    }

}
