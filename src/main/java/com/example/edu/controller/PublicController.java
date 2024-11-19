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

    private final StudentService studentService;

    @Autowired
    public PublicController(StudentService studentService) {
        this.studentService = studentService;
    }

    @GetMapping("")
    public String welcome(Model model) {
        return "index";  
    }

    @GetMapping("login")
    public String login(Model model) {
        return "login";  
    }

    @GetMapping("register")
    public String getRegister(Model model) {
        return "register";  
    }

    @PostMapping("register")
    public String PostRegister(Model model, @RequestParam MultiValueMap<String, String> params) throws unknownRuleException{
        Validator requestValidator = new Validator(Map.of(
            "username", "required|email|max=100", 
            "surname", "max=100", 
            "name", "max=100", 
            "dateofbirth", "required|date", 
            "password", "required|confirm"
            )
        );

        if(requestValidator.validateRequest(params)){

            Student newStudent = new Student(null, params.getFirst("username"), params.getFirst("password"), params.getFirst("name"), params.getFirst("surname"), null, params.getFirst("dateofbirth"), null);
            this.studentService.createStudent(newStudent);

            model.addAttribute("title", "Inscription termine");
            model.addAttribute("message", "Ton inscription est terminée, tu recevras un mail lorsque ta demande aura été validée.");
            return "message";  
        }

        model.addAttribute("error", requestValidator.getErrors());
        model.addAttribute("old", requestValidator.getValidatedValue());

        return "register";  
    }


}
