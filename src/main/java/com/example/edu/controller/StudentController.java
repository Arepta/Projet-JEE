package com.example.edu.controller;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.edu.model.Student;
import com.example.edu.service.ClassesService;
import com.example.edu.service.CoursesService;
import com.example.edu.service.RoomService;
import com.example.edu.service.ScheduleService;
import com.example.edu.service.StudentService;
import com.example.edu.service.TeacherService;
import com.example.edu.tool.template.ScheduleTemplate;

@Controller
public class StudentController {
    
    // Dependencies for services related to students, courses, teachers, classes, and schedules
    private final StudentService studentService;
    private final CoursesService coursesService;
    private final TeacherService teacherService;
    private final ClassesService clService;
    private final RoomService roomService;
    private final ScheduleService scheduleService;
    private ScheduleTemplate scheduleTemplate;

    // Constructor injection for all required services
    @Autowired
    public StudentController(StudentService studentService, ScheduleService scheduleService, CoursesService coursesService, TeacherService teacherService, ClassesService clService, RoomService roomService) {
        this.studentService = studentService;
        this.scheduleService = scheduleService;
        this.coursesService = coursesService;
        this.teacherService = teacherService;
        this.clService = clService;
        this.roomService = roomService;

        // Initializing schedule template with static title "Emploi du temps"
        this.scheduleTemplate = new ScheduleTemplate("Emploi du temps", false);
        // Setting values for the schedule template from various services
        this.scheduleTemplate.setValuesFor("course", this.coursesService::getAllIdxName);
        this.scheduleTemplate.setValuesFor("teacher", this.teacherService::getAllIdxName);
        this.scheduleTemplate.setValuesFor("class", this.clService::getAllIdxName);
        this.scheduleTemplate.setValuesFor("room", this.roomService::getAllIdxName);
    }
    
    // Mapping for student dashboard page
    @GetMapping("/student/")
    public String showDashboard(Model model, Authentication authentication) {
        
        // Retrieve the student's details based on the authenticated user's email
        Student student = studentService.getByEmail(authentication.getName()).orElse(null);

        // Debugging prints for authentication name and student details
        System.out.println(authentication.getName());
        System.out.println(student);

        // Initialize the model with the student's schedule information
        if (student.getStudentClass() != null) {
            scheduleTemplate.initModel(model, this.scheduleService.getAllForStudent(student));
        } else {
            scheduleTemplate.initModel(model, new ArrayList<>());
        }

        // Return the view for the student dashboard
        return "student/dashboard";
    }

}
