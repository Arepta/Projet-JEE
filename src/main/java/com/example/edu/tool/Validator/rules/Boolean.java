package com.example.edu.tool.Validator.rules;

import org.springframework.util.MultiValueMap;

import com.example.edu.tool.Validator.Rule;

public class Boolean extends Rule{

    public Boolean(String params){
        super(params);
    }

    public boolean check(String name, MultiValueMap<String, String> body){
        for (String v : body.get(name)) {
            if( !v.matches("true|false|1|0") ){
                return false;
            }
        }
        return true;
    }

    public String getErrorMessage(String name){
        return "Le champ '"+name+"' doit être vrai ou faux.";
    }
}
