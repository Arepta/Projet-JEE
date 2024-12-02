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

import com.example.edu.model.Courses;
import com.example.edu.model.Program;
import com.example.edu.model.ProgramsContent;
import com.example.edu.service.CoursesService;
import com.example.edu.service.ProgramService;
import com.example.edu.service.ProgramsContentService;
import com.example.edu.tool.Validator.Validator;
import com.example.edu.tool.Validator.Exceptions.unknownRuleException;
import com.example.edu.tool.template.TableSingle;

@Controller
@RequestMapping("/admin/courses")
public class AdminProgramsContentController {

    private final CoursesService coursesService;
    private final ProgramService programService;
    private final ProgramsContentService programsContentService;

    private final String absoluteURL = "admin/courses/program"; // URL for managing programs content
    private TableSingle tableTemplate; // Template for displaying the content of programs in a table

    // Constructor to inject the required services and initialize the table template
    @Autowired
    public AdminProgramsContentController(CoursesService coursesService, ProgramService programService, ProgramsContentService programsContentService) {
        this.coursesService = coursesService;
        this.programService = programService;
        this.programsContentService = programsContentService;

        // Map to translate column attribute names to more readable labels
        Map<String, String> columnToLabel = Map.of( 
            "program", "Cursus",    // Program name/identifier
            "course", "Cours"       // Course name/identifier
        );

        // Columns to display in the table
        List<String> columnDisplayed = Arrays.asList("program", "course");
        this.tableTemplate = new TableSingle("Contenu des cursus", columnToLabel, columnDisplayed);

        // Set values for 'course' and 'program' dropdown filters (fetched from corresponding services)
        this.tableTemplate.setValuesFor("course", this.coursesService::getAllIdxName);
        this.tableTemplate.setValuesFor("program", this.programService::getAllIdxName);

        // Add filters for 'course' and 'program' to the table
        this.tableTemplate.addFilter("program");
        this.tableTemplate.addFilter("course");
    }

    /*
     * GET Mapping - Display all the programs content
     * The URL is: http://localhost:8080/admin/courses/program
     */
    @GetMapping("/program")
    public String get(Model model) {
        // Initialize the table with programs content data and display it
        tableTemplate.initModel(model, this.programsContentService.getAll(), ProgramsContent.class);
        return "admin/default"; // Return the view to be rendered
    }

    /*
     * PUT Mapping - Handle update request (not implemented, only displays a warning)
     * The URL is: http://localhost:8080/admin/courses/program
     */
    @PutMapping("/program")
    public String put(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException {

        // Display a warning message (since updates are not allowed, only add or delete)
        model.addAttribute("message", "Vous ne pouvez que supprimer ou ajouter.");
        model.addAttribute("messageType", "warning");

        // Prepare for redirect, passing any necessary model attributes
        tableTemplate.prepareRedirect(model, redirectAttributes);
        return "redirect:/"+absoluteURL;  // Redirect to the programs content management page
    }

    /*
     * POST Mapping - Add a new program content (course to program)
     * The URL is: http://localhost:8080/admin/courses/program
     */
    @PostMapping("/program")
    public String post(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException {

        // Validator to ensure the request has valid program and course IDs
        Validator requestContentValidator = new Validator(Map.of(
            "program", "required|int|min=0",    // Program ID must be provided, must be an integer, and at least 0
            "course", "required|int|min=0"      // Course ID must be provided, must be an integer, and at least 0
        ));

        // Validate the request
        if (requestContentValidator.validateRequest(request)) {

            // Fetch the program and course by their IDs (if valid)
            Optional<Program> ProgramOp = this.programService.getById(Long.parseLong(request.getFirst("program")));
            Program program = ProgramOp.isPresent() ? ProgramOp.get() : null;
            Optional<Courses> CoursesOp = this.coursesService.getById(Long.parseLong(request.getFirst("course")));
            Courses course = CoursesOp.isPresent() ? CoursesOp.get() : null;

            // Create a new ProgramsContent object with the provided program and course
            ProgramsContent details = new ProgramsContent(program, course);

            // Validation checks: Ensure that the program and course are not null and the content doesn't already exist
            if (program == null || course == null) {
                model.addAttribute("message", "Des champs sont incorrects ou incomplets.");
                model.addAttribute("messageType", "error");
                tableTemplate.initModel(model, null, ProgramsContent.class, true, requestContentValidator);
            } else if (this.programsContentService.getById(details.getId()).isPresent()) {
                model.addAttribute("message", "Le cours existe deja pour ce cursus");
                model.addAttribute("messageType", "error");
                tableTemplate.initModel(model, null, ProgramsContent.class, true, requestContentValidator);
            } else if (this.programsContentService.create(details) != null) {
                // If the program content is successfully created
                model.addAttribute("message", "Le contenu a été ajouté.");
                model.addAttribute("messageType", "success");
            } else {
                // Unexpected error during the process
                model.addAttribute("message", "Une erreur est survenue :/");
                model.addAttribute("messageType", "warning");
            }

        } else {
            // Validation failed: Display error message and reinitialize the table with validation errors
            model.addAttribute("message", "Des champs sont incorrects ou incomplets.");
            model.addAttribute("messageType", "error");
            tableTemplate.initModel(model, null, ProgramsContent.class, true, requestContentValidator);
        }

        // Prepare for redirect, passing any necessary model attributes
        tableTemplate.prepareRedirect(model, redirectAttributes);
        return "redirect:/"+absoluteURL;  // Redirect to the programs content management page
    }

    /*
     * DELETE Mapping - Delete a program content (remove course from program)
     * The URL is: http://localhost:8080/admin/courses/program
     */
    @DeleteMapping("/program")
    public String delete(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException {

        // Validator to ensure the request has valid program and course IDs
        Validator requestContentValidator = new Validator(Map.of(
            "program", "required|int|min=0",    // Program ID must be provided, must be an integer, and at least 0
            "course", "required|int|min=0"      // Course ID must be provided, must be an integer, and at least 0
        ));

        // Validate the request
        if (requestContentValidator.validateRequest(request)) {

            // Fetch the program and course by their IDs (if valid)
            Optional<Program> ProgramOp = this.programService.getById(Long.parseLong(request.getFirst("program")));
            Program program = ProgramOp.isPresent() ? ProgramOp.get() : null;
            Optional<Courses> CoursesOp = this.coursesService.getById(Long.parseLong(request.getFirst("course")));
            Courses course = CoursesOp.isPresent() ? CoursesOp.get() : null;

            // Create a new ProgramsContent object with the provided program and course
            ProgramsContent details = new ProgramsContent(program, course);

            // Validation check: Ensure that the program and course are not null
            if (program == null || course == null) {
                model.addAttribute("message", "Des champs sont incorrects ou incomplets.");
                model.addAttribute("messageType", "error");
                tableTemplate.initModel(model, null, ProgramsContent.class, false, requestContentValidator);
            } else {
                // Delete the program content if it exists
                this.programsContentService.delete(details.getId());
            }

            // Success message after deletion
            model.addAttribute("message", "Le cours a été supprimé.");
            model.addAttribute("messageType", "success");

        } else {
            // Validation failed: Display error message
            model.addAttribute("message", "Des champs sont incorrects ou incomplets.");
            model.addAttribute("messageType", "error");
        }

        // Prepare for redirect, passing any necessary model attributes
        tableTemplate.prepareRedirect(model, redirectAttributes);
        return "redirect:/"+absoluteURL;  // Redirect to the programs content management page
    }
}
