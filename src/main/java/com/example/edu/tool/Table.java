package com.example.edu.tool;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.ui.Model;

import com.example.edu.tool.Validator.Validator;

public class Table {
    
    private final static Map<String, String> javaTypeToHTML = Map.of(
        LocalDate.class.getName(), "date",
        String.class.getName(), "text",
        long.class.getName(), "number",
        Integer.class.getName(), "number",
        int.class.getName(), "number",
        double.class.getName(), "number",
        Double.class.getName(), "number",
        boolean.class.getName(), "select"
    );
    
    
    private static Map<String, String> getClassAttributes(Object obj) {
        Map<String, String> attributes = new HashMap<>();
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            attributes.put(field.getName().toLowerCase(), field.getType().getName());
        }
        return attributes;
    }
    
    public static void setup(Model model, String title, List<Object> data, Map<String, String> fieldToLabel, List<String> resitricedLineDisplay){
        model.addAttribute("_tableDataHeader", getClassAttributes(data.get(0))); 
        model.addAttribute("_tableTitle", title); 
        model.addAttribute("_tableData", data); 
        model.addAttribute("_tableFieldToLabel", fieldToLabel); 
        model.addAttribute("_tableLineDisplay", resitricedLineDisplay); 
        model.addAttribute("_tableType", javaTypeToHTML); 
    }
    
    
    
    public static void setup(Model model, String title, List<Object> data, Map<String, String> fieldToLabel){
        setup(model, title, data, fieldToLabel, null);
    }
    
    public static void setup(Model model, String title, List<Object> data, List<String> resitricedLineDisplay){
        setup(model, title, data, null, resitricedLineDisplay);
    }
    
    public static void setup(Model model, String title, List<Object> data){
        setup(model, title, data, null, null);
    }
    
    
    public static void setupWithError(Model model, String title, List<Object> data, Map<String, String> fieldToLabel, List<String> resitricedLineDisplay, Validator requestValiadtor){
        List<String> errMsg = new ArrayList<>();
        
        
        // Regular expression to find substrings enclosed in single quotes
        Pattern pattern = Pattern.compile("'(.*?)'");
        Matcher matcher = pattern.matcher("");
        
        for (String key : requestValiadtor.getErrors().keySet()) {


            matcher = pattern.matcher(requestValiadtor.getErrors().get(key));
            
            // StringBuilder for the result
            StringBuffer result = new StringBuffer();
            
            // Iterate over all matches
            while (matcher.find()) {
                String field = matcher.group(1); // Extract the key (without quotes)
                String replacement = "'"+fieldToLabel.getOrDefault(field, matcher.group(0))+"'"; // Find the replacement or keep the original
                matcher.appendReplacement(result, replacement);
            }

            matcher.appendTail(result);

            errMsg.add(result.toString());
        }
        
        model.addAttribute("_tableDataHeader", getClassAttributes(data.get(0))); 
        model.addAttribute("_tableTitle", title); 
        model.addAttribute("_tableData", data); 
        model.addAttribute("_tableErrorField", requestValiadtor.getErrors()); 
        model.addAttribute("_tableErrorMessages", errMsg); 
        model.addAttribute("_tableOldField", requestValiadtor.getValidatedValue());
        model.addAttribute("_tableFieldToLabel", fieldToLabel); 
        model.addAttribute("_tableLineDisplay", resitricedLineDisplay); 
        model.addAttribute("_tableType", javaTypeToHTML); 
    }
    
    public static void setupWithError(Model model, String title, List<Object> data, Map<String, String> fieldToLabel, Validator requestValiadtor){
        setupWithError(model, title, data, fieldToLabel, null, requestValiadtor);
    }
    
    public static void setupWithError(Model model, String title, List<Object> data, List<String> resitricedLineDisplay, Validator requestValiadtor){
        setupWithError(model, title, data, null, resitricedLineDisplay, requestValiadtor);
    }
    
    public static void setupWithError(Model model, String title, List<Object> data, Validator requestValiadtor){
        setupWithError(model, title, data, null, null, requestValiadtor);
    }
}
