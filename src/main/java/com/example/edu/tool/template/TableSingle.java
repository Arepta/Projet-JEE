package com.example.edu.tool.template;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.springframework.ui.Model;

import com.example.edu.tool.Validator.Validator;

public class TableSingle extends Template {

    // List of columns that should be locked (non-editable)
    private List<String> lockColumns;

    // List of filters applied to the table
    private List<String> filters;

    // Stores non-generic values for the table
    private Map<String, Supplier<Map<?, String>>> nonGeneriqueValues;

    // Stores relationships between linked values
    private Map<String, String> links;
    private Map<String, Supplier<Map<Long, List<Long>>>> linksData;

    // Constructor to initialize the table with title, column labels, and columns to display
    public TableSingle(String title, Map<String, String> columnToLabel, List<String> columnDisplayed) {
        super(title, columnToLabel, columnDisplayed);
        this.lockColumns = new ArrayList<>();
        this.filters = new ArrayList<>();
        this.nonGeneriqueValues = new HashMap<>();
        this.links = new HashMap<>();
        this.linksData = new HashMap<>();

        this.addLock("id"); // Locking the 'id' column by default
    }

    // Method to initialize the model with data for rendering the table
    public <T> void initModel(Model model, List<T> data, Class<T> dataClass) {
        Map<String, Map<?, String>> buffer_nonGeneriqueValues = new HashMap<>();
        for (String key : this.nonGeneriqueValues.keySet()) {
            buffer_nonGeneriqueValues.put(key, nonGeneriqueValues.get(key).get());
        }

        Map<String, Map<Long, List<Long>>> buffer_linksData = new HashMap<>();
        for (String key : this.linksData.keySet()) {
            buffer_linksData.put(key, linksData.get(key).get());
        }

        // Add attributes to the model for rendering
        model.addAttribute("_tableSingle_Title", this.title);
        model.addAttribute("_tableSingle_DataHeader", getClassAttributes(dataClass));
        model.addAttribute("_tableSingle_ColumnLock", this.lockColumns);
        model.addAttribute("_tableSingle_ColumnToLabel", this.columnToLabel);
        model.addAttribute("_tableSingle_ColumnDisplayed", this.columnDisplayed);
        model.addAttribute("_tableSingle_Data", data);
        model.addAttribute("_tableSingle_NGValues", buffer_nonGeneriqueValues);
        model.addAttribute("_tableSingle_NGValuesJSON", this.gson.toJson(buffer_nonGeneriqueValues));
        model.addAttribute("_tableSingle_Filters", filters);
        model.addAttribute("_tableSingle_Links", this.gson.toJson(this.links));
        model.addAttribute("_tableSingle_LinksData", this.gson.toJson(buffer_linksData));
        model.addAttribute("_tableSingle_Type", this.typeJavaToHTML);
    }

    // Overloaded method to initialize the model with validation data
    public <T> void initModel(Model model, List<T> data, Class<T> dataClass, boolean isCreate, Validator requestValidator) {
        this.initModel(model, data, dataClass);
        model.addAttribute("_tableSingle_ErrorField", requestValidator.getErrors());
        model.addAttribute("_tableSingle_ErrorMessages", this.replaceColumnInString(requestValidator.getErrorsMessages()));
        model.addAttribute("_tableSingle_OldField", requestValidator.getValidatedValue());
        if (!isCreate) model.addAttribute("_tableSingle_setEdit", false);
    }

    // Method to lock a column
    public void addLock(String column) {
        this.lockColumns.add(column);
    }

    // Method to add a filter to a column
    public void addFilter(String column) {
        this.filters.add(column);
    }

    // Method to set non-generic values for a column
    public void setValuesFor(String column, Supplier<Map<?, String>> MethodForValueToLabel) {
        this.nonGeneriqueValues.put(column, MethodForValueToLabel);
    }

    // Method to add a link between two columns
    public void addLink(String from, String to, Supplier<Map<Long, List<Long>>> fromValueOfToValues) {
        this.links.put(from, to);
        this.linksData.put(from + "-" + to, fromValueOfToValues);
    }

    // Method to add a filter link between two columns
    public void addFilterLink(String from, String to, Supplier<Map<Long, List<Long>>> fromValueOfToValues) {
        this.links.put("filter-" + from, "filter-" + to);
        this.linksData.put("filter-" + from + "-" + "filter-" + to, fromValueOfToValues);
    }
}
