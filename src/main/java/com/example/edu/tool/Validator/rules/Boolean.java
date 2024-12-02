package com.example.edu.tool.Validator.rules;

import org.springframework.util.MultiValueMap;
import com.example.edu.tool.Validator.Rule;

/*
 * Rule to validate if a field is a boolean (true/false or 1/0)
 */
public class Boolean extends Rule {

    // Constructor to initialize parameters for the rule
    public Boolean(String params) {
        super(params);
    }

    // Method to check if the field values match boolean values
    public boolean check(String name, MultiValueMap<String, String> body) {
        for (String v : body.get(name)) {
            if (!v.matches("true|false|1|0")) {
                return false;
            }
        }
        return true;
    }

    // Method to return an error message if validation fails
    public String getErrorMessage(String name) {
        return "Le champ '" + name + "' doit être vrai ou faux.";
    }
}
