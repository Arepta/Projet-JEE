package com.example.edu.tool.Validator.rules;

import org.springframework.util.MultiValueMap;
import com.example.edu.tool.Validator.Rule;

/*
 * Rule to ensure that a field is required (i.e., must be filled in)
 */
public class Required extends Rule {

    // Constructor to initialize parameters for the rule
    public Required(String params) {
        super(params);
    }

    // Method to check if the field is present and non-empty
    public boolean check(String name, MultiValueMap<String, String> body) {
        return !body.get(name).isEmpty();
    }

    // Method to return an error message if validation fails
    public String getErrorMessage(String name) {
        return "Le champ '" + name + "' doit être rempli.";
    }
}
