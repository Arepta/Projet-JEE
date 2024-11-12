package com.example.edu.tool.Validator.rules;

import org.springframework.util.MultiValueMap;

import com.example.edu.tool.Validator.Rule;

public class Double extends Rule{

    public Double(String params){
        super(params);
    }

    public boolean check(String name, MultiValueMap<String, String> body){
        for (String v : body.get(name)) {
            if( !v.matches("[0-9]{1,13}(\\.[0-9]*)?") ){
                return false;
            }
        }
        return true;
    }

    public String getErrorMessage(String name){
        return "Le champ '"+name+"' doit être une valeur décimale.";
    }
}
