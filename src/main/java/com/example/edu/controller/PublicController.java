package com.example.edu.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.edu.model.Student;
import com.example.edu.service.StudentService;
import com.example.edu.tool.Validator.Validator;
import com.example.edu.tool.Validator.Exceptions.unknownRuleException;

@Controller
@RequestMapping("/")
public class PublicController {

    // Service dependency for managing student data
    private final StudentService studentService;

    // Constructor injection of StudentService
    @Autowired
    public PublicController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Mapping for the homepage
    @GetMapping("")
    public String welcome(Model model) {
        return "index";  
    }

    // Mapping for the login page
    @GetMapping("login")
    public String login(Model model) {
        return "login";  
    }

    // Mapping for the registration page (GET request)
    @GetMapping("register")
    public String getRegister(Model model) {
        return "register";  
    }

    // Mapping for handling registration form submissions (POST request)
    @PostMapping("register")
    public String postRegister(Model model, @RequestParam MultiValueMap<String, String> params) throws unknownRuleException {
        // Create a validator with rules for validating user input
        Validator requestValidator = new Validator(Map.of(
            "username", "required|email|max=100", 
            "surname", "max=100", 
            "name", "max=100", 
            "dateofbirth", "required|date", 
            "password", "required|confirm"
        ));

        // If validation passes, create a new student
        if (requestValidator.validateRequest(params)) {
            Student newStudent = new Student(
                null, 
                params.getFirst("username"), 
                params.getFirst("password"), 
                params.getFirst("name"), 
                params.getFirst("surname"), 
                null, 
                params.getFirst("dateofbirth"), 
                null
            );
            this.studentService.create(newStudent);

            // Add success message to the model
            model.addAttribute("title", "Registration Completed");
            model.addAttribute("message", "Your registration is complete. You will receive an email once your request is validated.");
            return "message";  
        }

        // If validation fails, add errors and previously validated data to the model
        model.addAttribute("error", requestValidator.getErrors());
        model.addAttribute("old", requestValidator.getValidatedValue());

        return "register";  
    }

}
