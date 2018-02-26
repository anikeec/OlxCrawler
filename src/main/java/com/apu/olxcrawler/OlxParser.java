/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHeaders;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author apu
 */
public class OlxParser {
    
    private static final Log log = Log.getInstance();
    private final Class classname = OlxParser.class;    
        
    private final String OLX_PHONE_URL = "ajax/misc/contact/phone/";
    
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
                String link = getLinkFromContent(contentTemp, position);
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
    
    private String getLinkFromContent(String content, Pointer startPosition) {        
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
    
    String requestPhoneFromUrl(String urlStr) {
        String phoneResult = null;
        
        String idStr = this.getIDfromUrl(urlStr);
        if(idStr == null)   
                return null;
        
        String content;
        List<Cookie> cookies = null;
        
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
            HttpGet request = new HttpGet(urlStr);
            HttpClientContext context = HttpClientContext.create();

            try (CloseableHttpResponse response = httpClient.execute(request, context)) {
                CookieStore cookieStore = context.getCookieStore();
                cookies = cookieStore.getCookies();
                HttpEntity entity = response.getEntity();
                content = EntityUtils.toString(entity);
            }            
        } catch (Exception ex) {
            log.debug(classname, ExceptionUtils.getStackTrace(ex));
            return null;
        } 
        
        BasicCookieStore cookieStore = new BasicCookieStore();
        for(Cookie cookie:cookies) {
            cookieStore.addCookie(cookie);
        }
        
        try (CloseableHttpClient httpClient = HttpClientBuilder
                    .create().setDefaultCookieStore(cookieStore).build()) {
            
            String token = getTokenFromContent(content);
            if(token == null)   
                    return null;
            
            String requestPhoneNumber = OlxVariables.OLX_HOST_URL + 
                                    OLX_PHONE_URL + idStr + "/?pt=" + token;
            log.debug(classname, requestPhoneNumber);
            
            HttpGet request = new HttpGet(requestPhoneNumber);
            request.addHeader(HttpHeaders.HOST, "www.olx.ua");
            request.addHeader(HttpHeaders.REFERER, urlStr);
            request.addHeader(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,"
                    + "application/xml;q=0.9,image/webp,image/apng,*/*");
            request.addHeader(HttpHeaders.ACCEPT_LANGUAGE, "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7");
            request.addHeader(HttpHeaders.ACCEPT_ENCODING, "Accept-Encoding: gzip, deflate, br");
            request.addHeader(HttpHeaders.CONNECTION, "keep-alive");
            request.addHeader("X-Requested-With", "XMLHttpRequest");
            request.addHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; Win64; x64) "
                    + "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.167 Safari/537.36");
            
            try (CloseableHttpResponse response = httpClient.execute(request)) {                
                HttpEntity entity = response.getEntity();
                phoneResult = EntityUtils.toString(entity);            
            } 
        } catch (Exception ex) {
            log.debug(classname, ExceptionUtils.getStackTrace(ex));
            return null;
        }
        
        return phoneResult;
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
    
    public static void main(String[] args) {
        String urlStr = "https://www.olx.ua/obyavlenie/learn-version-control-with-"
            + "git-raspredelennaya-sistema-upravleniya-vers-IDya9jS.html#5b61bf5b91";
        OlxParser parser = new OlxParser();
        
        String idStr = parser.getIDfromUrl(urlStr);
        System.out.println(idStr);
        
        String phoneStr = parser.requestPhoneFromUrl(urlStr);
        System.out.println(phoneStr);
    }
    
}
