/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.parser;

import com.apu.olxcrawler.OlxCategory;
import com.apu.olxcrawler.query.GetRequest;
import com.apu.olxcrawler.entity.AnAdvert;
import com.apu.olxcrawler.entity.ExpandedLink;
import com.apu.olxcrawler.parseProcess.PhoneNumberQuery;
import static com.apu.olxcrawler.parser.OlxParserUtils.getPatternCutOut;
import com.apu.olxcrawler.query.GetRequestException;
import com.apu.olxcrawler.query.QueryResult;
import com.apu.olxcrawler.utils.DataChecker;
import com.apu.olxcrawler.utils.Log;
import com.apu.olxcrawler.utils.Time;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author Anikeichyk Pavel
 * @email  pasha_anik@ukr.net
 * 
 */
public class OlxAnAdvertParser {
    
    private static final Log log = Log.getInstance();
    private final Class classname = OlxAnAdvertParser.class;    
        
    public static final String OLX_PHONE_URL = "ajax/misc/contact/phone/";
    public static final String OLX_ADVERT_URL = "obyavlenie/";

    public OlxAnAdvertParser() {
    }
    
    public AnAdvert getAnAdvertFromLink(ExpandedLink link) 
                throws GetRequestException, IllegalInputValueException {
        if(link == null) 
            throw new IllegalInputValueException("Input link = NULL");
        AnAdvert advert = new AnAdvert();
        String content;
        
        GetRequest request = new GetRequest();
        QueryResult result = request.makeRequest(link.getLink(), GetRequest.OLX_HOST);
        content = result.getContent();
        
        advert.setAuthor(getAuthorFromContent(content));
        advert.setDescription(getDescriptionFromContent(content));
        advert.setHeader(getHeaderFromContent(content));
        try {
            advert.setId(getIdFromContent(content));
        } catch(IllegalInputValueException ex) {
            result.setInvalid();
            throw new IllegalInputValueException(ex.toString());
        }
        advert.setLink(link.getLink());        
        advert.setPrice(getPriceFromContent(content));
        advert.setPublicationDate(getPublicationDateFromContent(content));
        advert.setRegion(getRegionFromContent(content));
        advert.setPhone(getPhoneAvailabilityFromContent(content));
        advert.setUserId(getUserIdFromLink(link.getLink()));
        advert.setUserOffers(getUserOffersFromContent(content));
        advert.setUserSince(getUserSinceFromContent(content));
        advert.setInitQuery(link.getInitQuery());
        
        
        advert.setPreviousQueryResult(result);        
//        try {
//            outputQueryQueue.put(new PhoneNumberQuery(advert, result));
//        } catch (InterruptedException ex) {
//            log.error(classname, ExceptionUtils.getStackTrace(ex));
//        }
        
        return advert;
    }
    
    public static String cleanLinkForCompare(String link) {
        if(link == null)    
                return null;
        String endPattern = ".html";
        int endPosition = link.indexOf(endPattern);
        if(endPosition == -1)
                return null;
        return link.substring(0, endPosition + endPattern.length());
    }
    
    public static String getUserIdFromLink(String link) {
        if(link == null)    return null;
        String regExpUrl = "https://www\\.olx\\.ua/" + 
                            OLX_ADVERT_URL + "(.*)\\-ID(.*)\\.html(.*)";    
        Pattern pattern = Pattern.compile(regExpUrl, Pattern.DOTALL);  
        
        Matcher matcher = pattern.matcher(link);        
        if(matcher.matches() == false) return null;
        
        String startPattern = "-ID";
        String endPattern = ".html";
        return getPatternCutOut(link, startPattern, endPattern);    
    }
    
    private String getAuthorFromContent(String content) {
        if(content == null) return null;
        String innerContent = getUserDetailsBlockFromContent(content);
        String startPattern = "\">";
        String endPattern = "</a>";
        String ret = getPatternCutOut(innerContent, startPattern, endPattern);
        if(ret != null) {
            ret = ret.trim();
            ret = removeHtmlTags(ret);
            ret = ret.trim();
            if((ret != null) && (ret.length() > 30)) {
                log.error(classname, "Too long name: " + ret);                
                ret = ret.substring(0, 30);
            } 
            return ret;            
        }
        else 
            return ret;
    }   
    
