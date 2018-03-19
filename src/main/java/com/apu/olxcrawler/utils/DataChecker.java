/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.utils;

import com.apu.olxcrawler.parser.IllegalInputValueException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author apu
 */
public class DataChecker {
    
    public static void regularCheck(String input, String regExp, String exceptionMsg) 
            throws IllegalInputValueException {
        Pattern pattern = Pattern.compile(regExp, Pattern.DOTALL);        
        Matcher matcher = pattern.matcher(input);        
        if(matcher.matches() == false) 
            throw new IllegalInputValueException(exceptionMsg);
    }
    
    public static boolean regularCheck(String input, String regExp) {
        Pattern pattern = Pattern.compile(regExp, Pattern.DOTALL);        
        Matcher matcher = pattern.matcher(input);        
        return matcher.matches();
    }
    
    public static void nullCheck(String input, String exceptionMsg) 
            throws IllegalInputValueException {
        if(input == null)
            throw new IllegalInputValueException(exceptionMsg + " is NULL");
    }
    
    public static void nullCheck(Object input, String exceptionMsg) 
            throws IllegalInputValueException {
        if(input == null)
            throw new IllegalInputValueException(exceptionMsg + " is NULL");
    }
            
}
