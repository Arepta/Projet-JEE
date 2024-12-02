package com.example.edu.tool.Validator.rules;

import org.springframework.util.MultiValueMap;
import com.example.edu.tool.Validator.Rule;

/*
 * Rule to validate if a field is a valid date (yyyy-MM-dd)
 */
public class Date extends Rule {

    // Constructor to initialize parameters for the rule
    public Date(String params) {
        super(params);
    }

    // Method to check if the field values match the date format
    public boolean check(String name, MultiValueMap<String, String> body) {
        for (String v : body.get(name)) {
            if (!v.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                return false;
            }
        }
        return true;
    }

    // Method to return an error message if validation fails
    public String getErrorMessage(String name) {
        return "Le champ '" + name + "' doit être une date au format yyyy-MM-dd.";
    }
}