    private String getDescriptionFromContent(String content) {
        if(content == null) return null;
        String innerContent = getOfferDescriptionContentBlockFromContent(content);
        String startPattern = "<p class=\"pding10 lheight20 large\">";
        String endPattern = "</p>";
        String ret = getPatternCutOut(innerContent, startPattern, endPattern);
        if(ret != null) {
            ret = removeHtmlTags(ret).trim();
            ret = ret.replaceAll("\\s+", " ");//[\\pP\\s]
            return ret;
        }
        else            return ret;
    }
    
    private String getHeaderFromContent(String content) {
        if(content == null) return null;
        String innerContent = getOfferTitleboxBlockFromContent(content);
        String startPattern = "<h1>";
        String endPattern = "</h1>";
        String ret = getPatternCutOut(innerContent, startPattern, endPattern);
        if(ret != null) {
            ret = ret.replaceAll("\\s+", " ");
            return ret.trim();
        }
        else            return ret;
    }
    
    private String getIdFromContent(String content) throws IllegalInputValueException {
        if(content == null) return null;
        String innerContent = getOfferTitleboxDetailsBlockFromContent(content);
        String startPattern = "<small>Номер объявления:";
        String endPattern = "</small>";
        String ret = getPatternCutOut(innerContent, startPattern, endPattern);
        if(ret != null) return ret.trim();
        else {
            log.error(classname, "Advert ID error. Content.");
            throw new IllegalInputValueException("Advert parse id error.");
        }
    }
    
    private String getPriceFromContent(String content) {
        if(content == null) return null;
        String innerContent = getPriceLabelBlockFromContent(content);
        String startPattern = "<strong class=\"xxxx-large not-arranged\">";
        String endPattern = "</strong>";
        String ret = getPatternCutOut(innerContent, startPattern, endPattern);
        if(ret != null) return ret.trim();
        
        startPattern = "<strong class=\"xxxx-large arranged\">";
        endPattern = "</strong>";
        ret = getPatternCutOut(innerContent, startPattern, endPattern);
        if(ret != null) {
            ret = removeHtmlTags(ret);
            ret = ret.trim();
            if((ret != null) && (ret.length() > 15)) {
                log.error(classname, "Too long price: " + ret);                
                ret = ret.substring(0, 15);
            } 
            return ret;
        }
        return ret;
    }
    
    private String getPhoneAvailabilityFromContent(String content) {
        if(content == null) return null;
        String innerContent = getPhoneLabelBlockFromContent(content);
        if(innerContent != null)
            return null;    //we have to query phone
        return "";          //user hasn't phone
    }
    
    private String getPublicationDateFromContent(String content) {
        if(content == null) return null;
        String innerContent = getOfferTitleboxDetailsBlockFromContent(content);
        String startPattern = "Опубликовано с мобильного</a>";
        String endPattern = "<small>";
        String ret = getPatternCutOut(innerContent, startPattern, endPattern);
        if(ret != null) {
            String dateContent = ret.trim();
            Date date = getDateFromPublicationDate(dateContent);  
            return Time.timeToDateString(date.getTime());
        }
        
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
        if(content == null) return null;
        String innerContent = getOfferTitleboxDetailsBlockFromContent(content);
        String startPattern = "<strong>";
        String endPattern = "</strong>";
        String ret = getPatternCutOut(innerContent, startPattern, endPattern);
        if(ret != null) return ret.trim();
        else            return ret;
    }
    
    private String getUserOffersFromContent(String content) {
        if(content == null) return null;
        String innerContent = getUserDetailsBlockFromContent(content);
        String startPattern = "<a href=\"";
        String endPattern = "\">";
        return getPatternCutOut(innerContent, startPattern, endPattern);
    }
    
    private String getUserSinceFromContent(String content) {
        if(content == null) return null;
        String innerContent = getUserDetailsBlockFromContent(content);
        String startPattern = "<span class=\"user-since\">на OLX с";
        String endPattern = "</span>";
        String ret = getPatternCutOut(innerContent, startPattern, endPattern);
        if(ret == null) 
                return null;
        String dateContent = ret.trim();
        Date date = getDateFromSinceDate(dateContent);
        if(date == null) {
            return null;
        }
        return Time.timeToDateString(date.getTime());
    }
    
    private String getUserDetailsBlockFromContent(String content) {
        if(content == null) return null;
        String startPattern = "<div class=\"offer-user__details \">";
        String endPattern = "</div>";
        return getPatternCutOut(content, startPattern, endPattern);
    }  
    
