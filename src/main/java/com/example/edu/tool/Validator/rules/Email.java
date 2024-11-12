package com.example.edu.tool.Validator.rules;

import org.springframework.util.MultiValueMap;

import com.example.edu.tool.Validator.Rule;

public class Email extends Rule{

    public Email(String params){
        super(params);
    }

    public boolean check(String name, MultiValueMap<String, String> body){
        for (String v : body.get(name)) {
            if( !v.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$") ){
                return false;
            }
        }
        return true;
    }

    public String getErrorMessage(String name){
        return "Le champ '"+name+"' doit être une adresse mail.";
    }
}
