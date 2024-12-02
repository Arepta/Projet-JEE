package com.example.edu.tool.Validator.rules;

import java.util.Arrays;
import java.util.List;
import org.springframework.util.MultiValueMap;
import com.example.edu.tool.Validator.Rule;
import com.example.edu.tool.Validator.Exceptions.InvalidParameterException;

/*
 * Rule to ensure that a field contains only values from a predefined list
 */
public class Value extends Rule {

    // Constructor to initialize parameters for the rule
    public Value(String params) {
        super(params);
    }

    // Method to check if the field value is within the allowed values
    public boolean check(String name, MultiValueMap<String, String> body) throws InvalidParameterException {
        List<String> values = Arrays.asList(this.params.split(","));

        for (String v : body.get(name)) {
            if (!values.contains(v)) {
                return false;
            }
        }
        return true;
    }

    // Method to return an error message if validation fails
    public String getErrorMessage(String name) {
        return "Le champ '" + name + "' doit être dans la liste de valeurs suivante : " + this.params + ".";
    }
}
