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

import com.example.edu.model.ClassLevel;
import com.example.edu.service.ClassLevelService;
import com.example.edu.tool.Validator.Validator;
import com.example.edu.tool.Validator.Exceptions.unknownRuleException;
import com.example.edu.tool.template.TableSingle;

@Controller
@RequestMapping("/admin/config")
public class AdminClassLevelController {

    // Service for managing class levels
    private final ClassLevelService clService;

    // Base URL for the controller
    private final String absoluteURL = "admin/config/classLevel";

    // Template for rendering a table of class levels
    private TableSingle tableTemplate;

    @Autowired
    public AdminClassLevelController(ClassLevelService clService) {
        this.clService = clService;

        // Map to translate attribute names to user-friendly labels
        Map<String, String> columnToLabel = Map.of(
            "id", "ID",
            "name", "Nom"
        );

        // List of columns to display in the table
        List<String> columnDisplayed = Arrays.asList("id", "name");

        // Initialize table template with configuration
        this.tableTemplate = new TableSingle("Cursus", columnToLabel, columnDisplayed);
    }

    /**
     * Handles GET requests to "/classLevel".
     * Renders a page to manage class level data.
     */
    @GetMapping("/classLevel")
    public String get(Model model) {
        tableTemplate.initModel(model, this.clService.getAll(), ClassLevel.class);
        return "admin/default";
    }

    /**
     * Handles PUT requests to "/classLevel".
     * Updates an existing class level based on request data.
     */
    @PutMapping("/classLevel")
    public String put(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException {

        // Validator to enforce rules on request data
        Validator requestContentValidator = new Validator(Map.of(
            "id", "required|int|min=0", // ID is required and must be a positive integer
            "name", "required|max=100" // Name is required with a max length of 100
        ));

        if (requestContentValidator.validateRequest(request)) { // Validate request data
            // Create updated class level object from request data
            ClassLevel details = new ClassLevel(
                Long.parseLong(request.getFirst("id")),
                request.getFirst("name")
            );

            // Update class level in the service
            if (this.clService.update(details) != null) {
                model.addAttribute("message", "La salle a été mit à jour.");
                model.addAttribute("messageType", "success");
            } else {
                model.addAttribute("message", "Une erreur est survenue :/");
                model.addAttribute("messageType", "warning");
            }
        } else {
            // Handle validation failure
            model.addAttribute("message", "Des champs sont incorrect ou incomplet.");
            model.addAttribute("messageType", "error");
            tableTemplate.initModel(model, null, ClassLevel.class, false, requestContentValidator);
        }

        // Prepare redirect with updated model data
        tableTemplate.prepareRedirect(model, redirectAttributes);
        return "redirect:/" + absoluteURL;
    }

    /**
     * Handles POST requests to "/classLevel".
     * Creates a new class level based on request data.
     */
    @PostMapping("/classLevel")
    public String post(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException {

        // Validator to enforce rules on request data
        Validator requestContentValidator = new Validator(Map.of(
            "name", "required|max=100" // Name is required with a max length of 100
        ));

        if (requestContentValidator.validateRequest(request)) { // Validate request data
            // Create new class level object
            ClassLevel details = new ClassLevel(
                null, // ID is null for new entities
                request.getFirst("name")
            );

            // Add class level to the service
            if (this.clService.create(details) != null) {
                model.addAttribute("message", "La salle a été ajoutée.");
                model.addAttribute("messageType", "success");
            } else {
                model.addAttribute("message", "Une erreur est survenue :/");
                model.addAttribute("messageType", "warning");
            }
        } else {
            // Handle validation failure
            model.addAttribute("message", "Des champs sont incorrect ou incomplet.");
            model.addAttribute("messageType", "error");
            tableTemplate.initModel(model, null, ClassLevel.class, false, requestContentValidator);
        }

        // Prepare redirect with updated model data
        tableTemplate.prepareRedirect(model, redirectAttributes);
        return "redirect:/" + absoluteURL;
    }

    /**
     * Handles DELETE requests to "/classLevel".
     * Deletes an existing class level based on request data.
     */
    @DeleteMapping("/classLevel")
    public String delete(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException {

        // Validator to enforce rules on request data
        Validator requestContentValidator = new Validator(Map.of(
            "id", "required|int|min=0" // ID is required and must be a positive integer
        ));

        if (requestContentValidator.validateRequest(request)) { // Validate request data
            // Delete class level from the service
            this.clService.delete(Long.parseLong(request.getFirst("id")));
            model.addAttribute("message", "La salle a été supprimé.");
            model.addAttribute("messageType", "success");
        } else {
            // Handle validation failure
            model.addAttribute("message", "Des champs sont incorrect ou incomplet.");
            model.addAttribute("messageType", "error");
        }

        // Prepare redirect with updated model data
        tableTemplate.prepareRedirect(model, redirectAttributes);
        return "redirect:/" + absoluteURL;
    }
}