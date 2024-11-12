package com.example.edu.tool.Validator.rules;

import java.util.List;

import org.springframework.util.MultiValueMap;

import com.example.edu.tool.Validator.Rule;

public class Confirm extends Rule{

    public Confirm(String params){
        super(params);
    }

    public boolean check(String name, MultiValueMap<String, String> body){
        List<String> confirmEl = body.get("confirm_"+name);

        if (confirmEl == null || body.get(name).size() != confirmEl.size()) return false;
        
        for(int i=0; i<body.get(name).size(); i++){
            if(!confirmEl.get(i).equals( body.get(name).get(i))){
                return false;
            }
        }

        return true;
    }

    public String getErrorMessage(String name){
        return "Le champ '"+name+"' doit être confirmé.";
    }
}
