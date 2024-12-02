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


import com.example.edu.model.Room;
import com.example.edu.service.RoomService;
import com.example.edu.tool.template.TableSingle;
import com.example.edu.tool.Validator.Validator;
import com.example.edu.tool.Validator.Exceptions.unknownRuleException;


@Controller
@RequestMapping("/admin/config")
public class AdminRoomController {

    // Service for managing rooms
    private final RoomService roomService;

    // Base URL for the controller
    private final String absoluteURL = "admin/config/room";

    // Template for rendering a table of rooms
    private TableSingle tableTemplate;

    @Autowired
    public AdminRoomController(RoomService roomService) {
        this.roomService = roomService;

        // Map to translate attribute names to user-friendly labels
        Map<String, String> columnToLabel = Map.of(
            "id", "ID",
            "name", "Nom",
            "size", "Taille"
        );

        // List of columns to display in the table
        List<String> columnDisplayed = Arrays.asList("id", "name", "size");

        // Initialize table template with configuration
        this.tableTemplate = new TableSingle("Salle", columnToLabel, columnDisplayed);
    }

    /**
     * Handles GET requests to "/room".
     * Renders a page to manage room data.
     */
    @GetMapping("/room")
    public String get(Model model) {
        tableTemplate.initModel(model, this.roomService.getAll(), Room.class);
        return "admin/default";
    }

    /**
     * Handles PUT requests to "/room".
     * Updates an existing room based on request data.
     */
    @PutMapping("/room")
    public String put(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException {

        // Validator to enforce rules on request data
        Validator requestContentValidator = new Validator(Map.of(
            "id", "required|int|min=0", // ID is required and must be a positive integer
            "name", "required|max=100", // Name is required with a max length of 100
            "size", "required|int|min=0" // Size is required and must be a positive integer
        ));

        if (requestContentValidator.validateRequest(request)) { // Validate request data
            // Create updated room object from request data
            Room details = new Room(
                Long.parseLong(request.getFirst("id")),
                request.getFirst("name"),
                Integer.parseInt(request.getFirst("size"))
            );

            // Update room in the service
            if (this.roomService.update(details) != null) {
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
            tableTemplate.initModel(model, null, Room.class, false, requestContentValidator);
        }

        // Prepare redirect with updated model data
        tableTemplate.prepareRedirect(model, redirectAttributes);
        return "redirect:/" + absoluteURL;
    }

    /**
     * Handles POST requests to "/room".
     * Creates a new room based on request data.
     */
    @PostMapping("/room")
    public String post(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException {

        // Validator to enforce rules on request data
        Validator requestContentValidator = new Validator(Map.of(
            "name", "required|max=100", // Name is required with a max length of 100
            "size", "required|int|min=0" // Size is required and must be a positive integer
        ));

        if (requestContentValidator.validateRequest(request)) { // Validate request data
            // Create new room object
            Room details = new Room(
                null, // ID is null for new entities
                request.getFirst("name"),
                Integer.parseInt(request.getFirst("size"))
            );

            // Add room to the service
            if (this.roomService.create(details) != null) {
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
            tableTemplate.initModel(model, null, Room.class, false, requestContentValidator);
        }

        // Prepare redirect with updated model data
        tableTemplate.prepareRedirect(model, redirectAttributes);
        return "redirect:/" + absoluteURL;
    }

    /**
     * Handles DELETE requests to "/room".
     * Deletes an existing room based on request data.
     */
    @DeleteMapping("/room")
    public String delete(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException {

        // Validator to enforce rules on request data
        Validator requestContentValidator = new Validator(Map.of(
            "id", "required|int|min=0" // ID is required and must be a positive integer
        ));

        if (requestContentValidator.validateRequest(request)) { // Validate request data
            // Delete room from the service
            this.roomService.delete(Long.parseLong(request.getFirst("id")));
            model.addAttribute("message", "La salle a été supprimée.");
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
