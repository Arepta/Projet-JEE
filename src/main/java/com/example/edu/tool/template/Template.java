package com.example.edu.tool.template;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.google.gson.Gson;


/*
 * Default configuration for template
 */
public abstract class Template {

    /*
     * Store a cast systeme for java type to HTML type.
     */
    protected final Map<String, String> typeJavaToHTML = Map.of(
        LocalDate.class.getName(), "date",
        String.class.getName(), "text",
        Long.class.getName(), "number",
        long.class.getName(), "number",
        Integer.class.getName(), "number",
        int.class.getName(), "number",
        double.class.getName(), "number",
        Double.class.getName(), "number"
    );

    protected String title; //title
    protected Map<String, String> columnToLabel; //store cast for SQL column name to displayed name
    protected List<String> columnDisplayed; // store the column that are display per line
    protected Gson gson;

    protected Template(String title, Map<String, String> columnToLabel, List<String> columnDisplayed){
        this.title = title;
        this.columnToLabel = columnToLabel;
        this.columnDisplayed = columnDisplayed;
        this.gson = new Gson();
    }


    public Map<String, String> getColumnToLabel(){
        return this.columnToLabel;
    }

    public void setColumnToLabel(Map<String, String> columnToLabel){
        this.columnToLabel = columnToLabel;
    }

    public List<String> getColumnDisplayed(){
        return columnDisplayed;
    }

    public void setColumnDisplayed(List<String> columnDisplayed){
        this.columnDisplayed = columnDisplayed;
    }

    public String getTitle(){
        return this.title;
    }

    public void setTitle(String title){
        this.title = title;
    }

    /*
     * Get a hashmap with class attributes' as key (in lower case) and there java type as value
     */
    protected <T> Map<String, String> getClassAttributes(Class<T> cls) {
        Map<String, String> attributes = new HashMap<>();
        for (Field field : cls.getDeclaredFields()) {
            attributes.put(field.getName().toLowerCase(), field.getType().getName());
        }
        return attributes;
    }

    /*
     * Replace all apparition | 'SQLColumnName' | by | 'columnToLabelEquivalent' |.
     */
    protected List<String> replaceColumnInString(List<String> stringList){

        if(this.columnToLabel == null){
            return stringList;
        }

        List<String> updatedStringList = new ArrayList<>();

        
        // Regular expression to find substrings enclosed in single quotes
        Pattern pattern = Pattern.compile("'(.*?)'");
        Matcher matcher = pattern.matcher("");
        
        for (String string : stringList) {

            matcher = pattern.matcher(string);
            
            // StringBuilder for the result
            StringBuffer result = new StringBuffer();
            
            // Iterate over all matches
            while (matcher.find()) {
                String field = matcher.group(1); // Extract the key (without quotes)
                String replacement = "'"+this.columnToLabel.getOrDefault(field, matcher.group(0))+"'"; // Find the replacement or keep the original
                matcher.appendReplacement(result, replacement);
            }

            matcher.appendTail(result);

            updatedStringList.add(result.toString());
        }

        return updatedStringList;
    }

    public void prepareRedirect(Model model, RedirectAttributes redirectAttributes){
        // Transfer all attributes from the Model to RedirectAttributes
        for (String attributeName : model.asMap().keySet()) {
            redirectAttributes.addFlashAttribute(attributeName, model.asMap().get(attributeName));
        }
    }

}
