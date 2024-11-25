package com.example.edu.controller.admin.courses;


import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private final CoursesService coursesService;
    private final TeacherService teacherService;
    private final ClassesService clService;
    private final RoomService roomService;
    private final ScheduleService scheduleService;

    private final String absoluteURL = "admin/courses/schedule";
    private ScheduleTemplate scheduleTemplate;

    @Autowired
    public AdminScheduleController(CoursesService coursesService, TeacherService teacherService, ClassesService clService, RoomService roomService, ScheduleService scheduleService) {
        this.coursesService = coursesService;
        this.teacherService = teacherService;
        this.clService = clService;
        this.roomService = roomService;
        this.scheduleService = scheduleService;

        this.scheduleTemplate = new ScheduleTemplate("Emploie du temps", true);

        this.scheduleTemplate.setValuesFor("course", this.coursesService::getAllIdxName);
        this.scheduleTemplate.setValuesFor("teacher", this.teacherService::getAllIdxName);
        this.scheduleTemplate.setValuesFor("class", this.clService::getAllIdxName);
        this.scheduleTemplate.setValuesFor("room", this.roomService::getAllIdxName);
        this.scheduleTemplate.addLink("class", "room", this.clService::getAllRoom); 
        this.scheduleTemplate.addLink("course", "teacher", this.coursesService::getAllTeachers); 
    }

    /*
     * GET - http://localhost:8080/admin/student
     * Page to manage students datas.
     * 
     */
    @GetMapping("/schedule")
    public String get(Model model) {

        scheduleTemplate.initModel(model, this.scheduleService.getAll());
        return "admin/schedule";  
    }

    
    /*
     * PUT - http://localhost:8080/admin/student
     * update a student.
     * 
     */
    @PutMapping("/schedule")
    public String put(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException{

        //Create validator to validate request content
        Validator requestContentValidator = new Validator(Map.of(
            "id", "required|int|min=0",
            "course", "required|int|min=0",
            "teacher", "required|int|min=0",
            "class", "required|int|min=0",
            "room", "required|int|min=0",
            "start", "required|datetime",
            "end", "required|datetime"
            )
        );

        if( requestContentValidator.validateRequest(request) ){ //validate the request

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

            LocalDateTime start = LocalDateTime.parse(request.getFirst("start"), formatter);
            LocalDateTime end = LocalDateTime.parse(request.getFirst("end"), formatter);

            Optional<Courses> courseOp = this.coursesService.getById(Long.parseLong(request.getFirst("course")));
            Courses course = courseOp.isPresent() ? courseOp.get() : null;

            Optional<Teacher> teacherOp = this.teacherService.getTeachersById(Long.parseLong(request.getFirst("teacher")));
            Teacher teacher = teacherOp.isPresent() ? teacherOp.get() : null;

            Optional<Classes> classesOp = this.clService.getById(Long.parseLong(request.getFirst("class")));
            Classes classes = classesOp.isPresent() ? classesOp.get() : null;

            Optional<Room> roonOp = this.roomService.getById(Long.parseLong(request.getFirst("room")));
            Room room = roonOp.isPresent() ? roonOp.get() : null;


            Schedule details = new Schedule(
                Long.parseLong(request.getFirst("id")),
                room,
                classes,
                teacher,
                course,
                start,
                end
            );


            if(start.isAfter(end) || start.getHour() < 8 || (end.getHour() >= 20 && end.getMinute() > 0 && end.getSecond() > 0) || start.plusHours(12).isBefore(end)){
                //processed
                model.addAttribute("message", "Un cours ne peut pas exceder 12h, commencer avant 8h00 ou terminer apres 20h00.");
                model.addAttribute("messageType", "warning");
            }
            else if(room == null || classes == null || teacher == null || course == null){
                model.addAttribute("message", "Des champs sont incorrect ou incomplet.");
                model.addAttribute("messageType", "error");

                scheduleTemplate.initModel(model, null, false, requestContentValidator);
            }
            else if(this.scheduleService.update(details) != null){
                //processed
                model.addAttribute("message", "Le cours a été mit à jour.");
                model.addAttribute("messageType", "success");
            }
            else{
                //unexcepted error in process
                model.addAttribute("message", "Une erreur est survenue :/");
                model.addAttribute("messageType", "warning");
            }


        } 
        else{
            //failed validation
            model.addAttribute("message", "Des champs sont incorrect ou incomplet.");
            model.addAttribute("messageType", "error");

            scheduleTemplate.initModel(model, null, false, requestContentValidator);
        }

        scheduleTemplate.prepareRedirect(model, redirectAttributes);
        return "redirect:/"+absoluteURL;  
    }

    /*
     * POST - http://localhost:8080/admin/student
     * Page to manage students datas.
     * 
     */
    @PostMapping("/schedule")
    public String post(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException{

        //Create validator to validate request content
        Validator requestContentValidator = new Validator(Map.of(
            "course", "required|int|min=0",
            "teacher", "required|int|min=0",
            "class", "required|int|min=0",
            "room", "required|int|min=0",
            "start", "required|datetime",
            "end", "required|datetime"
            )
        );

        if( requestContentValidator.validateRequest(request) ){ //validate the request

            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

            LocalDateTime start = LocalDateTime.parse(request.getFirst("start"), formatter);
            LocalDateTime end = LocalDateTime.parse(request.getFirst("end"), formatter);

            Optional<Courses> courseOp = this.coursesService.getById(Long.parseLong(request.getFirst("course")));
            Courses course = courseOp.isPresent() ? courseOp.get() : null;

            Optional<Teacher> teacherOp = this.teacherService.getTeachersById(Long.parseLong(request.getFirst("teacher")));
            Teacher teacher = teacherOp.isPresent() ? teacherOp.get() : null;

            Optional<Classes> classesOp = this.clService.getById(Long.parseLong(request.getFirst("class")));
            Classes classes = classesOp.isPresent() ? classesOp.get() : null;

            Optional<Room> roonOp = this.roomService.getById(Long.parseLong(request.getFirst("room")));
            Room room = roonOp.isPresent() ? roonOp.get() : null;


            Schedule details = new Schedule(
                null,
                room,
                classes,
                teacher,
                course,
                start,
                end
            );


            if(start.isAfter(end) || start.getHour() < 8 || (end.getHour() >= 20 && end.getMinute() > 0 && end.getSecond() > 0) || start.plusHours(12).isBefore(end)){
                //processed
                model.addAttribute("message", "Un cours ne peut pas exceder 12h, commencer avant 8h00 ou terminer apres 20h00.");
                model.addAttribute("messageType", "warning");
            }
            else if(room == null || classes == null || teacher == null || course == null){
                model.addAttribute("message", "Des champs sont incorrect ou incomplet.");
                model.addAttribute("messageType", "error");

                scheduleTemplate.initModel(model, null, false, requestContentValidator);
            }
            else if(this.scheduleService.create(details) != null){
                //processed
                model.addAttribute("message", "Le cours a été ajouté.");
                model.addAttribute("messageType", "success");
            }
            else{
                    //unexcepted error in process
                model.addAttribute("message", "Une erreur est survenue :/");
                model.addAttribute("messageType", "warning");
            }

        } 
        else{
            //failed validation
            model.addAttribute("message", "Des champs sont incorrect ou incomplet.");
            model.addAttribute("messageType", "error");

            scheduleTemplate.initModel(model, null, false, requestContentValidator);
        }

        scheduleTemplate.prepareRedirect(model, redirectAttributes);
        return "redirect:/"+absoluteURL;  
    }

    /*
     * POST - http://localhost:8080/admin/student
     * Page to manage students datas.
     * 
     */
    @DeleteMapping("/schedule")
    public String delete(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException{

        //Create validator to validate request content
        Validator requestContentValidator = new Validator(Map.of(
                "id", "required|int|min=0" //must be a number 0 min, MANDATORY
            )
        );

        if( requestContentValidator.validateRequest(request) ){ //validate the request

            this.coursesService.delete(Long.parseLong(request.getFirst("id")));
            model.addAttribute("message", "Le cours a été supprimé.");
            model.addAttribute("messageType", "success");
 
        } 
        else{
            //failed validation
            model.addAttribute("message", "Des champs sont incorrect ou incomplet.");
            model.addAttribute("messageType", "error");
        }

        scheduleTemplate.prepareRedirect(model, redirectAttributes);
        return "redirect:/"+absoluteURL;  
    }
}
