/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.parser;

import com.apu.olxcrawler.utils.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Anikeichyk Pavel
 * @email  pasha_anik@ukr.net
 * 
 */
public class OlxSearchParser {
    
    private static final Log log = Log.getInstance();
    private final Class classname = OlxSearchParser.class;
    
    public List<String> parseSearchResultOnePage(String content) {
        List<String> list = new ArrayList<>();
        if(content == null)  return list;
        String regExpUrl = "https://www.olx.ua/obyavlenie/(.+)\" class=\"marginright5 link linkWithHash detailsLink\"";
        Pattern pattern = Pattern.compile(regExpUrl, 
                Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);        
        Matcher matcher = pattern.matcher(content); 
        Integer redultInt = matcher.groupCount();
        if(matcher.find() != false) {
            Integer startPos = matcher.start();
            Integer endPos = matcher.end();
            String contentTemp = content.substring(startPos, endPos);
            Pointer position = new Pointer();
            String lastLink = "";
            while(true) {
                String link = getLinkFromSearchResultContent(contentTemp, position);
                if(link == null)
                        break;
                if(link.equals(lastLink)) 
                        continue;
                lastLink = link;
                list.add(link);
            }            
        }      
        return list;
    }
    
    private String getLinkFromSearchResultContent(String content, Pointer startPosition) {        
        String linkStartPattern = "https://www.olx.ua/obyavlenie/";
        String linkEndPattern = "\"";
        int linkStartPosition = content.indexOf(linkStartPattern, 
                                        startPosition.get());
        if(linkStartPosition == -1)
                            return null;
        int linkEndPosition = content.indexOf(linkEndPattern, 
                                        linkStartPosition + 1);
        if(linkEndPosition == -1)
                            return null;        
        startPosition.set(linkEndPosition);        
        return content.substring(linkStartPosition, linkEndPosition);
    }
    
    public Integer getAmountOfPagesFromContent(String content) {
        if(content == null) return null;
        String innerContent = getPagesBlockFromContent(content);
        
        if(innerContent == null)    
                return null;
        
        while(true) {
            String tempContent = getNextPageIndex(innerContent);
            if(tempContent == null) {              
                break;
            } 
            innerContent = tempContent;
        }
        
        String startPattern = "<span>";
        String endPattern = "</span>";
        String amount = 
            OlxParserUtils.getPatternCutOut(innerContent, startPattern, endPattern);
        if(amount != null) {
            amount = amount.trim();
            return Integer.parseInt(amount);
        }
        return null;
    }
    
    private String getNextPageIndex(String content) {
        String startPattern = "<span class=\"item fleft\">";
        int startPosition = content.indexOf(startPattern);
        if(startPosition == -1)
                            return null;
        return content.substring(startPosition + startPattern.length());
    }
    
    private String getPagesBlockFromContent(String content) {
        String startPattern = "<div class=\"pager rel clr\">";
        String endPattern = "</div>";
        return OlxParserUtils.getPatternCutOut(content, startPattern, endPattern);
    }
    
}
