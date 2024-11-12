package com.example.edu.tool.Validator.rules;

import java.lang.Integer;

import org.springframework.util.MultiValueMap;

import com.example.edu.tool.Validator.Rule;
import com.example.edu.tool.Validator.Exceptions.InvalidParameterException;

public class Lenght extends Rule{

    public Lenght(String params){
        super(params);
    }

    public boolean check(String name, MultiValueMap<String, String> body) throws InvalidParameterException{
        int size = 0;
        try{
            size = Integer.valueOf(this.params).intValue();
        }
        catch(NumberFormatException e){
            throw new InvalidParameterException("'size' rule can only use paramater of type Int, not '"+this.params+"'.");
        }
        
        for (String v : body.get(name)) {

            if( v.length() != size){
                return false;
            }

        }
        return true;
    }

    public String getErrorMessage(String name){
        return "Le champ '"+name+"' doit être égal à "+this.params+" caractères.";
    }
}
