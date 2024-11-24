package com.example.edu.controller.admin.config;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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

import com.example.edu.model.Program;
import com.example.edu.service.ProgramService;
import com.example.edu.tool.Validator.Validator;
import com.example.edu.tool.Validator.Exceptions.unknownRuleException;
import com.example.edu.tool.template.TableSingle;

@Controller
@RequestMapping("/admin/config")
public class AdminProgramController {

    private final ProgramService progamService;
    private final String absoluteURL = "admin/config/program";
    private TableSingle tableTemplate;

    @Autowired
    public AdminProgramController(ProgramService progamService) {
        this.progamService = progamService;

        Map<String, String> columnToLabel = Map.of( //translate attribute name to label name (More readable) [OPTIONAL]
            "id","ID",
            "name","Nom"
        );
        List<String> columnDisplayed = Arrays.asList("id", "name");
        this.tableTemplate = new TableSingle("Cursus", columnToLabel, columnDisplayed);
    }
    
    /*
     * GET - http://localhost:8080/admin/student
     * Page to manage students datas.
     * 
     */
    @GetMapping("/program")
    public String get(Model model) {

        tableTemplate.initModel(model, this.progamService.getAll(), Program.class);
        return "admin/default";  
    }

    
    /*
     * PUT - http://localhost:8080/admin/student
     * update a student.
     * 
     */
    @PutMapping("/program")
    public String put(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException{

        //Create validator to validate request content
        Validator requestContentValidator = new Validator(Map.of(
            "id", "required|int|min=0",
            "name", "required|max=100"
            )
        );

        if( requestContentValidator.validateRequest(request) ){ //validate the request

            

            Program details = new Program(
                Long.parseLong(request.getFirst("id")),
                request.getFirst("name")
            );


            if(this.progamService.update(details) != null){
                //processed
                model.addAttribute("message", "Le cursus a été mit à jour.");
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

            tableTemplate.initModel(model, null, Program.class, false, requestContentValidator);
        }

        tableTemplate.prepareRedirect(model, redirectAttributes);
        return "redirect:/"+absoluteURL;  
    }

    /*
     * POST - http://localhost:8080/admin/student
     * Page to manage students datas.
     * 
     */
    @PostMapping("/program")
    public String post(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException{

        //Create validator to validate request content
        Validator requestContentValidator = new Validator(Map.of(
            "name", "required|max=100"
            )
        );

        if( requestContentValidator.validateRequest(request) ){ //validate the request
            Program details = new Program(
                null,
                request.getFirst("name")
            );

            if(this.progamService.create(details) != null){
                //processed
                model.addAttribute("message", "Le cursus a été ajoutée.");
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

            tableTemplate.initModel(model, null, Program.class, false, requestContentValidator);
        }

        tableTemplate.prepareRedirect(model, redirectAttributes);
        return "redirect:/"+absoluteURL;  
    }

    /*
     * POST - http://localhost:8080/admin/student
     * Page to manage students datas.
     * 
     */
    @DeleteMapping("/program")
    public String delete(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException{

        //Create validator to validate request content
        Validator requestContentValidator = new Validator(Map.of(
                "id", "required|int|min=0" //must be a number 0 min, MANDATORY
            )
        );

        if( requestContentValidator.validateRequest(request) ){ //validate the request

            this.progamService.delete(Long.parseLong(request.getFirst("id")));
            model.addAttribute("message", "Le cursus a été supprimé.");
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
