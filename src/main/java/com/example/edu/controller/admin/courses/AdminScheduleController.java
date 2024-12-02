package com.example.edu.controller.admin.courses;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

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

import com.example.edu.model.Classes;
import com.example.edu.model.Courses;
import com.example.edu.model.Room;
import com.example.edu.model.Schedule;
import com.example.edu.model.Teacher;
import com.example.edu.service.ClassesService;
import com.example.edu.service.CoursesService;
import com.example.edu.service.RoomService;
import com.example.edu.service.ScheduleService;
import com.example.edu.service.TeacherService;
import com.example.edu.tool.Validator.Validator;
import com.example.edu.tool.Validator.Exceptions.unknownRuleException;
import com.example.edu.tool.template.ScheduleTemplate;


@Controller
@RequestMapping("/admin/courses")
public class AdminScheduleController {

    // Injecting service classes to manage courses, teachers, classes, rooms, and schedules
    private final CoursesService coursesService;
    private final TeacherService teacherService;
    private final ClassesService clService;
    private final RoomService roomService;
    private final ScheduleService scheduleService;

    // URL path for the admin schedule page
    private final String absoluteURL = "admin/courses/schedule";
    
    // A schedule template used for managing schedule-related data
    private ScheduleTemplate scheduleTemplate;

    // Constructor to initialize the controller with the services and template
    @Autowired
    public AdminScheduleController(CoursesService coursesService, TeacherService teacherService, ClassesService clService, RoomService roomService, ScheduleService scheduleService) {
        this.coursesService = coursesService;
        this.teacherService = teacherService;
        this.clService = clService;
        this.roomService = roomService;
        this.scheduleService = scheduleService;

        // Initialize the schedule template with title and visibility
        this.scheduleTemplate = new ScheduleTemplate("Emploi du temps", true);

        // Set the available options for course, teacher, class, and room in the schedule template
        this.scheduleTemplate.setValuesFor("course", this.coursesService::getAllIdxName);
        this.scheduleTemplate.setValuesFor("teacher", this.teacherService::getAllIdxName);
        this.scheduleTemplate.setValuesFor("class", this.clService::getAllIdxName);
        this.scheduleTemplate.setValuesFor("room", this.roomService::getAllIdxName);
        
        // Link class and room, and course and teacher in the schedule template
        this.scheduleTemplate.addLink("class", "room", this.clService::getAllRoom); 
        this.scheduleTemplate.addLink("course", "teacher", this.coursesService::getAllTeachers); 
    }

    /*
     * GET - http://localhost:8080/admin/student
     * Page to manage students' data.
     */
    @GetMapping("/schedule")
    public String get(Model model) {
        // Initialize the model with schedule data from the service
        scheduleTemplate.initModel(model, this.scheduleService.getAll());
        return "admin/schedule";  // Return the schedule management view
    }

