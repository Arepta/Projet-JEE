package com.example.edu.tool.template;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.springframework.ui.Model;

import com.example.edu.model.Schedule;
import com.example.edu.tool.Validator.Validator;

public class ScheduleTemplate extends Template {

    // Indicates if the view is for admin purposes
    private boolean isViewAdmin;

    // Stores non-generic values with their labels
    private Map<String, Supplier<Map<?, String>>> nonGeneriqueValues;

    // Stores relationships between linked values
    private Map<String, String> links;
    private Map<String, Supplier<Map<Long, List<Long>>>> linksData;

    // Constructor to initialize the template with title and default values
    public ScheduleTemplate(String title, boolean isViewAdmin) {
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

    // Method to initialize the model with schedule data
    public void initModel(Model model, List<Schedule> data) {
        Map<String, Map<?, String>> buffer_nonGeneriqueValues = new HashMap<>();
        for (String key : this.nonGeneriqueValues.keySet()) {
            buffer_nonGeneriqueValues.put(key, nonGeneriqueValues.get(key).get());
        }

        Map<String, Map<Long, List<Long>>> buffer_linksData = new HashMap<>();
        for (String key : this.linksData.keySet()) {
            buffer_linksData.put(key, linksData.get(key).get());
        }

        // Add attributes to the model for rendering
        model.addAttribute("_schedule_Title", this.title);
        model.addAttribute("_schedule_Admin", this.isViewAdmin);
        model.addAttribute("_schedule_Data", data);
        model.addAttribute("_schedule_NGValues", buffer_nonGeneriqueValues);
        model.addAttribute("_schedule_NGValuesJSON", this.gson.toJson(buffer_nonGeneriqueValues));
        model.addAttribute("_schedule_Links", this.gson.toJson(this.links));
        model.addAttribute("_schedule_LinksData", this.gson.toJson(buffer_linksData));
    }

    // Overloaded method to initialize the model with additional validation data
    public void initModel(Model model, List<Schedule> data, boolean isCreate, Validator requestValidator) {
        this.initModel(model, data);
        model.addAttribute("_schedule_ErrorField", requestValidator.getErrors());
        model.addAttribute("_schedule_ErrorMessages", this.replaceColumnInString(requestValidator.getErrorsMessages()));
        model.addAttribute("_schedule_OldField", requestValidator.getValidatedValue());
        if (!isCreate) model.addAttribute("_schedule_setEdit", false);
    }

    // Method to set values for a given column using a supplier
    public void setValuesFor(String column, Supplier<Map<?, String>> MethodForValueToLabel) {
        this.nonGeneriqueValues.put(column, MethodForValueToLabel);
    }

    // Method to add a link between two columns
    public void addLink(String from, String to, Supplier<Map<Long, List<Long>>> fromValueOfToValues) {
        this.links.put(from, to);
        this.linksData.put(from + "-" + to, fromValueOfToValues);
    }
}
