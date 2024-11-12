package com.example.edu.tool.Validator.rules;

import org.springframework.util.MultiValueMap;

import com.example.edu.tool.Validator.Rule;

public class Datetime extends Rule {

    public Datetime(String params){
        super(params);
    }

    public boolean check(String name, MultiValueMap<String, String> body){
        for (String v : body.get(name)) {
            if( !v.matches("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$") ){
                return false;
            }
        }
        return true;
    }

    public String getErrorMessage(String name){
        return "Le champ '"+name+"' doit être une date au format yyyy-MM-ddThh:mm:ss.";
    }
}
