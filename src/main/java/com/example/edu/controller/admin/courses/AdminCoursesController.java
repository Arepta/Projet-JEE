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

    private final String absoluteURL = "admin/courses/courses"; // URL for course management page
    private TableSingle tableTemplate; // Template for displaying courses in a table

    // Constructor to inject services and initialize table template
    @Autowired
    public AdminCoursesController(CoursesService coursesService, FieldService fieldService, ClassLevelService clService) {
        this.coursesService = coursesService;
        this.fieldService = fieldService;
        this.clService = clService;

        // Map to translate column attribute names to more readable labels
        Map<String, String> columnToLabel = Map.of(
            "id", "ID",          // ID of the course
            "name", "Nom",       // Name of the course
            "level", "Promotion", // Level (Class Level) of the course
            "field", "Domaine"   // Field/Discipline of the course
        );

        // Columns that will be displayed in the table
        List<String> columnDisplayed = Arrays.asList("id", "name", "level", "field");

        // Initialize the table template with the title, column labels, and columns to display
        this.tableTemplate = new TableSingle("Cours", columnToLabel, columnDisplayed);

        // Set values for 'level' and 'field' dropdown filters (retrieved from corresponding services)
        this.tableTemplate.setValuesFor("level", this.clService::getAllIdxName);
        this.tableTemplate.setValuesFor("field", this.fieldService::getAllIdxName);

        // Add filters for 'level' and 'field' to the table
        this.tableTemplate.addFilter("level");
        this.tableTemplate.addFilter("field");
    }

    /*
     * GET Mapping - http://localhost:8080/admin/courses/courses
     * This method handles the request to display all the courses.
     */
    @GetMapping("/courses")
    public String get(Model model) {
        // Initialize the table with data fetched from the coursesService and display the courses
        tableTemplate.initModel(model, this.coursesService.getAll(), Courses.class);
        return "admin/default"; // Return the view to be rendered
    }

    /*
     * PUT Mapping - Update an existing course.
     * The URL is: http://localhost:8080/admin/courses/courses
     */
    @PutMapping("/courses")
    public String put(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException {
        
        // Validator to check if the request parameters are valid (ID, name, field, and level)
        Validator requestContentValidator = new Validator(Map.of(
            "id", "required|int|min=0",          // ID must be provided, must be an integer, and at least 0
            "name", "default=NULL|max=100",      // Name is optional but if provided, it should be less than 100 characters
            "field", "default=0|int",            // Field is optional, should be an integer
            "level", "default=0|int"             // Level is optional, should be an integer
        ));

        // Validate the request
        if (requestContentValidator.validateRequest(request)) {

            // Fetch field and level data by their IDs (if valid)
            Optional<FieldTeacher> fieldOp = this.fieldService.getById(Long.parseLong(request.getFirst("field")));
            FieldTeacher field = fieldOp.isPresent() ? fieldOp.get() : null; // Field may be null if not found
            Optional<ClassLevel> levelOp = this.clService.getById(Long.parseLong(request.getFirst("level")));
            ClassLevel level = levelOp.isPresent() ? levelOp.get() : null; // Level may be null if not found

            // Create a new Courses object with the data from the request
            Courses details = new Courses(
                Long.parseLong(request.getFirst("id")), // Course ID
                request.getFirst("name"),               // Course name
                level,                                  // Class level
                field                                   // Field/Discipline
            );

            // Attempt to update the course in the database
            if (this.coursesService.update(details) != null) {
                // Success: Add a success message
                model.addAttribute("message", "Le cours a été mit à jour.");
                model.addAttribute("messageType", "success");
            } else {
                // Failure: Something went wrong, display an error message
                model.addAttribute("message", "Une erreur est survenue :/");
                model.addAttribute("messageType", "warning");
            }
        } else {
            // Validation failed: Add an error message and reinitialize the table with validation errors
            model.addAttribute("message", "Des champs sont incorrects ou incomplets.");
            model.addAttribute("messageType", "error");

            // Reinitialize the table with invalid data for display and show validation errors
            tableTemplate.initModel(model, null, Courses.class, false, requestContentValidator);
        }

        // Prepare for redirect, passing any necessary model attributes
        tableTemplate.prepareRedirect(model, redirectAttributes);
        return "redirect:/" + absoluteURL;  // Redirect to the courses management page
    }

    /*
     * POST Mapping - Add a new course.
     * The URL is: http://localhost:8080/admin/courses/courses
     */
    @PostMapping("/courses")
    public String post(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException {

        // Validator to ensure that the request is valid (name, field, and level)
        Validator requestContentValidator = new Validator(Map.of(
            "name", "default=NULL|max=100",   // Course name is optional but should be up to 100 characters
            "field", "default=0|int",          // Field is optional, should be an integer
            "level", "default=0|int"           // Level is optional, should be an integer
        ));

        // Validate the request
        if (requestContentValidator.validateRequest(request)) {

            // Fetch field and level data by their IDs (if valid)
            Optional<FieldTeacher> fieldOp = this.fieldService.getById(Long.parseLong(request.getFirst("field")));
            FieldTeacher field = fieldOp.isPresent() ? fieldOp.get() : null; // Field may be null if not found
            Optional<ClassLevel> levelOp = this.clService.getById(Long.parseLong(request.getFirst("level")));
            ClassLevel level = levelOp.isPresent() ? levelOp.get() : null; // Level may be null if not found

            // Create a new Courses object with the data from the request
            Courses details = new Courses(
                null,                                // ID is null for new course
                request.getFirst("name"),            // Course name
                level,                               // Class level
                field                                // Field/Discipline
            );

            // Attempt to create the new course in the database
            if (this.coursesService.create(details) != null) {
                // Success: Add a success message
                model.addAttribute("message", "Le cours a été ajouté.");
                model.addAttribute("messageType", "success");
            } else {
                // Failure: Something went wrong, display an error message
                model.addAttribute("message", "Une erreur est survenue :/");
                model.addAttribute("messageType", "warning");
            }
        } else {
            // Validation failed: Add an error message and reinitialize the table with validation errors
            model.addAttribute("message", "Des champs sont incorrects ou incomplets.");
            model.addAttribute("messageType", "error");

            // Reinitialize the table with invalid data for display and show validation errors
            tableTemplate.initModel(model, null, Courses.class, false, requestContentValidator);
        }

        // Prepare for redirect, passing any necessary model attributes
        tableTemplate.prepareRedirect(model, redirectAttributes);
        return "redirect:/" + absoluteURL;  // Redirect to the courses management page
    }

    /*
     * DELETE Mapping - Delete a course.
     * The URL is: http://localhost:8080/admin/courses/courses
     */
    @DeleteMapping("/courses")
    public String delete(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException {

        // Validator to ensure that the request is valid (course ID must be provided and valid)
        Validator requestContentValidator = new Validator(Map.of(
            "id", "required|int|min=0"  // ID must be provided, must be an integer, and at least 0
        ));

        // Validate the request
        if (requestContentValidator.validateRequest(request)) {
            // Attempt to delete the course by its ID
            this.coursesService.delete(Long.parseLong(request.getFirst("id")));
            model.addAttribute("message", "Le cours a été supprimé.");
            model.addAttribute("messageType", "success");
        } else {
            // Validation failed: Add an error message
            model.addAttribute("message", "Des champs sont incorrects ou incomplets.");
            model.addAttribute("messageType", "error");
        }

        // Prepare for redirect, passing any necessary model attributes
        tableTemplate.prepareRedirect(model, redirectAttributes);
        return "redirect:/" + absoluteURL;  // Redirect to the courses management page
    }
}

