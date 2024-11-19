package com.example.edu.tool.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.ui.Model;

import com.example.edu.tool.Validator.Validator;
import com.google.gson.Gson;

public class TableSingle extends Template{

    private List<String> filters;
    private Map<String, Map<?, String>> filterValueToLabel;
    private Gson gson;

    public TableSingle(String title, Map<String, String> columnToLabel, List<String> columnDisplayed){
        super(title, columnToLabel, columnDisplayed);
        this.filters = new ArrayList<>();
        this.filterValueToLabel = new HashMap<>();
        this.gson = new Gson();
    }

    public <T> void initModel(Model model, List<T> data, Class<T> dataClass){
        
        model.addAttribute("_tableSingle_Title", this.title); 
        model.addAttribute("_tableSingle_DataHeader", getClassAttributes(dataClass)); 
        model.addAttribute("_tableSingle_ColumnToLabel", this.columnToLabel); 
        model.addAttribute("_tableSingle_ColumnDisplayed", this.columnDisplayed); 
        model.addAttribute("_tableSingle_Data", data); 
        model.addAttribute("_tableSingle_Filters", this.filters); 
        model.addAttribute("_tableSingle_FiltersJSON", this.gson.toJson( this.filters)); 
        model.addAttribute("_tableSingle_FiltersValueToLabel", this.gson.toJson(this.filterValueToLabel)); 
        model.addAttribute("_tableSingle_Type", this.typeJavaToHTML); 
    }

    public <T> void initModel(Model model, List<T> data,  Class<T> dataClass, boolean isCreate, Validator requestValiadtor){
        this.initModel(model, data, dataClass);
        model.addAttribute("_tableSingle_ErrorField", requestValiadtor.getErrors()); 
        model.addAttribute("_tableSingle_ErrorMessages", this.replaceColumnInString(requestValiadtor.getErrorsMessages())); 
        model.addAttribute("_tableSingle_OldField", requestValiadtor.getValidatedValue());
        model.addAttribute("_tableSingle_SetCreate", isCreate); 
    }

    public void addFilter(String For){
        this.filters.add(For);
    }

    public void addFilter(String For, Map<?, String> valueToLabel){
        this.filters.add(For);
        this.filterValueToLabel.put(For, valueToLabel);
    }

    public List<String> getFilter(){
        return this.filters;
    }

    public void clearFilter() {
        this.filters.clear();
        this.filterValueToLabel.clear();
    }
}
