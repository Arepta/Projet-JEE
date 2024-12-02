package com.example.edu.tool.Validator.rules;

import org.springframework.util.MultiValueMap;
import com.example.edu.tool.Validator.Rule;

/*
 * Rule to validate if a field is an array (always returns true)
 */
public class Array extends Rule {

    // Constructor to initialize parameters for the rule
    public Array(String params) {
        super(params);
    }

    // Method to always return true (placeholder rule for array type)
    public boolean check(String name, MultiValueMap<String, String> body) {
        return true;
    }

    // Method to return an error message if validation fails
    public String getErrorMessage(String name) {
        return "Si tu vois ce message s'afficher c'est que un cas d'erreur est mal penser. Cette regle ne peut pas etre fausse.";
    }
}
