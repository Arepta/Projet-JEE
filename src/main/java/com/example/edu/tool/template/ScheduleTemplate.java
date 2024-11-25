package com.example.edu.tool.template;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.springframework.ui.Model;

import com.example.edu.model.Schedule;
import com.example.edu.tool.Validator.Validator;

public class ScheduleTemplate extends Template{
    private boolean isViewAdmin;
    private Map<String, Supplier<Map<?, String>> > nonGeneriqueValues;
    private Map<String, String> links;
    private Map<String, Supplier< Map<Long,List<Long>> >> linksData;

    public ScheduleTemplate(String title, boolean isViewAdmin){
        super(title, Map.of(
            "class", "Classe",
            "course", "Cours",
            "teacher", "Professeur",
            "room", "Salle",
            "start", "Debut",
            "end", "Fin"
        ), null);
        this.isViewAdmin = isViewAdmin;
        this.nonGeneriqueValues = new HashMap<>();
        this.links = new HashMap<>();
        this.linksData = new HashMap<>();
    }


    public void initModel(Model model, List<Schedule> data){
        Map<String, Map<?, String>> buffer_nonGeneriqueValues=  new HashMap<>();

        for(String key : this.nonGeneriqueValues.keySet()){
            buffer_nonGeneriqueValues.put(key, nonGeneriqueValues.get(key).get());
        }

        Map<String, Map<Long,List<Long>> > buffer_linksData = new HashMap<>();

        for(String key : this.linksData.keySet()){
            buffer_linksData.put(key, linksData.get(key).get());
        }

        model.addAttribute("_schedule_Title", this.title); 
        model.addAttribute("_schedule_Admin", this.isViewAdmin); 
        model.addAttribute("_schedule_Data", data); 
        model.addAttribute("_schedule_NGValues", buffer_nonGeneriqueValues); 
        model.addAttribute("_schedule_NGValuesJSON", this.gson.toJson(buffer_nonGeneriqueValues)); 
        model.addAttribute("_schedule_Links", this.gson.toJson(this.links)); 
        model.addAttribute("_schedule_LinksData", this.gson.toJson(buffer_linksData)); 
    }

    public void initModel(Model model, List<Schedule> data, boolean isCreate, Validator requestValiadtor){
        this.initModel(model, data);
        model.addAttribute("_schedule_ErrorField", requestValiadtor.getErrors()); 
        model.addAttribute("_schedule_ErrorMessages", this.replaceColumnInString(requestValiadtor.getErrorsMessages())); 
        model.addAttribute("_schedule_OldField", requestValiadtor.getValidatedValue());
        if(!isCreate) model.addAttribute("_schedule_setEdit", false);
    }

    public void setValuesFor(String column, Supplier<Map<?, String>>  MethodForvalueToLabel){
        this.nonGeneriqueValues.put(column, MethodForvalueToLabel);
    }

    public void addLink(String From, String To, Supplier<Map<Long,List<Long>>> FromValueOfToValues){
        this.links.put(From, To);
        this.linksData.put(From+"-"+To, FromValueOfToValues);
    }
}
