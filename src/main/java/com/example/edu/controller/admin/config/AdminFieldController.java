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

import com.example.edu.model.FieldTeacher;
import com.example.edu.service.FieldService;
import com.example.edu.tool.Validator.Validator;
import com.example.edu.tool.Validator.Exceptions.unknownRuleException;
import com.example.edu.tool.template.TableSingle;

@Controller
@RequestMapping("/admin/config")
public class AdminFieldController {

    // Service for managing fields
    private final FieldService fieldService;

    // Base URL for the controller
    private final String absoluteURL = "admin/config/field";

    // Template for rendering a table of fields
    private TableSingle tableTemplate;

    @Autowired
    public AdminFieldController(FieldService fieldService) {
        this.fieldService = fieldService;

        // Map to translate attribute names to user-friendly labels
        Map<String, String> columnToLabel = Map.of(
            "id", "ID",
            "name", "Nom"
        );

        // List of columns to display in the table
        List<String> columnDisplayed = Arrays.asList("id", "name");

        // Initialize table template with configuration
        this.tableTemplate = new TableSingle("Domaines", columnToLabel, columnDisplayed);
    }

    /**
     * Handles GET requests to "/field".
     * Renders a page to manage field data.
     */
    @GetMapping("/field")
    public String get(Model model) {
        tableTemplate.initModel(model, this.fieldService.getAll(), FieldTeacher.class);
        return "admin/default";
    }

    /**
     * Handles PUT requests to "/field".
     * Updates an existing field based on request data.
     */
    @PutMapping("/field")
    public String put(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException {

        // Validator to enforce rules on request data
        Validator requestContentValidator = new Validator(Map.of(
            "id", "required|int|min=0", // ID is required and must be a positive integer
            "name", "required|max=100" // Name is required with a max length of 100
        ));

        if (requestContentValidator.validateRequest(request)) { // Validate request data
            // Create updated field object from request data
            FieldTeacher details = new FieldTeacher(
                Long.parseLong(request.getFirst("id")),
                request.getFirst("name")
            );

            // Update field in the service
            if (this.fieldService.update(details) != null) {
                model.addAttribute("message", "Le domaine a été mit à jour.");
                model.addAttribute("messageType", "success");
            } else {
                model.addAttribute("message", "Une erreur est survenue :/");
                model.addAttribute("messageType", "warning");
            }
        } else {
            // Handle validation failure
            model.addAttribute("message", "Des champs sont incorrect ou incomplet.");
            model.addAttribute("messageType", "error");
            tableTemplate.initModel(model, null, FieldTeacher.class, false, requestContentValidator);
        }

        // Prepare redirect with updated model data
        tableTemplate.prepareRedirect(model, redirectAttributes);
        return "redirect:/" + absoluteURL;
    }

    /**
     * Handles POST requests to "/field".
     * Creates a new field based on request data.
     */
    @PostMapping("/field")
    public String post(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException {

        // Validator to enforce rules on request data
        Validator requestContentValidator = new Validator(Map.of(
            "name", "required|max=100" // Name is required with a max length of 100
        ));

        if (requestContentValidator.validateRequest(request)) { // Validate request data
            // Create new field object
            FieldTeacher details = new FieldTeacher(
                null, // ID is null for new entities
                request.getFirst("name")
            );

            // Add field to the service
            if (this.fieldService.create(details) != null) {
                model.addAttribute("message", "Le domaine a été ajoutée.");
                model.addAttribute("messageType", "success");
            } else {
                model.addAttribute("message", "Une erreur est survenue :/");
                model.addAttribute("messageType", "warning");
            }
        } else {
            // Handle validation failure
            model.addAttribute("message", "Des champs sont incorrect ou incomplet.");
            model.addAttribute("messageType", "error");
            tableTemplate.initModel(model, null, FieldTeacher.class, false, requestContentValidator);
        }

        // Prepare redirect with updated model data
        tableTemplate.prepareRedirect(model, redirectAttributes);
        return "redirect:/" + absoluteURL;
    }

    /**
     * Handles DELETE requests to "/field".
     * Deletes an existing field based on request data.
     */
    @DeleteMapping("/field")
    public String delete(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException {

        // Validator to enforce rules on request data
        Validator requestContentValidator = new Validator(Map.of(
            "id", "required|int|min=0" // ID is required and must be a positive integer
        ));

        if (requestContentValidator.validateRequest(request)) { // Validate request data
            // Delete field from the service
            this.fieldService.delete(Long.parseLong(request.getFirst("id")));
            model.addAttribute("message", "Le domaine a été supprimé.");
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
