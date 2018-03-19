/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author apu
 */
public class DataChecker {
    
    public static void regularCheck(String input, String regExp, String exceptionMsg) {
        Pattern pattern = Pattern.compile(regExp, Pattern.DOTALL);        
        Matcher matcher = pattern.matcher(input);        
        if(matcher.matches() == false) 
            throw new IllegalArgumentException(exceptionMsg);
    }
    
    public static void nullCheck(String input, String exceptionMsg) {
        if(input == null)
            throw new IllegalArgumentException(exceptionMsg + " is NULL");
    }
    
    public static void nullCheck(Object input, String exceptionMsg) {
        if(input == null)
            throw new IllegalArgumentException(exceptionMsg + " is NULL");
    }
            
}
