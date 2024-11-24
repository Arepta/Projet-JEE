package com.example.edu.controller.admin.config;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

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


import com.example.edu.model.Room;
import com.example.edu.service.RoomService;
import com.example.edu.tool.template.TableSingle;
import com.example.edu.tool.Validator.Validator;
import com.example.edu.tool.Validator.Exceptions.unknownRuleException;


@Controller
@RequestMapping("/admin/config")
public class AdminRoomController {

    private final RoomService roomService;
    private final String absoluteURL = "admin/config/room";
    private TableSingle tableTemplate;

    @Autowired
    public AdminRoomController(RoomService roomService) {
        this.roomService = roomService;

        Map<String, String> columnToLabel = Map.of( //translate attribute name to label name (More readable) [OPTIONAL]
            "id","ID",
            "name","Nom",
            "size","Taille"
        );
        List<String> columnDisplayed = Arrays.asList("id", "name", "size");
        this.tableTemplate = new TableSingle("Salle", columnToLabel, columnDisplayed);
    }

    /*
     * GET - http://localhost:8080/admin/student
     * Page to manage students datas.
     * 
     */
    @GetMapping("/room")
    public String get(Model model) {

        tableTemplate.initModel(model, this.roomService.getAll(), Room.class);
        return "admin/default";  
    }

    
    /*
     * PUT - http://localhost:8080/admin/student
     * update a student.
     * 
     */
    @PutMapping("/room")
    public String put(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException{

        //Create validator to validate request content
        Validator requestContentValidator = new Validator(Map.of(
            "id", "required|int|min=0",
            "name", "required|max=100",
            "size", "required|int|min=0"
            )
        );

        if( requestContentValidator.validateRequest(request) ){ //validate the request

            

            Room details = new Room(
                Long.parseLong(request.getFirst("id")),
                request.getFirst("name"), 
                Integer.parseInt(request.getFirst("size"))
            );


            if(this.roomService.update(details) != null){
                //processed
                model.addAttribute("message", "La salle a été mit à jour.");
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

            tableTemplate.initModel(model, null, Room.class, false, requestContentValidator);
        }

        tableTemplate.prepareRedirect(model, redirectAttributes);
        return "redirect:/"+absoluteURL;  
    }

    /*
     * POST - http://localhost:8080/admin/student
     * Page to manage students datas.
     * 
     */
    @PostMapping("/room")
    public String post(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException{

        //Create validator to validate request content
        Validator requestContentValidator = new Validator(Map.of(
            "name", "required|max=100",
            "size", "required|int|min=0"
            )
        );

        if( requestContentValidator.validateRequest(request) ){ //validate the request
            Room details = new Room(
                null,
                request.getFirst("name"), 
                Integer.parseInt(request.getFirst("size"))
            );

            if(this.roomService.create(details) != null){
                //processed
                model.addAttribute("message", "La salle a été ajoutée.");
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

            tableTemplate.initModel(model, null, Room.class, false, requestContentValidator);
        }

        tableTemplate.prepareRedirect(model, redirectAttributes);
        return "redirect:/"+absoluteURL;  
    }

    /*
     * POST - http://localhost:8080/admin/student
     * Page to manage students datas.
     * 
     */
    @DeleteMapping("/room")
    public String delete(Model model, RedirectAttributes redirectAttributes, @RequestParam MultiValueMap<String, String> request) throws unknownRuleException{

        //Create validator to validate request content
        Validator requestContentValidator = new Validator(Map.of(
                "id", "required|int|min=0" //must be a number 0 min, MANDATORY
            )
        );

        if( requestContentValidator.validateRequest(request) ){ //validate the request

            this.roomService.delete(Long.parseLong(request.getFirst("id")));
            model.addAttribute("message", "La salle a été supprimé.");
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
