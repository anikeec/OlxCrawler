/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.query;

import com.apu.olxcrawler.OlxVariables;
import com.apu.olxcrawler.entity.CookieItem;
import com.apu.olxcrawler.entity.CookieItemList;
import com.apu.olxcrawler.parser.OlxAnAdvertParser;
import com.apu.olxcrawler.utils.Log;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpHeaders;

/**
 *
 * @author apu
 */
public class OlxRequest {
    
    private static final Log log = Log.getInstance();
    private final Class classname = OlxRequest.class;
    
    private static final Object lockForCookies = new Object();
    
    private final ConnectionManager connectionManager = 
                            ConnectionManager.getInstance();
    
    public OlxResult makeRequest(String urlStr) {
        HttpClientItem httpClientItem = connectionManager.getClient();
        HttpClient client = httpClientItem.getHttpClient();
//        client.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
        GetMethod request = new GetMethod(urlStr);
        
        try {
            request.setRequestHeader(HttpHeaders.HOST, "www.olx.ua");
            request.setRequestHeader(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,"
                    + "application/xml;q=0.9,image/webp,image/apng,*/*");
            request.setRequestHeader(HttpHeaders.ACCEPT_LANGUAGE, "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7");
            request.setRequestHeader(HttpHeaders.ACCEPT_ENCODING, "Accept-Encoding: gzip, deflate, br");
            request.setRequestHeader(HttpHeaders.CONNECTION, "keep-alive");
            request.setRequestHeader("X-Requested-With", "XMLHttpRequest");
            request.setRequestHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; Win64; x64) "
                    + "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.167 Safari/537.36");
            log.error(classname, Thread.currentThread().getName() + " send request");
            client.executeMethod(request);
            log.error(classname, Thread.currentThread().getName() + " get responce");
            String responseBody = request.getResponseBodyAsString();
            Cookie[] cookies = client.getState().getCookies();

