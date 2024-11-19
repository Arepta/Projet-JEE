package com.example.edu.tool.template;

import java.util.List;
import java.util.Map;

import org.springframework.ui.Model;

import com.example.edu.tool.Validator.Validator;

public class TableSingle extends Template{

    public TableSingle(String title, Map<String, String> columnToLabel, List<String> columnDisplayed){
        super(title, columnToLabel, columnDisplayed);
    }

    public <T> void initModel(Model model, List<T> data, Class<T> dataClass){
        model.addAttribute("_tableSingle_DataHeader", getClassAttributes(dataClass)); 
        model.addAttribute("_tableSingle_Title", this.title); 
        model.addAttribute("_tableSingle_Data", data); 
        model.addAttribute("_tableSingle_ColumnToLabel", this.columnToLabel); 
        model.addAttribute("_tableSingle_ColumnDisplayed", this.columnDisplayed); 
        model.addAttribute("_tableSingle_Type", this.typeJavaToHTML); 
    }

    public <T> void initModel(Model model, List<T> data,  Class<T> dataClass, boolean isCreate, Validator requestValiadtor){
        this.initModel(model, data, dataClass);
        model.addAttribute("_tableSingle_ErrorField", requestValiadtor.getErrors()); 
        model.addAttribute("_tableSingle_ErrorMessages", this.replaceColumnInString(requestValiadtor.getErrorsMessages())); 
        model.addAttribute("_tableSingle_OldField", requestValiadtor.getValidatedValue());
        model.addAttribute("_tableSingle_SetCreate", isCreate); 
    }
}
