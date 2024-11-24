package com.example.edu.controller;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.example.edu.model.Student;
import com.example.edu.service.ScheduleService;
import com.example.edu.service.StudentService;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Controller
public class StudentController {

    private final StudentService studentService;
    private final ScheduleService scheduleService;

    public StudentController(StudentService studentService, ScheduleService scheduleService) {
        this.studentService = studentService;
        this.scheduleService = scheduleService;
    }

    @GetMapping("/student/")
public String showDashboard(
        @RequestParam(required = false) String startOfWeek,
        Model model,
        Authentication authentication) {
    String email = authentication.getName();
    System.out.println("Utilisateur connecté : " + email);

    Student student = studentService.getByEmail(email).orElse(null);
    if (student == null) {
        System.out.println("Aucun étudiant trouvé pour l'email : " + email);
        model.addAttribute("errorMessage", "Aucune information trouvée pour l'utilisateur connecté.");
        return "student/error";
    }

    System.out.println("Étudiant trouvé : " + student.getName() + " " + student.getSurname());
    model.addAttribute("studentName", student.getName());
    model.addAttribute("studentSurname", student.getSurname());
    model.addAttribute("studentClass", student.getStudentClass() != null ? student.getStudentClass() : "Non assignée");
    model.addAttribute("studentLevel", student.getLevel() != null ? student.getLevel().getName() : "Non assigné");
    model.addAttribute("confirmation", student.getConfirm() ? "Confirmé" : "Non confirmé");

    // Gestion des dates de la semaine
    LocalDate startDate = startOfWeek == null
            ? LocalDate.now().with(DayOfWeek.MONDAY)
            : LocalDate.parse(startOfWeek);
    System.out.println("Semaine sélectionnée : " + startDate);

    model.addAttribute("currentWeekStart", startDate);
    model.addAttribute("currentWeekEnd", startDate.plusDays(6));

    // Récupération et ajout de l'emploi du temps
    Map<String, Map<String, List<Map<String, Object>>>> schedule = scheduleService.getScheduleForWeek(startDate);
    System.out.println("Données d'emploi du temps récupérées : " + schedule);

    model.addAttribute("schedule", schedule);
    model.addAttribute("timeSlots", List.of("08:00", "09:00", "10:00", "11:00", "12:00",
                                            "13:00", "14:00", "15:00", "16:00", "17:00", "18:00", "19:00", "20:00"));
    model.addAttribute("days", List.of("Lundi", "Mardi", "Mercredi", "Jeudi", "Vendredi"));

    return "student/dashboard";
}


}