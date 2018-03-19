/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.query;

import com.apu.olxcrawler.query.cookie.CookieItem;
import com.apu.olxcrawler.query.cookie.CookieItemList;
import com.apu.olxcrawler.utils.Log;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpHeaders;

/**
 *
 * @author Anikeichyk Pavel
 * @email  pasha_anik@ukr.net
 * 
 */
public class OlxRequest {
    
    private static final Log log = Log.getInstance();
    private final Class classname = OlxRequest.class;
    
    private final ConnectionManager connectionManager = 
                            ConnectionManager.getInstance();
    
    public OlxResult makeRequest(String urlStr) throws GetRequestException {
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
            
            CookieItemList cookieList = 
                        cookiesToCookieItemList(cookies);

            return new OlxResult(responseBody, cookieList);
        } catch (IOException ex) {
            throw new GetRequestException("gerRequestError.", ex);
        } finally {
            request.releaseConnection();
            connectionManager.putClient(httpClientItem);
        }
    }
    
    public OlxResult makeRequest(String urlStr, String refererUrlStr, OlxResult previousResult) 
                throws GetRequestException {
        HttpClientItem httpClientItem = connectionManager.getClient();
        HttpClient client = httpClientItem.getHttpClient();
        client.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
        GetMethod request = new GetMethod(urlStr);
        
        Cookie[] cookies = 
                        cookieItemListToCookies(previousResult.getCookieList());
        
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
            CookieItemList cookieList = 
                        cookiesToCookieItemList(cookiesRet);
            return new OlxResult(responseBody, cookieList);
        } catch (IOException ex) {
            throw new GetRequestException("gerRequestError.", ex);
        } finally {
            request.releaseConnection();
            connectionManager.putClient(httpClientItem);
        }
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
