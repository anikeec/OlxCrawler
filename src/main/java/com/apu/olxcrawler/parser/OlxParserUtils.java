/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.parser;

/**
 *
 * @author Anikeichyk Pavel
 * @email  pasha_anik@ukr.net
 * 
 */
public class OlxParserUtils {
    
    public static String getPatternCutOut(String content, 
                                            String startPattern, 
                                            String endPattern) {
        if(content == null)
                            return null;
        int startPosition = content.indexOf(startPattern) ;
        if(startPosition == -1)
                            return null;
        int endPosition = content.indexOf(endPattern, 
                                        startPosition + 
                                        startPattern.length() + 1);
        if(endPosition == -1)
                            return null;
        return content.substring(startPosition + startPattern.length(), endPosition);
    }
    
}
