/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.parser;

/**
 *
 * @author apu
 */
public class OlxParserUtils {
    
    public static String getPatternCutOut(String content, 
                                            String startPattern, 
                                            String endPattern) {
        
        int startPosition = content.indexOf(startPattern) + 
                                        startPattern.length();
        if(startPosition == -1)
                            return null;
        int endPosition = content.indexOf(endPattern, 
                                        startPosition + 1);
        if(endPosition == -1)
                            return null;
        return content.substring(startPosition, endPosition);
    }
    
}
