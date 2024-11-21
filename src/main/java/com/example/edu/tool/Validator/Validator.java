package com.example.edu.tool.Validator;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.util.MultiValueMap;

import com.example.edu.tool.Validator.Exceptions.unknownRuleException;


/*
 * Use to Validate request field by using rules
 */
public class Validator {
    private final static String PATH_TO_RULES = "com.example.edu.tool.Validator.rules.";

    private Map<String, String> fieldRules;

    private MultiValueMap<String, String> request;

    private List<String> validatedField;
    private Map<String, String> failedField;
    private List<String> remainingField;

    
    public Validator(Map<String, String> rules){
        this.fieldRules = new HashMap<>(rules);

        for(String field : this.fieldRules.keySet()) {
            if(!this.fieldRules.get(field).contains("array") && !this.fieldRules.get(field).contains("size")){
                try{
                    this.fieldRules.computeIfPresent(field, (k,v) -> "single|"+this.fieldRules.get(field));
                }
                catch(Exception e){
                    System.out.println(e);
                }

            }
        
        }

        this.validatedField = new ArrayList<>();
        this.failedField = new HashMap<>();
        this.remainingField = new ArrayList<>();
    }

    public boolean validateRequest(MultiValueMap<String, String> requestContent) throws unknownRuleException{

        this.request = requestContent;
        this.validatedField.clear();
        this.failedField.clear();
        this.remainingField.clear();

        boolean isRequestValidated = true;
        boolean isFieldValidated = true;

        for(String field : this.fieldRules.keySet()){
            if(requestContent.get(field) == null){;
                requestContent.put(field, new ArrayList<>());
            }
        }

        for (String field : requestContent.keySet()) {

            isFieldValidated = true;
            
            if(this.fieldRules.get(field) == null){
                this.remainingField.add(field);
                continue;
            }

            List<String> rules = Arrays.asList(this.fieldRules.get(field).split("\\|"));
            for (String instruction : rules) {

                String ruleName = "";
                String arguments = "";

                List<String> parsedInstruction = Arrays.asList(instruction.split("="));
                ruleName = parsedInstruction.get(0).toLowerCase();
                ruleName = ruleName.replaceFirst(""+ruleName.charAt(0), (""+ruleName.charAt(0)).toUpperCase());
                if(parsedInstruction.size() > 1){
                    arguments = parsedInstruction.get(1);
                }

                try{
                    Class<?> cls = Class.forName(PATH_TO_RULES+ruleName); //get the class if it existe

                    Constructor<?> ctor = cls.getConstructor(String.class);

                    Rule ruleObjectExtend = (Rule)ctor.newInstance(new Object[] { arguments });


                    if( !ruleObjectExtend.check(field, requestContent) ){
                        isFieldValidated = false;
                        isRequestValidated = false;
                        this.failedField.put(field, ruleObjectExtend.getErrorMessage(field));
                        break;
                    }
                }
                catch(Exception e){
                    throw new unknownRuleException(e.toString());
                }
            }
            
            if(isFieldValidated){
                this.validatedField.add(field);
            }
        }

        return isRequestValidated;
    };

    public List<String> getValidatedField(){
        return this.validatedField;
    }

    public Map<String, String> getValidatedValue(){
        Map<String, String> VV = new HashMap<>();
        
        for (String field : this.validatedField) {
            VV.put(field, this.request.getFirst(field));
        }

        return VV;
    }

    public Map<String, String> getErrors(){
        return this.failedField;
    }

    public List<String> getErrorsMessages(){
        return  new ArrayList<String>(this.failedField.values());
    }

    public List<String> getRemainingField(){
        return this.remainingField;
    }
}
