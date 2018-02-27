/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler;

import com.apu.olxcrawler.query.OlxRequest;
import com.apu.olxcrawler.entity.AnAdwert;
import com.apu.olxcrawler.query.OlxResult;
import com.apu.olxcrawler.utils.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author apu
 */
public class OlxParser {
    
    private static final Log log = Log.getInstance();
    private final Class classname = OlxParser.class;    
        
    private final String OLX_PHONE_URL = "ajax/misc/contact/phone/";
    
    public AnAdwert getAnAdwertFromLink(String link) {
        AnAdwert adwert = new AnAdwert();
        String content = null;
        
        OlxRequest request = new OlxRequest();
        OlxResult result = request.makeRequest(link);
        content = result.getContent();
        
        adwert.setAuthor(getAuthorFromContent(content));
        adwert.setDescription(getDescriptionFromContent(content));
        adwert.setHeader(getHeaderFromContent(content));
        adwert.setId(getIdFromContent(content));
        adwert.setLink(link);        
        adwert.setPrice(getPriceFromContent(content));
        adwert.setPublicationDate(getPublicationDateFromContent(content));
        adwert.setRegion(getRegionFromContent(content));
        adwert.setPhone(getPhoneFromUrlAndResult(link, result));
        
        return adwert;
    }
    
    public List<String> parseSearchResult(String content) {
        List<String> list = new ArrayList<>();
        String regExpUrl = "https://www.olx.ua/obyavlenie/(.+)\" class=\"marginright5 link linkWithHash detailsLink\"";
        Pattern pattern = Pattern.compile(regExpUrl, Pattern.CASE_INSENSITIVE | Pattern.MULTILINE | Pattern.DOTALL);        
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
    
    String getPhoneFromUrlAndResult(String urlStr, OlxResult result) {      
        String idStr = this.getIDfromUrl(urlStr);
        if(idStr == null)   
                return null;
        
        String token = getTokenFromContent(result.getContent());
        String phoneUrlStr = OlxVariables.OLX_HOST_URL + 
                                    OLX_PHONE_URL + idStr + "/?pt=" + token;
        log.debug(classname, phoneUrlStr);
        OlxRequest request = new OlxRequest();
        OlxResult phoneRequestResult = 
                request.makeRequest(phoneUrlStr, urlStr, result.getCookieStore());

        return phoneRequestResult.getContent();
    }
    
    private String getTokenFromContent(String content) {
        String regExpUrl = "(.*)var phoneToken = \\'(.*)\\';(.*)";    
        Pattern pattern = Pattern.compile(regExpUrl, Pattern.DOTALL);  
        
        Matcher matcher = pattern.matcher(content);        
        if(matcher.matches() == false) return null;
        
        String tokenStartPattern = "var phoneToken = '";
        String tokenEndPattern = "';";
        int tokenStartPosition = content.indexOf(tokenStartPattern) + 
                                        tokenStartPattern.length();
        int tokenEndPosition = content.indexOf(tokenEndPattern, 
                                        tokenStartPosition + 1);

        return content.substring(tokenStartPosition, tokenEndPosition);        
    }
    
    private String getIDfromUrl(String url) {
        String regExpUrl = ".+\\-ID(.*).html(.*)";        
        Pattern pattern = Pattern.compile(regExpUrl, Pattern.DOTALL);        
        Matcher matcher = pattern.matcher(url);        
        if(matcher.matches() == false) return null;
        
        String startPattern = "-ID";
        String endPattern = ".html";
        int startPosition = url.indexOf(startPattern) + startPattern.length();
        int endPosition = url.indexOf(endPattern, startPosition + 1);
        return url.substring(startPosition, endPosition);
    }
    
    private String getAuthorFromContent(String content) {
        return null;
    }
    
    private String getDescriptionFromContent(String content) {
        return null;
    }
    
    private String getHeaderFromContent(String content) {
        return null;
    }
    
    private String getIdFromContent(String content) {
        return null;
    }
    
    private String getPriceFromContent(String content) {
        return null;
    }
    
    private String getPublicationDateFromContent(String content) {
        return null;
    }
    
    private String getRegionFromContent(String content) {
        return null;
    }
    
    public static void main(String[] args) {
        String urlStr = "https://www.olx.ua/obyavlenie/learn-version-control-with-"
            + "git-raspredelennaya-sistema-upravleniya-vers-IDya9jS.html#5b61bf5b91";
        
        OlxParser parser = new OlxParser();

        AnAdwert anAdwert = parser.getAnAdwertFromLink(urlStr);
        System.out.println(anAdwert.getPhone());
    }
    
}