    /*
     * PUT - http://localhost:8080/admin/student
     * Update an existing schedule entry.
     */
    @PutMapping("/schedule")
    public String put(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException {

        // Create a validator to validate the request content (check required fields, types, and constraints)
        Validator requestContentValidator = new Validator(Map.of(
            "id", "required|int|min=0",
            "course", "required|int|min=0",
            "teacher", "required|int|min=0",
            "class", "required|int|min=0",
            "room", "required|int|min=0",
            "start", "required|datetime",
            "end", "required|datetime"
        ));

        // Add the seconds to the 'start' and 'end' times for consistency
        request.putAll(Map.of("start", Arrays.asList(request.getFirst("start") + ":00")));
        request.putAll(Map.of("end", Arrays.asList(request.getFirst("end") + ":00")));

        // Validate the request content
        if (requestContentValidator.validateRequest(request)) {

            // Parse start and end time strings into LocalDateTime objects
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime start = LocalDateTime.parse(request.getFirst("start"), formatter);
            LocalDateTime end = LocalDateTime.parse(request.getFirst("end"), formatter);

            // Retrieve course, teacher, class, and room from the database
            Optional<Courses> courseOp = this.coursesService.getById(Long.parseLong(request.getFirst("course")));
            Courses course = courseOp.isPresent() ? courseOp.get() : null;

            Optional<Teacher> teacherOp = this.teacherService.getTeachersById(Long.parseLong(request.getFirst("teacher")));
            Teacher teacher = teacherOp.isPresent() ? teacherOp.get() : null;

            Optional<Classes> classesOp = this.clService.getById(Long.parseLong(request.getFirst("class")));
            Classes classes = classesOp.isPresent() ? classesOp.get() : null;

            Optional<Room> roonOp = this.roomService.getById(Long.parseLong(request.getFirst("room")));
            Room room = roonOp.isPresent() ? roonOp.get() : null;

            // Create a Schedule object with the parsed data
            Schedule details = new Schedule(
                Long.parseLong(request.getFirst("id")),
                room,
                classes,
                teacher,
                course,
                start,
                end
            );

            // Validate schedule data (check if the time is valid, if the room, teacher, and class are available, etc.)
            if (start.isAfter(end) || start.getHour() < 8 || (end.getHour() >= 20 && end.getMinute() > 0 && end.getSecond() > 0) || start.plusHours(12).isBefore(end)) {
                model.addAttribute("message", "A course cannot exceed 12 hours, start before 8 AM, or end after 8 PM.");
                model.addAttribute("messageType", "warning");
            } else if (!this.scheduleService.isRoomAvailableId(room, start, end, details.getId())) {
                model.addAttribute("message", "This room is already occupied during this time slot.");
                model.addAttribute("messageType", "warning");
                scheduleTemplate.initModel(model, null, false, requestContentValidator);
            } else if (!this.scheduleService.isTeacherAvailableId(teacher, start, end, details.getId())) {
                model.addAttribute("message", "This teacher is already teaching during this time slot.");
                model.addAttribute("messageType", "warning");
                scheduleTemplate.initModel(model, null, false, requestContentValidator);
            } else if (!this.scheduleService.isClassAvailableId(classes, start, end, details.getId())) {
                model.addAttribute("message", "This class is already scheduled during this time slot.");
                model.addAttribute("messageType", "warning");
                scheduleTemplate.initModel(model, null, false, requestContentValidator);
            } else if (room == null || classes == null || teacher == null || course == null) {
                model.addAttribute("message", "Some fields are incorrect or incomplete.");
                model.addAttribute("messageType", "error");
                scheduleTemplate.initModel(model, null, false, requestContentValidator);
            } else if (this.scheduleService.update(details) != null || teacher.getField().getId() != course.getField().getId()) {
                // Successfully updated the course schedule
                model.addAttribute("message", "The course has been updated.");
                model.addAttribute("messageType", "success");
            } else {
                // Unexpected error
                model.addAttribute("message", "An error occurred :/");
                model.addAttribute("messageType", "warning");
            }

        } else {
            // Validation failed
            model.addAttribute("message", "Some fields are incorrect or incomplete.");
            model.addAttribute("messageType", "error");
            scheduleTemplate.initModel(model, null, false, requestContentValidator);
        }

        // Prepare for a redirect with the updated model
        scheduleTemplate.prepareRedirect(model, redirectAttributes);
        return "redirect:/"+absoluteURL;  
    }

    /*
     * POST - http://localhost:8080/admin/student
     * Create a new course schedule entry.
     */
    @PostMapping("/schedule")
    public String post(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException {
        // Same validation logic as in the PUT method for creating a schedule entry
        Validator requestContentValidator = new Validator(Map.of(
            "course", "required|int|min=0",
            "teacher", "required|int|min=0",
            "class", "required|int|min=0",
            "room", "required|int|min=0",
            "start", "required|datetime",
            "end", "required|datetime"
        ));
        
        request.putAll(Map.of("start", Arrays.asList(request.getFirst("start") + ":00")));
        request.putAll(Map.of("end", Arrays.asList(request.getFirst("end") + ":00")));

        if (requestContentValidator.validateRequest(request)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");
            LocalDateTime start = LocalDateTime.parse(request.getFirst("start"), formatter);
            LocalDateTime end = LocalDateTime.parse(request.getFirst("end"), formatter);

            // Retrieve course, teacher, class, and room
            Optional<Courses> courseOp = this.coursesService.getById(Long.parseLong(request.getFirst("course")));
            Courses course = courseOp.isPresent() ? courseOp.get() : null;

            Optional<Teacher> teacherOp = this.teacherService.getTeachersById(Long.parseLong(request.getFirst("teacher")));
            Teacher teacher = teacherOp.isPresent() ? teacherOp.get() : null;

            Optional<Classes> classesOp = this.clService.getById(Long.parseLong(request.getFirst("class")));
            Classes classes = classesOp.isPresent() ? classesOp.get() : null;

            Optional<Room> roonOp = this.roomService.getById(Long.parseLong(request.getFirst("room")));
            Room room = roonOp.isPresent() ? roonOp.get() : null;

            Schedule details = new Schedule(
                null,  // Null ID because it's a new schedule
                room,
                classes,
                teacher,
                course,
                start,
                end
            );

            // Perform validations (same as PUT method)
            if (start.isAfter(end) || start.getHour() < 8 || (end.getHour() >= 20 && end.getMinute() > 0 && end.getSecond() > 0) || start.plusHours(12).isBefore(end)) {
                model.addAttribute("message", "A course cannot exceed 12 hours, start before 8 AM, or end after 8 PM.");
                model.addAttribute("messageType", "warning");
                scheduleTemplate.initModel(model, null, true, requestContentValidator);
            } else if (!this.scheduleService.isRoomAvailable(room, start, end)) {
                model.addAttribute("message", "This room is already occupied during this time slot.");
                model.addAttribute("messageType", "warning");
                scheduleTemplate.initModel(model, null, true, requestContentValidator);
            } else if (!this.scheduleService.isTeacherAvailable(teacher, start, end)) {
                model.addAttribute("message", "This teacher is already teaching during this time slot.");
                model.addAttribute("messageType", "warning");
                scheduleTemplate.initModel(model, null, true, requestContentValidator);
            } else if (!this.scheduleService.isClassAvailable(classes, start, end)) {
                model.addAttribute("message", "This class is already scheduled during this time slot.");
                model.addAttribute("messageType", "warning");
                scheduleTemplate.initModel(model, null, true, requestContentValidator);
            } else if (room == null || classes == null || teacher == null || course == null) {
                model.addAttribute("message", "Some fields are incorrect or incomplete.");
                model.addAttribute("messageType", "error");
                scheduleTemplate.initModel(model, null, true, requestContentValidator);
            } else if (this.scheduleService.create(details) != null) {
                model.addAttribute("message", "The course has been added.");
                model.addAttribute("messageType", "success");
            } else {
                model.addAttribute("message", "An error occurred :/");
                model.addAttribute("messageType", "warning");
            }
        } else {
            model.addAttribute("message", "Some fields are incorrect or incomplete.");
            model.addAttribute("messageType", "error");
            scheduleTemplate.initModel(model, null, true, requestContentValidator);
        }

        scheduleTemplate.prepareRedirect(model, redirectAttributes);
        return "redirect:/"+absoluteURL;  
    }

    /*
     * DELETE - http://localhost:8080/admin/student
     * Delete a course schedule entry.
     */
    @DeleteMapping("/schedule")
    public String delete(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException {

        // Validator to ensure the 'id' field is valid
        Validator requestContentValidator = new Validator(Map.of(
            "id", "required|int|min=0"  // id is required and must be a non-negative integer
        ));

        if (requestContentValidator.validateRequest(request)) {
            // Delete the schedule entry by its ID
            this.scheduleService.delete(Long.parseLong(request.getFirst("id")));
            model.addAttribute("message", "The course has been deleted.");
            model.addAttribute("messageType", "success");
        } else {
            model.addAttribute("message", "Some fields are incorrect or incomplete.");
            model.addAttribute("messageType", "error");
        }

        scheduleTemplate.prepareRedirect(model, redirectAttributes);
        return "redirect:/"+absoluteURL;  
    }
}