            return new OlxResult(responseBody, cookies);
        } catch (IOException ex) {
            log.error(classname, ExceptionUtils.getStackTrace(ex));
        } finally {
            request.releaseConnection();
            connectionManager.putClient(httpClientItem);
        }
        return null;
    }
    
    public OlxResult makeRequest(String urlStr, String refererUrlStr, Cookie[] cookies) {
        HttpClientItem httpClientItem = connectionManager.getClient();
        HttpClient client = httpClientItem.getHttpClient();
        client.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
        GetMethod request = new GetMethod(urlStr);
        
        try {
            HttpState state = new HttpState();
            state.addCookies(cookies);
            client.setState(state);
            request.setRequestHeader(HttpHeaders.HOST, "www.olx.ua");
            request.setRequestHeader(HttpHeaders.REFERER, refererUrlStr);
            request.setRequestHeader(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,"
                    + "application/xml;q=0.9,image/webp,image/apng,*/*");
            request.setRequestHeader(HttpHeaders.ACCEPT_LANGUAGE, "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7");
            request.setRequestHeader(HttpHeaders.ACCEPT_ENCODING, "Accept-Encoding: gzip, deflate, br");
            request.setRequestHeader(HttpHeaders.CONNECTION, "keep-alive");
            request.setRequestHeader("X-Requested-With", "XMLHttpRequest");
            request.setRequestHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; Win64; x64) "
                    + "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.167 Safari/537.36");
            log.error(classname, Thread.currentThread().getName() + " send request");
            client.executeMethod(request);
            log.error(classname, Thread.currentThread().getName() + " get responce");
            String responseBody = request.getResponseBodyAsString();
            Cookie[] cookiesRet = client.getState().getCookies();
            return new OlxResult(responseBody, cookiesRet);
        } catch (IOException ex) {
            log.error(classname, ExceptionUtils.getStackTrace(ex));
        } finally {
            request.releaseConnection();
            connectionManager.putClient(httpClientItem);
        }
        return null;
    }
    
    public OlxResult makeRequestFull(String urlStr) {
        
        HttpClientItem httpClientItem = connectionManager.getClient();
        HttpClient client = httpClientItem.getHttpClient();
        GetMethod requestFirst = new GetMethod(urlStr);
        
        try {
            requestFirst.setRequestHeader(HttpHeaders.HOST, "www.olx.ua");
            requestFirst.setRequestHeader(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,"
                    + "application/xml;q=0.9,image/webp,image/apng,*/*");
            requestFirst.setRequestHeader(HttpHeaders.ACCEPT_LANGUAGE, "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7");
            requestFirst.setRequestHeader(HttpHeaders.ACCEPT_ENCODING, "Accept-Encoding: gzip, deflate, br");
            requestFirst.setRequestHeader(HttpHeaders.CONNECTION, "keep-alive");
            requestFirst.setRequestHeader("X-Requested-With", "XMLHttpRequest");
            requestFirst.setRequestHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; Win64; x64) "
                    + "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.167 Safari/537.36");
            log.error(classname, Thread.currentThread().getName() + " send first request");
            
            OlxResult resultFirst = null;  
            OlxResult resultSecond = null;
            String responseFirstBody = null;
            
            synchronized(lockForCookies) {
                client.executeMethod(requestFirst);
                log.error(classname, Thread.currentThread().getName() + " get first responce");
                responseFirstBody = requestFirst.getResponseBodyAsString();
                Cookie[] cookiesFirst = client.getState().getCookies();

                CookieItemList cookieListInput = 
                        cookiesToCookieItemList(cookiesFirst);

                resultFirst = new OlxResult(responseFirstBody, null);
                resultFirst.setCookieList(cookieListInput);
            
            
                String OLX_PHONE_URL = "ajax/misc/contact/phone/";
                OlxAnAdvertParser parser = new OlxAnAdvertParser();

                String idStr = parser.getIDfromUrl(urlStr);
                if(idStr == null)  return null;

                String token = parser.getTokenFromContent(resultFirst.getContent());

                String phoneUrlStr = OlxVariables.OLX_HOST_URL + 
                                            OLX_PHONE_URL + idStr + "/?pt=" + token;
                log.error(classname, Thread.currentThread().getName() + ": " + phoneUrlStr);


                Cookie[] cookiesSecond = 
                        cookieItemListToCookies(resultFirst.getCookieList());
        
                GetMethod requestSecond = new GetMethod(phoneUrlStr);
                HttpState state = new HttpState();
                state.addCookies(cookiesSecond);
                client.setState(state);
                requestSecond.setRequestHeader(HttpHeaders.HOST, "www.olx.ua");
                requestSecond.setRequestHeader(HttpHeaders.REFERER, urlStr);
                requestSecond.setRequestHeader(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,"
                        + "application/xml;q=0.9,image/webp,image/apng,*/*");
                requestSecond.setRequestHeader(HttpHeaders.ACCEPT_LANGUAGE, "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7");
                requestSecond.setRequestHeader(HttpHeaders.ACCEPT_ENCODING, "Accept-Encoding: gzip, deflate, br");
                requestSecond.setRequestHeader(HttpHeaders.CONNECTION, "keep-alive");
                requestSecond.setRequestHeader("X-Requested-With", "XMLHttpRequest");
                requestSecond.setRequestHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; Win64; x64) "
                        + "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.167 Safari/537.36");
                log.error(classname, Thread.currentThread().getName() + " send second request");
                client.executeMethod(requestSecond);
                log.error(classname, Thread.currentThread().getName() + " get second responce");
                String responseSecondBody = requestSecond.getResponseBodyAsString();            
                resultSecond = new OlxResult(responseFirstBody, null);
                resultSecond.setPhone(responseSecondBody);
            }
            
            return resultSecond;
 
        } catch (IOException ex) {
            log.error(classname, ExceptionUtils.getStackTrace(ex));
        } finally {
            requestFirst.releaseConnection();
            connectionManager.putClient(httpClientItem);
        }
        return null;
    }
    
    private CookieItemList cookiesToCookieItemList(Cookie[] cookiesFirst) {
        CookieItemList cookieListInput = new CookieItemList();
        for(Cookie cookie:cookiesFirst) {
            CookieItem item = new CookieItem(cookie.getName(),
                                                cookie.getValue(),
                                                cookie.getComment(), 
                                                cookie.getDomain(), 
                                                cookie.getExpiryDate(), 
                                                cookie.getPath(), 
                                                cookie.getSecure(), 
                                                cookie.isPathAttributeSpecified(), 
                                                cookie.isDomainAttributeSpecified(), 
                                                cookie.getVersion());
            cookieListInput.add(item);
        }
        return cookieListInput;
    }
    
    private Cookie[] cookieItemListToCookies(CookieItemList cookieListItem) {
        List<CookieItem> cookieList = cookieListItem.getCookieList();
        Cookie[] cookiesSecond = new Cookie[cookieList.size()];
        CookieItem cookieItem;
        for(int i=0;i<cookieList.size();i++) {
            cookieItem = cookieList.get(i);
            Date expireDate = cookieItem.getCookieExpiryDate();
            int maxAge;
            if(expireDate != null) {
                Long maxAgeLong = (expireDate.getTime() - new Date().getTime())/1000;
                maxAge = maxAgeLong.intValue();
            } else {
                maxAge = -1;
            }
            cookiesSecond[i] = new Cookie(cookieItem.getCookieDomain(),
                                    cookieItem.getName(),
                                    cookieItem.getValue(),
                                    cookieItem.getCookiePath(),
                                    maxAge,
                                    cookieItem.isIsSecure());
            cookiesSecond[i].setPathAttributeSpecified(cookieItem.isHasPathAttribute());
            cookiesSecond[i].setDomainAttributeSpecified(cookieItem.isHasDomainAttribute());
        }
        return cookiesSecond;
    }
    
}
