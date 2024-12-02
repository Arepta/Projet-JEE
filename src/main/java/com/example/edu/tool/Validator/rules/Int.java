package com.example.edu.tool.Validator.rules;

import org.springframework.util.MultiValueMap;
import com.example.edu.tool.Validator.Rule;

/*
 * Rule to ensure that a field is an integer
 */
public class Int extends Rule {

    // Constructor to initialize parameters for the rule
    public Int(String params) {
        super(params);
    }

    // Method to check if the field value is an integer
    public boolean check(String name, MultiValueMap<String, String> body) {
        for (String v : body.get(name)) {
            if (!v.matches("-?\\d+")) {
                return false;
            }
        }
        return true;
    }

    // Method to return an error message if validation fails
    public String getErrorMessage(String name) {
        return "Le champ '" + name + "' doit être un entier.";
    }
}
