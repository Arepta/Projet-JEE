package com.example.edu.tool.Validator.rules;

import org.springframework.util.MultiValueMap;

import com.example.edu.tool.Validator.Rule;
import com.example.edu.tool.Validator.Exceptions.InvalidParameterException;

public class Default extends Rule{

    public Default(String params){
        super(params);
    }

    public boolean check(String name, MultiValueMap<String, String> body) throws InvalidParameterException{
        if(body.get(name).size() == 0){
            if(params.equals("NULL")){
                body.get(name).add(null);
            }
            else{
                body.get(name).add(params);
            }
        }
        return true;
    }

    public String getErrorMessage(String name){
        return "Si tu vois ce message s'afficher c'est que un cas d'erreur est mal penser. Cette regle ne peut pas etre fausse.";
    }
}
