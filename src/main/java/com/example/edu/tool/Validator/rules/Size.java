package com.example.edu.tool.Validator.rules;

import java.lang.Integer;
import org.springframework.util.MultiValueMap;
import com.example.edu.tool.Validator.Rule;
import com.example.edu.tool.Validator.Exceptions.InvalidParameterException;

/*
 * Rule to ensure that a field contains a specific number of values
 */
public class Size extends Rule {

    // Constructor to initialize parameters for the rule
    public Size(String params) {
        super(params);
    }

    // Method to check if the number of values matches the required size
    public boolean check(String name, MultiValueMap<String, String> body) throws InvalidParameterException {
        int size = 0;
        try {
            size = Integer.valueOf(this.params).intValue();
        } catch (NumberFormatException e) {
            throw new InvalidParameterException("'asize' rule can only use parameter of type Int, not '" + this.params + "'.");
        }

        return body.get(name).size() == size;
    }

    // Method to return an error message if validation fails
    public String getErrorMessage(String name) {
        return "Le champ '" + name + "' doit contenir exactement " + this.params + " choix.";
    }
}
