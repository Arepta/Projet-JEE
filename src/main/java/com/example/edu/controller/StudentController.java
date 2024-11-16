package com.example.edu.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.edu.service.StudentService;

@Controller
@RequestMapping("/student")
public class StudentController {

    private final StudentService studentService;

    @Autowired
    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

     /*
     * This methode will be  call each time a GET request is send to http://localhost:8080/url/for/controller.
     * It will set the var message to "Hello, World!" in a jsp file at the location webapp/WEB-INF/jsp/pathTo/name.jsp
     * 
     */
    @GetMapping("/")
    /*
     * different request methode are available:
     * ----------     PLS RESPECT THE CONVENTION FOR THE USE OF METHOD. YOU CAN FIND THEM ON GOOGLE. SOME ARE HERE     ---------- 
     * GET (@GetMapping): to get a content <=> SELECT
     * POST (@PostMapping): when the request has datas to create/add content <=> INSERT
     * PUT (@PutMapping): when the request has datas to edit content <=> UPDATE
     * PATCH (@PatchMapping): when the request has datas to change a very specific ressource <=> UPDATE  | /!\ SHOULD BE USE ONLY IF YOU KNOW WHAT YOU ARE DOING. MAY TELL YOU AN URL DOES TO MUCH WORK. /!\
     */
    public String displayAllStudent(Model model) { //this Model is not the same as Model in the MVC (architecture your are using), this is just meh naming from sping
        model.addAttribute("content", studentService.getAllStudents()); //add value to variable in jsp file
        return "debug";  
    }

        /*
     * This methode will be  call each time a GET request is send to http://localhost:8080/url/for/controller/{id}, where {id} is a long.
     * It will set the var message to the user data with the corresponding id in a jsp file at the location webapp/WEB-INF/jsp/pathTo/name.jsp
     * 
     */
    @GetMapping("/{id}")
    public String getUser(Model model, @PathVariable long id) {
        model.addAttribute("content", studentService.getStudentsById(id)); //add value to variable in jsp file
        return "debug";  
    }

}
