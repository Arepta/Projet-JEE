package com.example.edu.tool.Validator;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.MultiValueMap;

import com.example.edu.tool.Validator.Exceptions.unknownRuleException;

/*
 * Class used to validate request fields using specified rules
 */
public class Validator {
    // Path to the directory containing validation rules
    private final static String PATH_TO_RULES = "com.example.edu.tool.Validator.rules.";

    // Map containing fields and their corresponding validation rules
    private Map<String, String> fieldRules;

    // Holds the request content to validate
    private MultiValueMap<String, String> request;

    // Lists to track validated, failed, and remaining fields
    private List<String> validatedField;
    private Map<String, String> failedField;
    private List<String> remainingField;

    // Constructor initializes the field rules and sets default validation types
    public Validator(Map<String, String> rules) {
        this.fieldRules = new HashMap<>(rules);

        for (String field : this.fieldRules.keySet()) {
            if (!this.fieldRules.get(field).contains("array") && !this.fieldRules.get(field).contains("size")) {
                try {
                    this.fieldRules.computeIfPresent(field, (k, v) -> "single|" + this.fieldRules.get(field));
                } catch (Exception e) {
                    System.out.println(e);
                }
            }
        }

        this.validatedField = new ArrayList<>();
        this.failedField = new HashMap<>();
        this.remainingField = new ArrayList<>();
    }

    // Method to validate the request content
    public boolean validateRequest(MultiValueMap<String, String> requestContent) throws unknownRuleException {
        this.request = requestContent;
        this.validatedField.clear();
        this.failedField.clear();
        this.remainingField.clear();

        boolean isRequestValidated = true;

        // Initialize missing fields
        for (String field : this.fieldRules.keySet()) {
            if (requestContent.get(field) == null) {
                requestContent.put(field, new ArrayList<>());
            }
        }

        // Loop through each field to validate
        for (String field : requestContent.keySet()) {
            boolean isFieldValidated = true;

            // Skip fields with no rules defined
            if (this.fieldRules.get(field) == null) {
                this.remainingField.add(field);
                continue;
            }

            List<String> rules = Arrays.asList(this.fieldRules.get(field).split("\\|"));
            for (String instruction : rules) {
                String ruleName;
                String arguments = "";

                // Parse rule name and arguments
                List<String> parsedInstruction = Arrays.asList(instruction.split("="));
                ruleName = parsedInstruction.get(0).toLowerCase();
                ruleName = ruleName.replaceFirst("" + ruleName.charAt(0), ("" + ruleName.charAt(0)).toUpperCase());
                if (parsedInstruction.size() > 1) {
                    arguments = parsedInstruction.get(1);
                }

                try {
                    // Dynamically load the rule class and instantiate it
                    Class<?> cls = Class.forName(PATH_TO_RULES + ruleName);
                    Constructor<?> ctor = cls.getConstructor(String.class);
                    Rule ruleObjectExtend = (Rule) ctor.newInstance(arguments);

                    // Check if the field passes the rule
                    if (!ruleObjectExtend.check(field, requestContent)) {
                        isFieldValidated = false;
                        isRequestValidated = false;
                        this.failedField.put(field, ruleObjectExtend.getErrorMessage(field));
                        break;
                    }
                } catch (Exception e) {
                    throw new unknownRuleException(e.toString());
                }
            }

            if (isFieldValidated) {
                this.validatedField.add(field);
            }
        }

        return isRequestValidated;
    }

    // Method to get the list of validated fields
    public List<String> getValidatedField() {
        return this.validatedField;
    }

    // Method to get the validated values of fields
    public Map<String, String> getValidatedValue() {
        Map<String, String> validatedValues = new HashMap<>();
        for (String field : this.validatedField) {
            validatedValues.put(field, this.request.getFirst(field));
        }
        return validatedValues;
    }

    // Method to get errors for fields that failed validation
    public Map<String, String> getErrors() {
        return this.failedField;
    }

    // Method to get error messages for failed fields
    public List<String> getErrorsMessages() {
        return new ArrayList<>(this.failedField.values());
    }

    // Method to get the remaining fields that were not validated
    public List<String> getRemainingField() {
        return this.remainingField;
    }
}
