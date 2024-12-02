package com.example.edu.tool.Validator.rules;

import java.util.List;
import org.springframework.util.MultiValueMap;
import com.example.edu.tool.Validator.Rule;

/*
 * Rule to validate if a field matches its confirmation counterpart
 */
public class Confirm extends Rule {

    // Constructor to initialize parameters for the rule
    public Confirm(String params) {
        super(params);
    }

    // Method to check if the field matches its corresponding confirm field
    public boolean check(String name, MultiValueMap<String, String> body) {
        List<String> confirmEl = body.get("confirm_" + name);

        if (confirmEl == null || body.get(name).size() != confirmEl.size()) return false;

        for (int i = 0; i < body.get(name).size(); i++) {
            if (!confirmEl.get(i).equals(body.get(name).get(i))) {
                return false;
            }
        }

        return true;
    }

    // Method to return an error message if validation fails
    public String getErrorMessage(String name) {
        return "Le champ '" + name + "' doit être confirmé.";
    }
}
