/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.parseProcess;

import com.apu.olxcrawler.OlxCategory;
import com.apu.olxcrawler.entity.AnAdvert;
import com.apu.olxcrawler.parser.OlxAnAdvertParser;
import static com.apu.olxcrawler.parser.OlxParserUtils.getPatternCutOut;
import com.apu.olxcrawler.query.GetRequest;
import com.apu.olxcrawler.query.GetRequestException;
import com.apu.olxcrawler.query.QueryResult;
import com.apu.olxcrawler.utils.Log;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author apu
 */
public class OlxPhoneNumberParserThread implements Runnable {
    
    private static final Log log = Log.getInstance();
    private final Class classname = OlxPhoneNumberParserThread.class;
    
    private final BlockingQueue<PhoneNumberQuery> inputQueryQueue;
    private final BlockingQueue<AnAdvert> outputAnAdvertQueue;
    private final int QUERY_TRY_COUNTER_MAX = 10;
    private final long PHONE_NUMBER_QUERY_TIMEOUT = 100;

    public OlxPhoneNumberParserThread(
                        BlockingQueue<PhoneNumberQuery> inputQueryQueue, 
                        BlockingQueue<AnAdvert> outputAnAdvertQueue) {
        this.inputQueryQueue = inputQueryQueue;
        this.outputAnAdvertQueue = outputAnAdvertQueue;
    }

    @Override
    public void run() {
        while(Thread.currentThread().isInterrupted() == false) {
            PhoneNumberQuery query;
            String phoneNumber;
            AnAdvert anAdvert;
            int querySize = 0;
            try {
                query = inputQueryQueue.take();
                if(querySize != inputQueryQueue.size()) {
                    querySize = inputQueryQueue.size();
                    log.info(classname, "PhoneQueryQueue - amount of data: " + querySize);
                }
                if((query == null) || (query.getAnAdvert() == null)) 
                    continue;
                int counter = 0;
                do{
                    phoneNumber = getPhoneFromUrlAndResult(query.getAnAdvert().getLink(),
                                                query.getPreviousQueryResult());
                    counter++;
                } while((phoneNumber == null)&&(counter<QUERY_TRY_COUNTER_MAX));
                anAdvert = new AnAdvert();
                anAdvert.setId(query.getAnAdvert().getId());
                anAdvert.setAuthor(query.getAnAdvert().getAuthor());
                anAdvert.setPhone(phoneNumber);
                log.debug(classname, Thread.currentThread().getName() + " put phone.");
                outputAnAdvertQueue.put(anAdvert); 
                Thread.sleep(PHONE_NUMBER_QUERY_TIMEOUT);
            } catch (InterruptedException ex) {
                log.error(classname, ExceptionUtils.getStackTrace(ex));
            }
        }
    }
    
    private String getPhoneFromUrlAndResult(String urlStr, QueryResult result) { 
        if(urlStr == null)    return null;
        if(result == null)    return null; 
        String idStr = OlxAnAdvertParser.getUserIdFromLink(urlStr);
        if(idStr == null)   
                return null;               
        
        String token = getTokenFromContent(result.getContent());
        if(token == null) {
            log.debug(classname, Thread.currentThread().getName() + ": token = NULL");
            return null;
        }
        String phoneUrlStr = OlxCategory.OLX_HOST_URL + 
                    OlxAnAdvertParser.OLX_PHONE_URL + idStr + "/?pt=" + token;
        log.debug(classname, Thread.currentThread().getName() + ": " + phoneUrlStr);
        GetRequest request = new GetRequest();
        try {
            QueryResult phoneRequestResult = 
                    request.makeRequest(phoneUrlStr, GetRequest.OLX_HOST, urlStr, result); //result.getCookies()
        
            String phoneStr = phoneRequestResult.getContent();

            String startPattern = "{\"value\":\"";
            String endPattern = "\"}";
            String ret = getPatternCutOut(phoneStr, startPattern, endPattern);
            if(ret != null) {
                ret = ret.trim();                
                ret = OlxAnAdvertParser.removeHtmlTags(ret);
                ret = ret.replaceAll("\\s+", "");
                ret = ret.replaceAll("[()\\-\\+]", "");
                if(ret.contains("000000")) {
                    log.info(classname, "Phone number request error: " + ret);
                    return null;
                }
                String regExpUrl = "(38)\\d{10}";
                Pattern pattern = Pattern.compile(regExpUrl, Pattern.DOTALL);
                Matcher matcher = pattern.matcher(ret);        
                if(matcher.matches() == true) {
                    ret = ret.replaceFirst("(38)", "");
                }
            } 
            
            if((ret != null) && (ret.length() > 15)) {
                log.error(classname, "Too long phone: " + ret);                
                ret = ret.substring(0, 15);
            }                
            return ret;
        } catch (GetRequestException ex) {
            log.error(classname, ExceptionUtils.getStackTrace(ex));
        }
        return null;
    }
    
    public String getTokenFromContent(String content) {
        if(content == null) return null;
        String regExpUrl = "(.*)var phoneToken = \\'(.*)\\';(.*)";    
        Pattern pattern = Pattern.compile(regExpUrl, Pattern.DOTALL);  
        
        Matcher matcher = pattern.matcher(content);        
        if(matcher.matches() == false) return null;
        
        String startPattern = "var phoneToken = '";
        String endPattern = "';";
        return getPatternCutOut(content, startPattern, endPattern);        
    }
    
}
