package com.example.edu.tool.Validator.rules;

import org.springframework.util.MultiValueMap;
import com.example.edu.tool.Validator.Rule;

/*
 * Rule to ensure that a field contains only a single value
 */
public class Single extends Rule {

    // Constructor to initialize parameters for the rule
    public Single(String params) {
        super(params);
    }

    // Method to check if the field contains only one value
    public boolean check(String name, MultiValueMap<String, String> body) {
        return body.get(name).size() <= 1;
    }

    // Method to return an error message if validation fails
    public String getErrorMessage(String name) {
        return "Le champ '" + name + "' ne peut contenir qu'une seule proposition.";
    }
}
