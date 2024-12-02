package com.example.edu.tool.Validator.rules;

import java.lang.Integer;
import org.springframework.util.MultiValueMap;
import com.example.edu.tool.Validator.Rule;
import com.example.edu.tool.Validator.Exceptions.InvalidParameterException;

/*
 * Rule to validate the minimum number of values required for a field
 */
public class Amin extends Rule {

    // Constructor to initialize parameters for the rule
    public Amin(String params) {
        super(params);
    }

    // Method to check if the number of values for the field is greater than or equal to the minimum required
    public boolean check(String name, MultiValueMap<String, String> body) throws InvalidParameterException {
        int min = 0;
        try {
            min = Integer.valueOf(this.params).intValue();
        } catch (NumberFormatException e) {
            throw new InvalidParameterException("'amin' rule can only use parameter of type Int, not '" + this.params + "'.");
        }

        return body.get(name).size() >= min;
    }

    // Method to return an error message if validation fails
    public String getErrorMessage(String name) {
        return "Le champ '" + name + "' doit contenir au minimum " + this.params + " choix.";
    }
}
