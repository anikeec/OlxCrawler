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
        adwert.setUserOffers(getUserOffersFromContent(content));
        adwert.setUserSince(getUserSinceFromContent(content));
        
        return adwert;
    }
    
    public List<String> parseSearchResultOnePage(String content) {
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
//        log.debug(classname, phoneUrlStr);
        OlxRequest request = new OlxRequest();
        OlxResult phoneRequestResult = 
                request.makeRequest(phoneUrlStr, urlStr, result.getCookies());
        String phoneStr = phoneRequestResult.getContent();

        String startPattern = "{\"value\":\"";
        String endPattern = "\"}";
        return getPatternCutOut(phoneStr, startPattern, endPattern);
    }
    
    private String getTokenFromContent(String content) {
        String regExpUrl = "(.*)var phoneToken = \\'(.*)\\';(.*)";    
        Pattern pattern = Pattern.compile(regExpUrl, Pattern.DOTALL);  
        
        Matcher matcher = pattern.matcher(content);        
        if(matcher.matches() == false) return null;
        
        String startPattern = "var phoneToken = '";
        String endPattern = "';";
        return getPatternCutOut(content, startPattern, endPattern);        
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
        String innerContent = getUserDetailsBlockFromContent(content);
        String startPattern = "\">";
        String endPattern = "</a>";
        String ret = getPatternCutOut(innerContent, startPattern, endPattern);
        if(ret != null) return ret.trim();
        else            return ret;
    }   
    
    private String getDescriptionFromContent(String content) {
        String innerContent = getOfferDescriptionContentBlockFromContent(content);
        String startPattern = "<p class=\"pding10 lheight20 large\">";
        String endPattern = "</p>";
        String ret = getPatternCutOut(innerContent, startPattern, endPattern);
        if(ret != null) return ret.trim();
        else            return ret;
    }
    
    private String getHeaderFromContent(String content) {
        String innerContent = getOfferTitleboxBlockFromContent(content);
        String startPattern = "<h1>";
        String endPattern = "</h1>";
        String ret = getPatternCutOut(innerContent, startPattern, endPattern);
        if(ret != null) return ret.trim();
        else            return ret;
    }
    
    private String getIdFromContent(String content) {
        String innerContent = getOfferTitleboxDetailsBlockFromContent(content);
        String startPattern = "<small>Номер объявления:";
        String endPattern = "</small>";
        String ret = getPatternCutOut(innerContent, startPattern, endPattern);
        if(ret != null) return ret.trim();
        else            return ret;
    }
    
    private String getPriceFromContent(String content) {
        String innerContent = getPriceLabelBlockFromContent(content);
        String startPattern = "<strong class=\"xxxx-large not-arranged\">";
        String endPattern = "</strong>";
        String ret = getPatternCutOut(innerContent, startPattern, endPattern);
        if(ret != null) return ret.trim();
        else            return ret;
    }
    
    private String getPublicationDateFromContent(String content) {
        String innerContent = getOfferTitleboxDetailsBlockFromContent(content);
        String startPattern = "Опубликовано с мобильного</a>";
        String endPattern = "<small>";
        String ret = getPatternCutOut(innerContent, startPattern, endPattern);
        if(ret != null) return ret.trim();
        else            return ret;
    }
    
    private String getRegionFromContent(String content) {
        String innerContent = getOfferTitleboxDetailsBlockFromContent(content);
        String startPattern = "<strong>";
        String endPattern = "</strong>";
        String ret = getPatternCutOut(innerContent, startPattern, endPattern);
        if(ret != null) return ret.trim();
        else            return ret;
    }
    
    private String getUserOffersFromContent(String content) {
        String innerContent = getUserDetailsBlockFromContent(content);
        String startPattern = "<a href=\"";
        String endPattern = "\">";
        return getPatternCutOut(innerContent, startPattern, endPattern);
    }
    
    private String getUserSinceFromContent(String content) {
        String innerContent = getUserDetailsBlockFromContent(content);
        String startPattern = "<span class=\"user-since\">на OLX с";
        String endPattern = "</span>";
        return getPatternCutOut(innerContent, startPattern, endPattern);
    }
    
    private String getUserDetailsBlockFromContent(String content) {
        String startPattern = "<div class=\"offer-user__details\">";
        String endPattern = "</div>";
        return getPatternCutOut(content, startPattern, endPattern);
    }  
    
    private String getPriceLabelBlockFromContent(String content) {
        String startPattern = "<div class=\"price-label\">";
        String endPattern = "</div>";
        return getPatternCutOut(content, startPattern, endPattern);
    }
    
    private String getOfferDescriptionContentBlockFromContent(String content) {
        String startPattern = "<div class=\"clr descriptioncontent marginbott20\">";
        String endPattern = "<div id=\"offerbottombar\" class=\"pding15\">";
        return getPatternCutOut(content, startPattern, endPattern);
    }
    
    private String getOfferTitleboxBlockFromContent(String content) {
        String startPattern = "<div class=\"offer-titlebox\">";
        String endPattern = "<div class=\"offer-titlebox__details\">";
        return getPatternCutOut(content, startPattern, endPattern);
    }
    
    private String getOfferTitleboxDetailsBlockFromContent(String content) {
        String startPattern = "<div class=\"offer-titlebox__details\">";
        String endPattern = "</div>";
        return getPatternCutOut(content, startPattern, endPattern);
    }
    
    public Integer getAmountOfPagesFromContent(String content) {
        String innerContent = getPagesBlockFromContent(content);
        
        Pointer ptr = new Pointer();
        while(true) {
            String tempContent = getNextPageIndex(innerContent, ptr);
            if(tempContent == null) {              
                break;
            } 
            innerContent = tempContent;
        }
        
        String startPattern = "<span>";
        String endPattern = "</span>";
        String amount = getPatternCutOut(innerContent, startPattern, endPattern);
        if(amount != null) {
            amount = amount.trim();
            return Integer.parseInt(amount);
        }
        return null;
    }
    
    private String getNextPageIndex(String content, Pointer ptr) {
        String startPattern = "<span class=\"item fleft\">";
        int startPosition = content.indexOf(startPattern);
        if(startPosition == -1)
                            return null;
//        ptr.set(startPosition);
        return content.substring(startPosition + startPattern.length());
    }
    
    private String getPagesBlockFromContent(String content) {
        String startPattern = "<div class=\"pager rel clr\">";
        String endPattern = "</div>";
        return getPatternCutOut(content, startPattern, endPattern);
    }
    
    private String getPatternCutOut(String content, String startPattern, String endPattern) {
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
    
    public static void main(String[] args) {
        String urlStr = "https://www.olx.ua/obyavlenie/learn-version-control-with-"
            + "git-raspredelennaya-sistema-upravleniya-vers-IDya9jS.html#5b61bf5b91";
        
        OlxParser parser = new OlxParser();

        AnAdwert anAdwert = parser.getAnAdwertFromLink(urlStr);
        System.out.println(anAdwert.getPhone());
    }
    
}
