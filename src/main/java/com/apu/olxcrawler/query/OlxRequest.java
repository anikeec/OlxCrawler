/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.query;

import com.apu.olxcrawler.utils.Log;
import java.io.IOException;
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
    
    public OlxResult makeRequest(String urlStr) {
        HttpClient client = ConnectionManager.getInstance().getClient();
        client.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
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
            client.executeMethod(request);
            String responseBody = request.getResponseBodyAsString();
            Cookie[] cookies = client.getState().getCookies();

            return new OlxResult(responseBody, cookies);
        } catch (IOException ex) {
            log.error(classname, ExceptionUtils.getStackTrace(ex));
        } finally {
            request.releaseConnection();
        }
        return null;
    }
    
    public OlxResult makeRequest(String urlStr, String refererUrlStr, Cookie[] cookies) {
        HttpClient client = ConnectionManager.getInstance().getClient();
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
            client.executeMethod(request);
            String responseBody = request.getResponseBodyAsString();
            Cookie[] cookiesRet = client.getState().getCookies();
            return new OlxResult(responseBody, cookiesRet);
        } catch (IOException ex) {
            log.error(classname, ExceptionUtils.getStackTrace(ex));
        } finally {
            request.releaseConnection();
        }
        return null;
    }
    
}
