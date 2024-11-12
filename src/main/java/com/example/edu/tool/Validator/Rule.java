package com.example.edu.tool.Validator;

import org.springframework.util.MultiValueMap;

import com.example.edu.tool.Validator.Exceptions.InvalidParameterException;

public abstract class Rule {

    protected String params;

    public Rule(String params){
        this.params = params;
    }

    public abstract boolean check(String name, MultiValueMap<String, String> body) throws InvalidParameterException;

    public abstract String getErrorMessage(String name);
}
