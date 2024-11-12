package com.example.edu.tool.Validator.rules;

import java.lang.Integer;

import org.springframework.util.MultiValueMap;

import com.example.edu.tool.Validator.Rule;
import com.example.edu.tool.Validator.Exceptions.InvalidParameterException;

public class Amin extends Rule{

    public Amin(String params){
        super(params);
    }

    public boolean check(String name, MultiValueMap<String, String> body) throws InvalidParameterException{
        int min = 0;
        try{
            min = Integer.valueOf(this.params).intValue();
        }
        catch(NumberFormatException e){
            throw new InvalidParameterException("'amin' rule can only use paramater of type Int, not '"+this.params+"'.");
        }
        
        return body.get(name).size() >= min;
    }

    public String getErrorMessage(String name){
        return "Le champ '"+name+"' doit contenir au minimum "+this.params+" choix.";
    }
}
