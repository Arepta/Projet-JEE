package com.example.edu.tool.Validator.rules;

import java.lang.Double;
import java.text.SimpleDateFormat;

import org.springframework.util.MultiValueMap;

import com.example.edu.tool.Validator.Rule;
import com.example.edu.tool.Validator.Exceptions.InvalidParameterException;

public class Min extends Rule{

    private final SimpleDateFormat formatterDate =  new SimpleDateFormat("yyyy-MM-dd");
    private final SimpleDateFormat formatterDatetime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss") ;

    public Min(String params){
        super(params);
    }

    public boolean check(String name, MultiValueMap<String, String> body) throws InvalidParameterException{
        double min = 0.0;
        double vParsed = 0.0;

        try{

            if(this.params.matches("^\\d{4}-\\d{2}-\\d{2}$")){
                min = (double)formatterDate.parse(this.params).getTime();
            }
            else if(this.params.matches("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$")){
                min = (double)formatterDatetime.parse(this.params).getTime();
            }
            else{
                min = Double.valueOf(this.params).doubleValue();
            }   
        }
        catch(Exception e){
            throw new InvalidParameterException("'min' rule can only use paramater of type Int, Double, Date or Datetime not '"+this.params+"'.");
        }
        
        for (String v : body.get(name)) {
            try{
                if(v.matches("^\\d{4}-\\d{2}-\\d{2}$")){
                    vParsed = (double)formatterDate.parse(v).getTime();
                }
                else if(v.matches("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$")){
                    vParsed = (double)formatterDatetime.parse(v).getTime();
                }
                else{
                    vParsed =  Double.valueOf(v).doubleValue();
                }   

                if( vParsed < min){
                    return false;
                }
            }
            catch(Exception e){
                if(v == null || v.length() < min){
                    return false;
                }
            }
        }
        return true;
    }

    public String getErrorMessage(String name){
        try{
            if(!this.params.matches("^\\d{4}-\\d{2}-\\d{2}$") && !this.params.matches("^\\d{4}-\\d{2}-\\d{2}T\\d{2}:\\d{2}:\\d{2}$")){
                Double.valueOf(this.params);
            }
            return "Le champ '"+name+"' doit être supérieur ou égal à "+this.params+".";
        }
        catch(NumberFormatException e){
            return "Le champ '"+name+"' doit être supérieur ou égal à "+this.params+" caractères.";
        }
    }
}
