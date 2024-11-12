package com.example.edu.tool.Validator.rules;

import java.util.Arrays;
import java.util.List;

import org.springframework.util.MultiValueMap;

import com.example.edu.tool.Validator.Rule;
import com.example.edu.tool.Validator.Exceptions.InvalidParameterException;

public class Value extends Rule{

    public Value(String params){
        super(params);
    }

    public boolean check(String name, MultiValueMap<String, String> body) throws InvalidParameterException{
        List<String> values = Arrays.asList(this.params.split(","));
        
        for (String v : body.get(name)) {
            if(!values.contains(v)){
                return false;
            }
        }
        return true;
    }

    public String getErrorMessage(String name){
        return "Le champ '"+name+"' doit être dans la liste de valeurs suivante : "+this.params+".";
    }
}
