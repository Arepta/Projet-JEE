package com.example.edu.tool.Validator.rules;

import org.springframework.util.MultiValueMap;

import com.example.edu.tool.Validator.Rule;

public class Single extends Rule{

    public Single(String params){
        super(params);
    }

    public boolean check(String name, MultiValueMap<String, String> body){
        return body.get(name).size() <= 1;
    }

    public String getErrorMessage(String name){
        return "Le champ '"+name+"' ne peut contenir qu'une seule proposition.";
    }
}
