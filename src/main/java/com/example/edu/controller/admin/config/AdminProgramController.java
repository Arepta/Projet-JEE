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

    // Service for managing programs
    private final ProgramService progamService;

    // Base URL for the controller
    private final String absoluteURL = "admin/config/program";

    // Template for rendering a table of programs
    private TableSingle tableTemplate;

    @Autowired
    public AdminProgramController(ProgramService progamService) {
        this.progamService = progamService;

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
     * Handles GET requests to "/program".
     * Renders a page to manage program data.
     *
     * @param model Model to pass attributes to the view.
     * @return View name for rendering the page.
     */
    @GetMapping("/program")
    public String get(Model model) {
        tableTemplate.initModel(model, this.progamService.getAll(), Program.class);
        return "admin/default";
    }

    /**
     * Handles PUT requests to "/program".
     * Updates an existing program based on request data.
     */
    @PutMapping("/program")
    public String put(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException {

        // Validator to enforce rules on request data
        Validator requestContentValidator = new Validator(Map.of(
            "id", "required|int|min=0", // ID is required and must be a positive integer
            "name", "required|max=100" // Name is required with a max length of 100
        ));

        if (requestContentValidator.validateRequest(request)) { // Validate request data
            // Create updated program object from request data
            Program details = new Program(
                Long.parseLong(request.getFirst("id")),
                request.getFirst("name")
            );

            // Update program in the service
            if (this.progamService.update(details) != null) {
                model.addAttribute("message", "Le cursus a été mit à jour.");
                model.addAttribute("messageType", "success");
            } else {
                model.addAttribute("message", "Une erreur est survenue :/");
                model.addAttribute("messageType", "warning");
            }
        } else {
            // Handle validation failure
            model.addAttribute("message", "Des champs sont incorrect ou incomplet.");
            model.addAttribute("messageType", "error");
            tableTemplate.initModel(model, null, Program.class, false, requestContentValidator);
        }

        // Prepare redirect with updated model data
        tableTemplate.prepareRedirect(model, redirectAttributes);
        return "redirect:/" + absoluteURL;
    }

    /**
     * Handles POST requests to "/program".
     * Creates a new program based on request data.
     */
    @PostMapping("/program")
    public String post(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException {

        // Validator to enforce rules on request data
        Validator requestContentValidator = new Validator(Map.of(
            "name", "required|max=100" // Name is required with a max length of 100
        ));

        if (requestContentValidator.validateRequest(request)) { // Validate request data
            // Create new program object
            Program details = new Program(
                null, // ID is null for new entities
                request.getFirst("name")
            );

            // Add program to the service
            if (this.progamService.create(details) != null) {
                model.addAttribute("message", "Le cursus a été ajoutée.");
                model.addAttribute("messageType", "success");
            } else {
                model.addAttribute("message", "Une erreur est survenue :/");
                model.addAttribute("messageType", "warning");
            }
        } else {
            // Handle validation failure
            model.addAttribute("message", "Des champs sont incorrect ou incomplet.");
            model.addAttribute("messageType", "error");
            tableTemplate.initModel(model, null, Program.class, false, requestContentValidator);
        }

        // Prepare redirect with updated model data
        tableTemplate.prepareRedirect(model, redirectAttributes);
        return "redirect:/" + absoluteURL;
    }

    /**
     * Handles DELETE requests to "/program".
     * Deletes an existing program based on request data.
     */
    @DeleteMapping("/program")
    public String delete(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException {

        // Validator to enforce rules on request data
        Validator requestContentValidator = new Validator(Map.of(
            "id", "required|int|min=0" // ID is required and must be a positive integer
        ));

        if (requestContentValidator.validateRequest(request)) { // Validate request data
            // Delete program from the service
            this.progamService.delete(Long.parseLong(request.getFirst("id")));
            model.addAttribute("message", "Le cursus a été supprimé.");
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
