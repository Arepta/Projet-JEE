package com.example.edu.tool.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.springframework.ui.Model;

import com.example.edu.tool.Validator.Validator;

public class TableSingle extends Template{

    private List<String> lockColumns;
    private List<String> filters;
    private Map<String, Supplier<Map<?, String>>> nonGeneriqueValues;

    public TableSingle(String title, Map<String, String> columnToLabel, List<String> columnDisplayed){
        super(title, columnToLabel, columnDisplayed);
        this.lockColumns = new ArrayList<>();
        this.filters = new ArrayList<>();
        this.nonGeneriqueValues = new HashMap<>();

        this.addLock("id");
    }

    public <T> void initModel(Model model, List<T> data, Class<T> dataClass){
        Map<String, Map<?, String>> buffer_nonGeneriqueValues=  new HashMap<>();

        for(String key : this.nonGeneriqueValues.keySet()){
            buffer_nonGeneriqueValues.put(key, nonGeneriqueValues.get(key).get());
        }

        model.addAttribute("_tableSingle_Title", this.title); 
        model.addAttribute("_tableSingle_DataHeader", getClassAttributes(dataClass)); 
        model.addAttribute("_tableSingle_ColumnLock", this.lockColumns); 
        model.addAttribute("_tableSingle_ColumnToLabel", this.columnToLabel); 
        model.addAttribute("_tableSingle_ColumnDisplayed", this.columnDisplayed); 
        model.addAttribute("_tableSingle_Data", data); 
        model.addAttribute("_tableSingle_NGValues", buffer_nonGeneriqueValues); 
        model.addAttribute("_tableSingle_Filters", filters); 
        model.addAttribute("_tableSingle_Type", this.typeJavaToHTML); 
    }

    public <T> void initModel(Model model, List<T> data,  Class<T> dataClass, boolean isCreate, Validator requestValiadtor){
        this.initModel(model, data, dataClass);
        model.addAttribute("_tableSingle_ErrorField", requestValiadtor.getErrors()); 
        model.addAttribute("_tableSingle_ErrorMessages", this.replaceColumnInString(requestValiadtor.getErrorsMessages())); 
        model.addAttribute("_tableSingle_OldField", requestValiadtor.getValidatedValue());
        model.addAttribute("_tableSingle_SetCreate", isCreate); 
    }

    public void addLock(String For){
        this.lockColumns.add(For);
    }

    public void addFilter(String For){
        this.filters.add(For);
    }

    public void setValuesFor(String column, Supplier<Map<?, String>>  MethodForvalueToLabel){
        this.nonGeneriqueValues.put(column, MethodForvalueToLabel);
    }

}
