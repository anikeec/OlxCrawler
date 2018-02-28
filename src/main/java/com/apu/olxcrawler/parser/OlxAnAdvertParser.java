/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.parser;

import com.apu.olxcrawler.OlxVariables;
import com.apu.olxcrawler.query.OlxRequest;
import com.apu.olxcrawler.entity.AnAdvert;
import static com.apu.olxcrawler.parser.OlxParserUtils.getPatternCutOut;
import com.apu.olxcrawler.query.OlxResult;
import com.apu.olxcrawler.utils.Log;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author apu
 */
public class OlxAnAdvertParser {
    
    private static final Log log = Log.getInstance();
    private final Class classname = OlxAnAdvertParser.class;    
        
    private final String OLX_PHONE_URL = "ajax/misc/contact/phone/";
    
    public AnAdvert getAnAdvertFromLink(String link) {
        AnAdvert advert = new AnAdvert();
        String content;
        
        OlxRequest request = new OlxRequest();
        OlxResult result = request.makeRequest(link);
        content = result.getContent();
        
        advert.setAuthor(getAuthorFromContent(content));
        advert.setDescription(getDescriptionFromContent(content));
        advert.setHeader(getHeaderFromContent(content));
        advert.setId(getIdFromContent(content));
        advert.setLink(link);        
        advert.setPrice(getPriceFromContent(content));
        advert.setPublicationDate(getPublicationDateFromContent(content));
        advert.setRegion(getRegionFromContent(content));
        advert.setPhone(getPhoneFromUrlAndResult(link, result));
        advert.setUserOffers(getUserOffersFromContent(content));
        advert.setUserSince(getUserSinceFromContent(content));
        
        return advert;
    }
    
    private String getPhoneFromUrlAndResult(String urlStr, OlxResult result) {      
        String idStr = this.getIDfromUrl(urlStr);
        if(idStr == null)   
                return null;
        
        String token = getTokenFromContent(result.getContent());
        String phoneUrlStr = OlxVariables.OLX_HOST_URL + 
                                    OLX_PHONE_URL + idStr + "/?pt=" + token;

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
        
        startPattern = "Добавлено:";
        endPattern = "<small>";
        ret = getPatternCutOut(innerContent, startPattern, endPattern);
        if(ret != null) return ret.trim();
  
        return null;
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
    
    public static void main(String[] args) {
        String urlStr = "https://www.olx.ua/obyavlenie/learn-version-control-with-"
            + "git-raspredelennaya-sistema-upravleniya-vers-IDya9jS.html#5b61bf5b91";
        
        OlxAnAdvertParser parser = new OlxAnAdvertParser();

        AnAdvert anAdvert = parser.getAnAdvertFromLink(urlStr);
        System.out.println(anAdvert.getPhone());
    }
    
}
