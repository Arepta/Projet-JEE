package com.example.edu.tool.Validator.rules;

import org.springframework.util.MultiValueMap;
import com.example.edu.tool.Validator.Rule;

/*
 * Rule to validate if a field is a valid double value
 */
public class Double extends Rule {

    // Constructor to initialize parameters for the rule
    public Double(String params) {
        super(params);
    }

    // Method to check if the field values match the double format
    public boolean check(String name, MultiValueMap<String, String> body) {
        for (String v : body.get(name)) {
            if (!v.matches("[0-9]{1,13}(\\.[0-9]*)?")) {
                return false;
            }
        }
        return true;
    }

    // Method to return an error message if validation fails
    public String getErrorMessage(String name) {
        return "Le champ '" + name + "' doit être une valeur décimale.";
    }
}
