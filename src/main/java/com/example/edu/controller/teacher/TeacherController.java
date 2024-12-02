package com.example.edu.controller.teacher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.edu.model.Teacher;
import com.example.edu.service.ClassesService;
import com.example.edu.service.CoursesService;
import com.example.edu.service.RoomService;
import com.example.edu.service.ScheduleService;
import com.example.edu.service.TeacherService;
import com.example.edu.tool.template.ScheduleTemplate;

@Controller
public class TeacherController {
    
    // Dependencies required for managing courses, teachers, classes, rooms, and schedules
    private final CoursesService coursesService;
    private final TeacherService teacherService;
    private final ClassesService clService;
    private final RoomService roomService;
    private ScheduleTemplate scheduleTemplate;
    private ScheduleService scheduleService;

    // Constructor injection for all required services
    @Autowired
    public TeacherController(ScheduleService scheduleService, CoursesService coursesService, TeacherService teacherService, ClassesService clService, RoomService roomService) {
        this.coursesService = coursesService;
        this.teacherService = teacherService;
        this.clService = clService;
        this.roomService = roomService;
        this.scheduleService = scheduleService;

        // Initializing schedule template with static title "Emploi du temps"
        this.scheduleTemplate = new ScheduleTemplate("Emploi du temps", false);
        // Setting values for the schedule template from various services
        this.scheduleTemplate.setValuesFor("course", this.coursesService::getAllIdxName);
        this.scheduleTemplate.setValuesFor("teacher", this.teacherService::getAllIdxName);
        this.scheduleTemplate.setValuesFor("class", this.clService::getAllIdxName);
        this.scheduleTemplate.setValuesFor("room", this.roomService::getAllIdxName);
    }
    
    // Mapping for teacher dashboard page
    @GetMapping("/teacher/")
    public String showDashboard(Model model, Authentication authentication) {

        // Retrieve the teacher's details based on the authenticated user's email
        Teacher teacher = teacherService.getByEmail(authentication.getName()).orElse(null);

        // Debugging prints for authentication name and teacher details
        System.out.println(authentication.getName());
        System.out.println(teacher);

        // Initialize the model with the teacher's schedule information
        scheduleTemplate.initModel(model, this.scheduleService.getAllForTeacher(teacher));
        // Return the view for the teacher dashboard
        return "teacher/dashboard";
    }
    
}
