package com.example.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.web.bind.annotation.RequestMapping;


import com.example.edu.service.StudentService;
import com.example.edu.tool.Security;
import com.example.edu.service.AdminService;

@Controller
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    private final StudentService studentService;

    @Autowired
    public AdminController(AdminService adminService, StudentService studentService) {
        this.adminService = adminService;
        this.studentService = studentService;
    }

    @GetMapping("/")
    public String dashboard(Model model) { //this Model is not the same as Model in the MVC (architecture your are using), this is just meh naming from sping

        String admin = Security.getLoggedEmail();
        model.addAttribute("email", admin); //add value to variable in jsp file
        return "admin/dashboard";  
    }

    @GetMapping("/ce/lien/nest/pas/suspect")
    public String displayAllStudent(Model model) { //this Model is not the same as Model in the MVC (architecture your are using), this is just meh naming from sping

        String admin = Security.getLoggedEmail();
        model.addAttribute("email", admin); //add value to variable in jsp file
        return "security";  
    }

}
