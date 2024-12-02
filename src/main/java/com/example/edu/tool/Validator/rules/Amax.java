package com.example.edu.tool.Validator.rules;

import java.lang.Integer;
import org.springframework.util.MultiValueMap;
import com.example.edu.tool.Validator.Rule;
import com.example.edu.tool.Validator.Exceptions.InvalidParameterException;

/*
 * Rule to validate the maximum number of values allowed for a field
 */
public class Amax extends Rule {

    // Constructor to initialize parameters for the rule
    public Amax(String params) {
        super(params);
    }

    // Method to check if the number of values for the field is less than or equal to the maximum allowed
    public boolean check(String name, MultiValueMap<String, String> body) throws InvalidParameterException {
        int max = 0;
        try {
            max = Integer.valueOf(this.params).intValue();
        } catch (NumberFormatException e) {
            throw new InvalidParameterException("'amax' rule can only use parameter of type Int, not '" + this.params + "'.");
        }

        return body.get(name).size() <= max;
    }

    // Method to return an error message if validation fails
    public String getErrorMessage(String name) {
        return "Le champ '" + name + "' doit contenir au maximum " + this.params + " choix.";
    }
}
