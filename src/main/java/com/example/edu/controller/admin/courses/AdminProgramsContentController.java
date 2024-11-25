package com.example.edu.controller.admin.courses;

import java.util.Arrays;
import java.util.List;
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

import com.example.edu.model.Courses;
import com.example.edu.model.Program;
import com.example.edu.model.ProgramsContent;
import com.example.edu.service.CoursesService;
import com.example.edu.service.ProgramService;
import com.example.edu.service.ProgramsContentService;
import com.example.edu.tool.Validator.Validator;
import com.example.edu.tool.Validator.Exceptions.unknownRuleException;
import com.example.edu.tool.template.TableSingle;

@Controller
@RequestMapping("/admin/courses")
public class AdminProgramsContentController {
    
    private final CoursesService coursesService;
    private final ProgramService programService;
    private final ProgramsContentService programsContentService;

    private final String absoluteURL = "admin/courses/program";
    private TableSingle tableTemplate;


    @Autowired
    public AdminProgramsContentController(CoursesService coursesService, ProgramService programService, ProgramsContentService programsContentService) {
        this.coursesService = coursesService;
        this.programService = programService;
        this.programsContentService = programsContentService;

        Map<String, String> columnToLabel = Map.of( 
            "program","Cursus",
            "course","Cours"
        );

        List<String> columnDisplayed = Arrays.asList("program", "course");
        this.tableTemplate = new TableSingle("Contenu des cursus", columnToLabel, columnDisplayed);

        this.tableTemplate.setValuesFor("course", this.coursesService::getAllIdxName);
        this.tableTemplate.setValuesFor("program", this.programService::getAllIdxName);
        this.tableTemplate.addFilter("program");
        this.tableTemplate.addFilter("course");
    }

    /*
     * GET - http://localhost:8080/admin/student
     * Page to manage students datas.
     * 
     */
    @GetMapping("/program")
    public String get(Model model) {

        tableTemplate.initModel(model, this.programsContentService.getAll(), ProgramsContent.class);
        return "admin/default";  
    }

    
    /*
     * PUT - http://localhost:8080/admin/student
     * update a student.
     * 
     */
    @PutMapping("/program")
    public String put(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException{

        model.addAttribute("message", "Vous ne pouvez que supprimer ou ajouter.");
        model.addAttribute("messageType", "warning");

        tableTemplate.prepareRedirect(model, redirectAttributes);
        return "redirect:/"+absoluteURL;  
    }

    /*
     * POST - http://localhost:8080/admin/student
     * Page to manage students datas.
     * 
     */
    @PostMapping("/program")
    public String post(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException{

         //Create validator to validate request content
        Validator requestContentValidator = new Validator(Map.of(
            "program", "required|int|min=0",
            "course", "required|int|min=0"
            )
        );

        if( requestContentValidator.validateRequest(request) ){ //validate the request

            Optional<Program> ProgramOp = this.programService.getById(Long.parseLong(request.getFirst("program")));
            Program program = ProgramOp.isPresent() ? ProgramOp.get() : null;
            Optional<Courses> CoursesOp = this.coursesService.getById(Long.parseLong(request.getFirst("course")));
            Courses course = CoursesOp.isPresent() ? CoursesOp.get() : null;

            ProgramsContent details = new ProgramsContent(
                program,
                course
            );

    
            if( program == null || course == null ){
                model.addAttribute("message", "Des champs sont incorrect ou incomplet.");
                model.addAttribute("messageType", "error");

                tableTemplate.initModel(model, null, ProgramsContent.class, true, requestContentValidator);
            }
            else if( this.programsContentService.getById( details.getId() ).isPresent() ){
                model.addAttribute("message", "Le cours existe deja pour ce cursus");
                model.addAttribute("messageType", "error");

                tableTemplate.initModel(model, null, ProgramsContent.class, true, requestContentValidator);
            }
            else if(this.programsContentService.create(details) != null){
                //processed
                model.addAttribute("message", "Le contenu a été ajouté.");
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

            tableTemplate.initModel(model, null, ProgramsContent.class, true, requestContentValidator);
        }

        tableTemplate.prepareRedirect(model, redirectAttributes);
        return "redirect:/"+absoluteURL;  
    }

    /*
     * POST - http://localhost:8080/admin/student
     * Page to manage students datas.
     * 
     */
    @DeleteMapping("/program")
    public String delete(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException{

        //Create validator to validate request content
        Validator requestContentValidator = new Validator(Map.of(
            "program", "required|int|min=0",
            "course", "required|int|min=0"
            )
        );

        if( requestContentValidator.validateRequest(request) ){ //validate the request

            Optional<Program> ProgramOp = this.programService.getById(Long.parseLong(request.getFirst("program")));
            Program program = ProgramOp.isPresent() ? ProgramOp.get() : null;
            Optional<Courses> CoursesOp = this.coursesService.getById(Long.parseLong(request.getFirst("course")));
            Courses course = CoursesOp.isPresent() ? CoursesOp.get() : null;

            ProgramsContent details = new ProgramsContent(
                program,
                course
            );

           

            if( program == null || course == null ){
                model.addAttribute("message", "Des champs sont incorrect ou incomplet.");
                model.addAttribute("messageType", "error");

                tableTemplate.initModel(model, null, ProgramsContent.class, false, requestContentValidator);
            }
            else{
                this.programsContentService.delete(details.getId());
            }

            model.addAttribute("message", "Le cours a été supprimé.");
            model.addAttribute("messageType", "success");
 
        } 
        else{
            //failed validation
            model.addAttribute("message", "Des champs sont incorrect ou incomplet.");
            model.addAttribute("messageType", "error");
        }

        tableTemplate.prepareRedirect(model, redirectAttributes);
        return "redirect:/"+absoluteURL;  
    }
}
