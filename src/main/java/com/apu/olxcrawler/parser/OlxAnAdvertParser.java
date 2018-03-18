/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.parser;

import com.apu.olxcrawler.OlxCategory;
import com.apu.olxcrawler.query.OlxRequest;
import com.apu.olxcrawler.entity.AnAdvert;
import com.apu.olxcrawler.entity.ExpandedLink;
import static com.apu.olxcrawler.parser.OlxParserUtils.getPatternCutOut;
import com.apu.olxcrawler.query.OlxResult;
import com.apu.olxcrawler.utils.Log;
import com.apu.olxcrawler.utils.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 *
 * @author Anikeichyk Pavel
 * @email  pasha_anik@ukr.net
 * 
 */
public class OlxAnAdvertParser {
    
    private static final Log log = Log.getInstance();
    private final Class classname = OlxAnAdvertParser.class;    
        
    private final String OLX_PHONE_URL = "ajax/misc/contact/phone/";
    private final String OLX_ADVERT_URL = "obyavlenie/";
    
    public AnAdvert getAnAdvertFromLink(ExpandedLink link) {
        AnAdvert advert = new AnAdvert();
        String content;
        
        OlxRequest request = new OlxRequest();
        OlxResult result = request.makeRequest(link.getLink());
        content = result.getContent();
        
        advert.setAuthor(getAuthorFromContent(content));
        advert.setDescription(getDescriptionFromContent(content));
        advert.setHeader(getHeaderFromContent(content));
        advert.setId(getIdFromContent(content));
        advert.setLink(link.getLink());        
        advert.setPrice(getPriceFromContent(content));
        advert.setPublicationDate(getPublicationDateFromContent(content));
        advert.setRegion(getRegionFromContent(content));
        advert.setPhone(getPhoneFromUrlAndResult(link.getLink(), result));
        advert.setUserId(getUserIdFromLink(link.getLink()));
        advert.setUserOffers(getUserOffersFromContent(content));
        advert.setUserSince(getUserSinceFromContent(content));
        advert.setInitQuery(link.getInitQuery());
        
        return advert;
    }
    
    private String getPhoneFromUrlAndResult(String urlStr, OlxResult result) {      
        String idStr = this.getUserIdFromLink(urlStr);
        if(idStr == null)   
                return null;
        
        String token = getTokenFromContent(result.getContent());
        String phoneUrlStr = OlxCategory.OLX_HOST_URL + 
                                    OLX_PHONE_URL + idStr + "/?pt=" + token;
        log.error(classname, Thread.currentThread().getName() + ": " + phoneUrlStr);
        OlxRequest request = new OlxRequest();
        OlxResult phoneRequestResult = 
                request.makeRequest(phoneUrlStr, urlStr, result);//result.getCookies()
        String phoneStr = phoneRequestResult.getContent();

        String startPattern = "{\"value\":\"";
        String endPattern = "\"}";
        String ret = getPatternCutOut(phoneStr, startPattern, endPattern);
        if(ret != null) {
            ret = ret.trim();
//            if(ret.equals("000 000 000")) {
//                ret = null;
//            }
        }
        return ret;
    }
    
    private String getUserIdFromLink(String link) {
        String regExpUrl = "https://www\\.olx\\.ua/" + 
                            OLX_ADVERT_URL + "(.*)\\-ID(.*)\\.html(.*)";    
        Pattern pattern = Pattern.compile(regExpUrl, Pattern.DOTALL);  
        
        Matcher matcher = pattern.matcher(link);        
        if(matcher.matches() == false) return null;
        
        String startPattern = "-ID";
        String endPattern = ".html";
        return getPatternCutOut(link, startPattern, endPattern);    
    }
    
    public String getTokenFromContent(String content) {
        String regExpUrl = "(.*)var phoneToken = \\'(.*)\\';(.*)";    
        Pattern pattern = Pattern.compile(regExpUrl, Pattern.DOTALL);  
        
        Matcher matcher = pattern.matcher(content);        
        if(matcher.matches() == false) return null;
        
        String startPattern = "var phoneToken = '";
        String endPattern = "';";
        return getPatternCutOut(content, startPattern, endPattern);        
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
        if(ret != null) return removeHtmlTags(ret).trim();
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
        if(ret == null) 
                return null;
        String dateContent = ret.trim();
        Date date = getDateFromPublicationDate(dateContent);
  
        return Time.timeToDateString(date.getTime());
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
        String startPattern = "<div class=\"offer-user__details \">";
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
    
    private String removeHtmlTags(String content) {
        //String ret = Jsoup.parse(content).text();
        return content.replaceAll("\\<[^>]{1,10}?>","")
                .replaceAll("class=\"[^\"]{1,20}?\"","")
                .replaceAll("data-id=\"[^\"]{1,10}?\"","")
                .replaceAll("data-raw=\"[^\"]{1,20}?\"","");
    }
    
    private Date getDateFromPublicationDate(String content) {
        String startPattern = "в ";
        String endPattern = ",";
        String timeStr = getPatternCutOut(content, startPattern, endPattern);
        
        String[] timeParts = timeStr.trim().split(":");
        
        startPattern = ", ";
        endPattern = ",";
        String dateStr = getPatternCutOut(content, startPattern, endPattern);
        
        String[] dateParts = dateStr.split(" ");
        Integer hours, minutes;
        Integer date, year, month;
        try {
            hours = Integer.parseInt(timeParts[0]);
            minutes = Integer.parseInt(timeParts[1]);
            date = Integer.parseInt(dateParts[0]);
            year = Integer.parseInt(dateParts[2]);
        } catch(Exception e) {
            return null;
        }        
        switch(dateParts[1]) {
            case "января":
                            month = 1;
                            break;
            case "февраля":
                            month = 2;
                            break;
            case "марта":
                            month = 3;
                            break;
            case "апреля":
                            month = 4;
                            break;
            case "мая":
                            month = 5;
                            break;
            case "июня":
                            month = 6;
                            break;
            case "июля":
                            month = 7;
                            break;
            case "августа":
                            month = 8;
                            break;
            case "сентября":
                            month = 9;
                            break;
            case "октября":
                            month = 10;
                            break;
            case "ноября":
                            month = 11;
                            break;
            case "декабря":
                            month = 12;
                            break;
            default:
                            month = null;
                            break;
        }
        if(month == null)
                return null;
        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, date, hours, minutes);
        Date retDate = c.getTime();
        return retDate;
    }
    
    public static void main(String[] args) {
        String urlStr = "https://www.olx.ua/obyavlenie/learn-version-control-with-"
            + "git-raspredelennaya-sistema-upravleniya-vers-IDya9jS.html#5b61bf5b91";
        
        OlxAnAdvertParser parser = new OlxAnAdvertParser();

        ExpandedLink link = new ExpandedLink();
        link.setLink(urlStr);
        
        AnAdvert anAdvert = parser.getAnAdvertFromLink(link);
        System.out.println(anAdvert.getUserId());
        
    }
    
}
