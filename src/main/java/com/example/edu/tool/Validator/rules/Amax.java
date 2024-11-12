package com.example.edu.tool.Validator.rules;

import java.lang.Integer;

import org.springframework.util.MultiValueMap;

import com.example.edu.tool.Validator.Rule;
import com.example.edu.tool.Validator.Exceptions.InvalidParameterException;

public class Amax extends Rule{

    public Amax(String params){
        super(params);
    }

    public boolean check(String name, MultiValueMap<String, String> body) throws InvalidParameterException{
        int max = 0;
        try{
            max = Integer.valueOf(this.params).intValue();
        }
        catch(NumberFormatException e){
            throw new InvalidParameterException("'amax' rule can only use paramater of type Int, not '"+this.params+"'.");
        }
        
        return body.get(name).size() <= max;
    }

    public String getErrorMessage(String name){
        return "Le champ '"+name+"' doit contenir au maximum "+this.params+" choix.";
    }
}
