package com.example.edu.controller.teacher;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.example.edu.model.Teacher;
import com.example.edu.service.ClassesService;
import com.example.edu.service.CoursesService;
import com.example.edu.service.RoomService;
import com.example.edu.service.TeacherService;
import com.example.edu.tool.template.ScheduleTemplate;

@Controller
public class TeacherController {
    
    private final CoursesService coursesService;
    private final TeacherService teacherService;
    private final ClassesService clService;
    private final RoomService roomService;
    private ScheduleTemplate scheduleTemplate;

    @Autowired
    public TeacherController(CoursesService coursesService, TeacherService teacherService, ClassesService clService, RoomService roomService) {
        this.coursesService = coursesService;
        this.teacherService = teacherService;
        this.clService = clService;
        this.roomService = roomService;

        this.scheduleTemplate = new ScheduleTemplate("Emploi du temps", false);
        this.scheduleTemplate.setValuesFor("course", this.coursesService::getAllIdxName);
        this.scheduleTemplate.setValuesFor("teacher", this.teacherService::getAllIdxName);
        this.scheduleTemplate.setValuesFor("class", this.clService::getAllIdxName);
        this.scheduleTemplate.setValuesFor("room", this.roomService::getAllIdxName);
    }
    
    @GetMapping("/teacher/")
    public String showDashboard(Model model, Authentication authentication) {

        Teacher teacher = teacherService.getByEmail(authentication.getName()).orElse(null);

        System.out.println(authentication.getName());
        System.out.println(teacher);

        scheduleTemplate.initModel(model, new ArrayList<>());
        return "teacher/dashboard";
    }
    
    
}