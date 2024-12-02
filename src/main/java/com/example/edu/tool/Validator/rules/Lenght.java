package com.example.edu.tool.Validator.rules;

import java.lang.Integer;
import org.springframework.util.MultiValueMap;
import com.example.edu.tool.Validator.Rule;
import com.example.edu.tool.Validator.Exceptions.InvalidParameterException;

/*
 * Rule to ensure that a field value has a specific length
 */
public class Lenght extends Rule {

    // Constructor to initialize parameters for the rule
    public Lenght(String params) {
        super(params);
    }

    // Method to check if the field value matches the required length
    public boolean check(String name, MultiValueMap<String, String> body) throws InvalidParameterException {
        int size = 0;
        try {
            size = Integer.valueOf(this.params).intValue();
        } catch (NumberFormatException e) {
            throw new InvalidParameterException("'size' rule can only use parameter of type Int, not '" + this.params + "'.");
        }

        for (String v : body.get(name)) {
            if ((v == null && size != 0) || (v != null && v.length() != size)) {
                return false;
            }
        }
        return true;
    }

    // Method to return an error message if validation fails
    public String getErrorMessage(String name) {
        return "Le champ '" + name + "' doit être égal à " + this.params + " caractères.";
    }
}
