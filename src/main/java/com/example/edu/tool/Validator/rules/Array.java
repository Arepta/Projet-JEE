package com.example.edu.tool.Validator.rules;

import org.springframework.util.MultiValueMap;

import com.example.edu.tool.Validator.Rule;

public class Array extends Rule{

    public Array(String params){
        super(params);
    }

    public boolean check(String name, MultiValueMap<String, String> body){
        return true;
    }

    public String getErrorMessage(String name){
        return "Si tu vois ce message s'afficher c'est que un cas d'erreur est mal penser. Cette regle ne peut pas etre fausse.";
    }
}
