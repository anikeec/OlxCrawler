/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.query;

import com.apu.olxcrawler.utils.Log;
import java.util.List;
import org.apache.commons.lang3.exception.ExceptionUtils;
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

/**
 *
 * @author apu
 */
public class OlxRequest {
    
    private static final Log log = Log.getInstance();
    private final Class classname = OlxRequest.class;
    
    public OlxResult makeRequest(String urlStr) {
        List<Cookie> cookies = null;
        String content;
        
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
            log.error(classname, ExceptionUtils.getStackTrace(ex));
            return null;
        } 
        
        BasicCookieStore cookieStore = new BasicCookieStore();
        for(Cookie cookie:cookies) {
            cookieStore.addCookie(cookie);
        }
        
        return new OlxResult(content, cookieStore);
    }
    
    public OlxResult makeRequest(String urlStr, String refererUrlStr, BasicCookieStore cookieStore) {
        String content;
        
        try (CloseableHttpClient httpClient = HttpClientBuilder
                    .create().setDefaultCookieStore(cookieStore).build()) {

            log.error(classname, urlStr);
            
            HttpGet request = new HttpGet(urlStr);
            request.addHeader(HttpHeaders.HOST, "www.olx.ua");
            request.addHeader(HttpHeaders.REFERER, refererUrlStr);
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
                content = EntityUtils.toString(entity);            
            } 
        } catch (Exception ex) {
            log.error(classname, ExceptionUtils.getStackTrace(ex));
            return null;
        }
        
        log.error(classname, "get results");
        
        return new OlxResult(content, cookieStore);
    }
    
    
}