    private String getPriceLabelBlockFromContent(String content) {
        if(content == null) return null;
        String startPattern = "<div class=\"price-label\">";
        String endPattern = "</div>";
        return getPatternCutOut(content, startPattern, endPattern);
    }
    
    private String getPhoneLabelBlockFromContent(String content) {
        if(content == null) return null;
        String startPattern = "<div class=\"contact-button link-phone";
        String endPattern = "</div>";
        String block = getPatternCutOut(content, startPattern, endPattern);
        return getPhoneLabelFromBlock(block);
    }
    
    private String getPhoneLabelFromBlock(String content) {
        if(content == null) return null;
        String startPattern = "data-rel=\"phone\">";
        int startPosition = content.indexOf(startPattern) ;
        if(startPosition == -1)
                            return null;
        return content.substring(startPosition + startPattern.length());
    }
    
    private String getOfferDescriptionContentBlockFromContent(String content) {
        if(content == null) return null;
        String startPattern = "<div class=\"clr descriptioncontent marginbott20\">";
        String endPattern = "<div id=\"offerbottombar\" class=\"pding15\">";
        return getPatternCutOut(content, startPattern, endPattern);
    }
    
    private String getOfferTitleboxBlockFromContent(String content) {
        if(content == null) return null;
        String startPattern = "<div class=\"offer-titlebox\">";
        String endPattern = "<div class=\"offer-titlebox__details\">";
        return getPatternCutOut(content, startPattern, endPattern);
    }
    
    private String getOfferTitleboxDetailsBlockFromContent(String content) {
        if(content == null) return null;
        String startPattern = "<div class=\"offer-titlebox__details\">";
        String endPattern = "</div>";
        return getPatternCutOut(content, startPattern, endPattern);
    }
    
    public static String removeHtmlTags(String content) {
        if(content == null) return null;
        //String ret = Jsoup.parse(content).text();
        return content.replaceAll("\\<[^>]{1,10}?>","")
                .replaceAll("class=\"[^\"]{1,20}?\"","")
                .replaceAll("data-id=\"[^\"]{1,10}?\"","")
                .replaceAll("data-raw=\"[^\"]{1,20}?\"","");
    }
    
    private Date getDateFromPublicationDate(String content) {
        if(content == null) return null;
        
        String regExpUrl = "(.){1,3}\\d{1,2}\\:\\d{2}(,)\\s+\\d{1,2}\\s+(.){8,14}";
        if(DataChecker.regularCheck(content, regExpUrl) == false) {
            log.error(classname, "Wrong content :" + content);
            return null;
        }
        
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
        } catch(NumberFormatException e) {
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
    
    private Date getDateFromSinceDate(String dateStr) {
        if(dateStr == null) return null;
        
        String regExpUrl = "(.*)\\s+\\d{4}";
        if(DataChecker.regularCheck(dateStr, regExpUrl) == false) {
            log.error(classname, "Wrong dateStr :" + dateStr);
            return null;
        }
        
        String[] dateParts = dateStr.split(" ");
        Integer year, month;
        try {
            year = Integer.parseInt(dateParts[1]);
        } catch(NumberFormatException e) {
            return null;
        }        
        switch(dateParts[0]) {
            case "янв.":
                            month = 1;
                            break;
            case "февр.":
                            month = 2;
                            break;
            case "марта":
                            month = 3;
                            break;
            case "апр.":
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
            case "авг.":
                            month = 8;
                            break;
            case "сент.":
                            month = 9;
                            break;
            case "окт.":
                            month = 10;
                            break;
            case "нояб.":
                            month = 11;
                            break;
            case "дек.":
                            month = 12;
                            break;
            default:
                            month = null;
                            break;
        }
        if(month == null)
                return null;
        Calendar c = Calendar.getInstance();
        c.set(year, month - 1, 1);
        Date retDate = c.getTime();
        return retDate;
    }
    
//    public static void main(String[] args) {
//        String urlStr = "https://www.olx.ua/obyavlenie/learn-version-control-with-"
//            + "git-raspredelennaya-sistema-upravleniya-vers-IDya9jS.html#5b61bf5b91";
//        
//        OlxAnAdvertParser parser = new OlxAnAdvertParser();
//
//        ExpandedLink link = new ExpandedLink();
//        link.setLink(urlStr);
//        
//        AnAdvert anAdvert = parser.getAnAdvertFromLink(link);
//        System.out.println(anAdvert.getUserId());
//        
//    }
    
}
