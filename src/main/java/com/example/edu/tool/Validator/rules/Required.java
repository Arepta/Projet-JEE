package com.example.edu.tool.Validator.rules;

import org.springframework.util.MultiValueMap;
import com.example.edu.tool.Validator.Rule;

public class Required extends Rule{

    public Required(String params){
        super(params);
    }

    public boolean check(String name, MultiValueMap<String, String> body){
        return !body.get(name).isEmpty();
    }

    public String getErrorMessage(String name){
        return "Le champ '"+name+"' doit être rempli.";
    }
}
