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

// Marks this class as a Spring MVC controller and maps requests with the prefix "/admin/config".
@Controller
@RequestMapping("/admin/config")
public class AdminClassesController {

    // Dependencies for services used in this controller.
    private final ClassesService classService;
    private final ClassLevelService clService;
    private final ProgramService programService;

    // URL segment used in redirections for this controller.
    private final String absoluteURL = "admin/config/class";

    // Template for displaying table data in the view.
    private TableSingle tableTemplate;

    // Constructor with @Autowired for dependency injection.
    @Autowired
    public AdminClassesController(ClassesService classService, ClassLevelService clService, ProgramService programService) {
        this.classService = classService;
        this.clService = clService;
        this.programService = programService;

        // Configuration for the table template: mapping column names to labels and defining display rules.
        Map<String, String> columnToLabel = Map.of(
            "id", "ID",
            "name", "Nom",
            "program", "Cursus",
            "level", "Promotion"
        );
        List<String> columnDisplayed = Arrays.asList("id", "name", "program", "level");

        // Initialize the table template with column labels and display rules.
        this.tableTemplate = new TableSingle("Classes", columnToLabel, columnDisplayed);

        // Set values and filters for dropdowns in the table.
        this.tableTemplate.setValuesFor("program", this.programService::getAllIdxName);
        this.tableTemplate.setValuesFor("level", this.clService::getAllIdxName);
        this.tableTemplate.addFilter("program");
        this.tableTemplate.addFilter("level");

        // Add a link to dynamically populate levels based on the selected program.
        this.tableTemplate.addLink("program", "level", this.programService::getAllLevel);
    }

    /*
     * Handles GET requests to "/admin/config/class".
     * Renders the default admin page for managing classes.
     */
    @GetMapping("/class")
    public String get(Model model) {
        // Populate the model with class data and table template configuration.
        tableTemplate.initModel(model, this.classService.getAll(), Classes.class);
        return "admin/default"; // Return the view for rendering.
    }

    /*
     * Handles PUT requests to "/admin/config/class".
     * Updates class information based on the request parameters.
     */
    @PutMapping("/class")
    public String put(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) 
            throws unknownRuleException {

        // Validator to enforce input validation rules.
        Validator requestContentValidator = new Validator(Map.of(
            "id", "required|int|min=0", // ID is required and must be a non-negative integer.
            "name", "default=NULL|max=100", // Name is optional but must be <= 100 characters.
            "program", "required|int|min=0", // Program ID is required and must be a non-negative integer.
            "level", "default=0|int" // Level is optional and must be a non-negative integer.
        ));

        // Validate the incoming request parameters.
        if (requestContentValidator.validateRequest(request)) {
            // Fetch the program and level based on IDs provided in the request.
            Optional<Program> programOp = this.programService.getById(Long.parseLong(request.getFirst("program")));
            Program program = programOp.orElse(null);
            Optional<ClassLevel> levelOp = this.clService.getById(Long.parseLong(request.getFirst("level")));
            ClassLevel level = levelOp.orElse(null);

            if (program == null) {
                // If program does not exist, add an error message to the model.
                model.addAttribute("message", "Le cursus n'existe pas.");
                model.addAttribute("messageType", "error");
                tableTemplate.initModel(model, null, Classes.class, false, requestContentValidator);
            } else {
                // Validate the level against the program's allowed levels.
                List<Long> possibleLevel = this.programService.getAllLevel().get(program.getId());
                Classes details = new Classes(
                    Long.parseLong(request.getFirst("id")),
                    request.getFirst("name"),
                    program,
                    level == null ? null : (possibleLevel.contains(level.getId()) ? level : null)
                );

                // Attempt to update the class information.
                if (this.classService.update(details) != null) {
                    model.addAttribute("message", "La classe a été mit à jour.");
                    model.addAttribute("messageType", "success");
                } else {
                    model.addAttribute("message", "Une erreur est survenue :/");
                    model.addAttribute("messageType", "warning");
                }
            }
        } else {
            // Validation failed, return error messages.
            model.addAttribute("message", "Des champs sont incorrect ou incomplet.");
            model.addAttribute("messageType", "error");
            tableTemplate.initModel(model, null, Classes.class, false, requestContentValidator);
        }

        tableTemplate.prepareRedirect(model, redirectAttributes);
        return "redirect:/" + absoluteURL;
    }

    /*
     * Handles POST requests to "/admin/config/class".
     * Creates a new class based on the request parameters.
     */
    @PostMapping("/class")
    public String post(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException{

        // Similar validation and logic as the PUT method.
        // The difference is creating a new class instead of updating an existing one.
        Validator requestContentValidator = new Validator(Map.of(
            "name", "default=NULL|max=100",
            "program", "required|int|min=0",
            "level", "default=0|int"
            )
        );

        if( requestContentValidator.validateRequest(request) ){

            Optional<Program> programOp = this.programService.getById(Long.parseLong(request.getFirst("program")));
            Program program = programOp.isPresent() ? programOp.get() : null;
            Optional<ClassLevel> levelOp = this.clService.getById(Long.parseLong(request.getFirst("level")));
            ClassLevel level = levelOp.isPresent() ? levelOp.get() : null;

            if(program == null){
                
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
                    
                    model.addAttribute("message", "La classe a été ajouté.");
                    model.addAttribute("messageType", "success");
                }
                else{
                    
                    model.addAttribute("message", "Une erreur est survenue :/");
                    model.addAttribute("messageType", "warning");
                }
            }

        } 
        else{
            
            model.addAttribute("message", "Des champs sont incorrect ou incomplet.");
            model.addAttribute("messageType", "error");

            tableTemplate.initModel(model, null, Classes.class, false, requestContentValidator);
        }

        tableTemplate.prepareRedirect(model, redirectAttributes);
        return "redirect:/"+absoluteURL;  
    }

    /*
     * Handles DELETE requests to "/admin/config/class".
     * Deletes a class based on the ID provided in the request parameters.
     */
    @DeleteMapping("/class")
    public String delete(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) 
            throws unknownRuleException {

        // Validate the input for the class ID to delete.
        Validator requestContentValidator = new Validator(Map.of(
            "id", "required|int|min=0" // ID must be a non-negative integer and is required.
        ));

        if (requestContentValidator.validateRequest(request)) {
            // Perform the delete operation.
            this.classService.delete(Long.parseLong(request.getFirst("id")));
            model.addAttribute("message", "La classe a été supprimé.");
            model.addAttribute("messageType", "success");
        } else {
            model.addAttribute("message", "Des champs sont incorrect ou incomplet.");
            model.addAttribute("messageType", "error");
        }

        tableTemplate.prepareRedirect(model, redirectAttributes);
        return "redirect:/" + absoluteURL;
    }
}
