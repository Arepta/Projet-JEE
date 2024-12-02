package com.example.edu.tool.Validator;

import org.springframework.util.MultiValueMap;

import com.example.edu.tool.Validator.Exceptions.InvalidParameterException;

/*
 * Abstract class representing a validation rule
 */
public abstract class Rule {

    // Parameters required by the rule
    protected String params;

    // Constructor to initialize parameters for the rule
    public Rule(String params) {
        this.params = params;
    }

    // Abstract method to check if a given field meets the rule's criteria
    public abstract boolean check(String name, MultiValueMap<String, String> body) throws InvalidParameterException;

    // Abstract method to get the error message when validation fails
    public abstract String getErrorMessage(String name);
}
