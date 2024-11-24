package com.example.edu.controller.admin.config;

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
import com.example.edu.model.Program;
import com.example.edu.service.ClassLevelService;
import com.example.edu.service.ClassesService;
import com.example.edu.service.ProgramService;
import com.example.edu.tool.Validator.Validator;
import com.example.edu.tool.Validator.Exceptions.unknownRuleException;
import com.example.edu.tool.template.TableSingle;

@Controller
@RequestMapping("/admin/config")
public class AdminClassesController {
    private final ClassesService classService;
    private final ClassLevelService clService;
    private final ProgramService programService;
    private final String absoluteURL = "admin/config/class";
    private TableSingle tableTemplate;

     @Autowired
    public AdminClassesController(ClassesService classService, ClassLevelService clService, ProgramService programService) {
        this.classService = classService;
        this.clService = clService;
        this.programService = programService;

        Map<String, String> columnToLabel = Map.of( //translate attribute name to label name (More readable) [OPTIONAL]
            "id","ID",
            "name","Nom",
            "program","Cursus",
            "level","Promotion"
        );
        List<String> columnDisplayed = Arrays.asList("id", "name", "program", "level");

        this.tableTemplate = new TableSingle("Classes", columnToLabel, columnDisplayed);

        this.tableTemplate.setValuesFor("program", this.programService::getAllIdxName);
        this.tableTemplate.setValuesFor("level", this.clService::getAllIdxName);
        this.tableTemplate.addFilter("program");
        this.tableTemplate.addFilter("level");
        this.tableTemplate.addLink("program", "level", this.programService::getAllLevel); 
    }

    /*
     * GET - http://localhost:8080/admin/student
     * Page to manage students datas.
     * 
     */
    @GetMapping("/class")
    public String get(Model model) {

        tableTemplate.initModel(model, this.classService.getAll(), Classes.class);
        return "admin/default";  
    }

    
    /*
     * PUT - http://localhost:8080/admin/student
     * update a student.
     * 
     */
    @PutMapping("/class")
    public String put(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException{

        //Create validator to validate request content
        Validator requestContentValidator = new Validator(Map.of(
            "id", "required|int|min=0",
            "name", "default=NULL|max=100",
            "program", "required|int|min=0",
            "level", "default=0|int"
            )
        );

        if( requestContentValidator.validateRequest(request) ){ //validate the request

            Optional<Program> programOp = this.programService.getById(Long.parseLong(request.getFirst("program")));
            Program program = programOp.isPresent() ? programOp.get() : null;
            Optional<ClassLevel> levelOp = this.clService.getById(Long.parseLong(request.getFirst("level")));
            ClassLevel level = levelOp.isPresent() ? levelOp.get() : null;

            if(program == null){
                //failed validation
                model.addAttribute("message", "Le cursus n'existe pas.");
                model.addAttribute("messageType", "error");
                tableTemplate.initModel(model, null, Classes.class, false, requestContentValidator);
            }
            else{

                List<Long> possibleLevel = this.programService.getAllLevel().get(program.getId());

                Classes details = new Classes(
                    Long.parseLong(request.getFirst("id")),
                    request.getFirst("name"), 
                    program,
                    level == null ? null : (possibleLevel.contains(level.getId()) ? level : null)
                );


    
    
                if(this.classService.update(details) != null){
                    //processed
                    model.addAttribute("message", "La classe a été mit à jour.");
                    model.addAttribute("messageType", "success");
                }
                else{
                    //unexcepted error in process
                    model.addAttribute("message", "Une erreur est survenue :/");
                    model.addAttribute("messageType", "warning");
                }
            }


        } 
        else{
            //failed validation
            model.addAttribute("message", "Des champs sont incorrect ou incomplet.");
            model.addAttribute("messageType", "error");

            tableTemplate.initModel(model, null, Classes.class, false, requestContentValidator);
        }

        tableTemplate.prepareRedirect(model, redirectAttributes);
        return "redirect:/"+absoluteURL;  
    }

    /*
     * POST - http://localhost:8080/admin/student
     * Page to manage students datas.
     * 
     */
    @PostMapping("/class")
    public String post(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException{

        //Create validator to validate request content
        Validator requestContentValidator = new Validator(Map.of(
            "name", "default=NULL|max=100",
            "program", "required|int|min=0",
            "level", "default=0|int"
            )
        );

        if( requestContentValidator.validateRequest(request) ){ //validate the request

            Optional<Program> programOp = this.programService.getById(Long.parseLong(request.getFirst("program")));
            Program program = programOp.isPresent() ? programOp.get() : null;
            Optional<ClassLevel> levelOp = this.clService.getById(Long.parseLong(request.getFirst("level")));
            ClassLevel level = levelOp.isPresent() ? levelOp.get() : null;

            if(program == null){
                //failed validation
                model.addAttribute("message", "Le cursus n'existe pas.");
                model.addAttribute("messageType", "error");
                tableTemplate.initModel(model, null, Classes.class, false, requestContentValidator);
            }
            else{

                List<Long> possibleLevel = this.programService.getAllLevel().get(program.getId());

                Classes details = new Classes(
                    null,
                    request.getFirst("name"), 
                    program,
                    level == null ? null : (possibleLevel.contains(level.getId()) ? level : null)
                );
    
    
                if(this.classService.create(details) != null){
                    //processed
                    model.addAttribute("message", "La classe a été ajouté.");
                    model.addAttribute("messageType", "success");
                }
                else{
                    //unexcepted error in process
                    model.addAttribute("message", "Une erreur est survenue :/");
                    model.addAttribute("messageType", "warning");
                }
            }

        } 
        else{
            //failed validation
            model.addAttribute("message", "Des champs sont incorrect ou incomplet.");
            model.addAttribute("messageType", "error");

            tableTemplate.initModel(model, null, Classes.class, false, requestContentValidator);
        }

        tableTemplate.prepareRedirect(model, redirectAttributes);
        return "redirect:/"+absoluteURL;  
    }

    /*
     * POST - http://localhost:8080/admin/student
     * Page to manage students datas.
     * 
     */
    @DeleteMapping("/class")
    public String delete(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException{

        //Create validator to validate request content
        Validator requestContentValidator = new Validator(Map.of(
                "id", "required|int|min=0" //must be a number 0 min, MANDATORY
            )
        );

        if( requestContentValidator.validateRequest(request) ){ //validate the request

            this.classService.delete(Long.parseLong(request.getFirst("id")));
            model.addAttribute("message", "La classe a été supprimé.");
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
