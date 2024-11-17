package com.example.edu.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.edu.service.StudentService;
import com.example.edu.tool.Security;
import com.example.edu.tool.Table;
import com.example.edu.tool.Validator.Validator;
import com.example.edu.tool.Validator.Exceptions.unknownRuleException;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final StudentService studentService;

    @Autowired
    public AdminController(StudentService studentService) {
        this.studentService = studentService;
    }

    /*
     * GET - http://localhost:8080/admin/
     * Default page for Admin
     * 
     */
    @GetMapping("/")
    public String dashboard(Model model) {

        String admin = Security.getLoggedEmail();
        model.addAttribute("email", admin);
        return "admin/dashboard";  
    }

    /*
     * GET - http://localhost:8080/admin/student
     * Page to manage students datas.
     * 
     */
    @GetMapping("student")
    public String student(Model model) {

        List<Object> data = new ArrayList<Object>(studentService.getAllStudents()); //get datas to show

        Map<String, String> fieldToLabel = Map.of( //translate attribute name to label name (More readable) [OPTIONAL]
            "id","ID",
            "email","E-mail",
            "surname","Nom",
            "name","Prénom",
            "dateofbirth","Date de naissance",
            "level","Promotion",
            "studentclass","Classe",
            "confirm","Inscription confirmée"
        );

        List<String> fieldDisplayedOnLine = Arrays.asList("id", "email", "surname", "name", "confirm"); // fields displayed on the line that represent the data [OPTIONAL]

        Table.setup(model, "Élèves", data, fieldToLabel, fieldDisplayedOnLine);

        return "admin/table";  
    }

    
    /*
     * POST - http://localhost:8080/admin/student
     * update a student.
     * 
     */
    @PostMapping("student")
    public String updateStudent(Model model, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException{

        List<Object> data = new ArrayList<Object>(studentService.getAllStudents());//get datas to show

        Map<String, String> fieldToLabel = Map.of( //translate attribute name to label name (More readable) [OPTIONAL]
            "id","ID",
            "email","E-mail",
            "surname","Nom",
            "name","Prénom",
            "dateofbirth","Date de naissance",
            "level","Promotion",
            "studentclass","Classe",
            "confirm","Inscription confirmée"
        );

        List<String> fieldDisplayedOnLine = Arrays.asList("id", "email", "surname", "name", "confirm"); // fields displayed on the line that represent the data [OPTIONAL]

        //Create validator to validate request content
        Validator requestContentValidator = new Validator(Map.of(
            "email", "required|email|max=100", //must be an email 100 char max, MANDATORY
            "surname", "max=100", //must be a string 100 char max, OPTIONAL
            "name", "max=100", //must be a string 100 char max, OPTIONAL
            "dateofbirth", "required|date", //must be a date yyyy-mm-dd, MANDATORY
            "confirm", "required|boolean" //must be a true or false, MANDATORY
            )
        );


        if( requestContentValidator.validateRequest(request) ){ //validate the request
            Table.setup(model, "Élèves", data, fieldToLabel, fieldDisplayedOnLine);
        } 
        else{
            Table.setupWithError(model, "Élèves", data, fieldToLabel, fieldDisplayedOnLine, requestContentValidator); //setup table to build with error
        }


        return "admin/table";  
    }

    @GetMapping("/ce/lien/nest/pas/suspect")
    public String displayAllStudent(Model model) { 

        String admin = Security.getLoggedEmail();
        model.addAttribute("email", admin);
        return "security";  
    }

}
