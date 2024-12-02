package com.example.edu.tool.Validator.rules;

import java.lang.Double;
import java.text.SimpleDateFormat;
import org.springframework.util.MultiValueMap;
import com.example.edu.tool.Validator.Rule;
import com.example.edu.tool.Validator.Exceptions.InvalidParameterException;

/*
 * Rule to ensure that a field value is less than or equal to a maximum value
 */
public class Max extends Rule {

    // Date formatters for date and datetime parsing
    private final SimpleDateFormat formatterDate = new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat formatterDatetime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");

    // Constructor to initialize parameters for the rule
    public Max(String params) {
        super(params);
    }

    // Method to check if the field value is less than or equal to the maximum allowed
    public boolean check(String name, MultiValueMap<String, String> body) throws InvalidParameterException {
        double max = 0.0;
        double vParsed = 0.0;

        try {
            if (this.params.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                max = (double) formatterDate.parse(this.params).getTime();
            } else if (this.params.matches("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$")) {
                max = (double) formatterDatetime.parse(this.params).getTime();
            } else {
                max = Double.valueOf(this.params).doubleValue();
            }
        } catch (Exception e) {
            throw new InvalidParameterException("'max' rule can only use parameter of type Int, Double, Date or Datetime not '" + this.params + "'.");
        }

        for (String v : body.get(name)) {
            try {
                if (v.matches("^\\d{4}-\\d{2}-\\d{2}$")) {
                    vParsed = (double) formatterDate.parse(v).getTime();
                } else if (v.matches("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$")) {
                    vParsed = (double) formatterDatetime.parse(v).getTime();
                } else {
                    vParsed = Double.valueOf(v).doubleValue();
                }

                if (vParsed > max) {
                    return false;
                }
            } catch (Exception e) {
                if (v != null && v.length() > max) {
                    return false;
                }
            }
        }
        return true;
    }

    // Method to return an error message if validation fails
    public String getErrorMessage(String name) {
        try {
            if (!this.params.matches("^\\d{4}-\\d{2}-\\d{2}$") && !this.params.matches("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$")) {
                Double.valueOf(this.params);
            }
            return "Le champ '" + name + "' doit être inférieur ou égal à " + this.params + ".";
        } catch (NumberFormatException e) {
            return "Le champ '" + name + "' doit être inférieur ou égal à " + this.params + " caractères.";
        }
    }
}
