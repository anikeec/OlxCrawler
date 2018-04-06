/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.query;

import com.apu.olxcrawler.proxy.ProxyItem;
import com.apu.olxcrawler.proxy.ProxyManager;
import com.apu.olxcrawler.query.cookie.CookieItem;
import com.apu.olxcrawler.query.cookie.CookieItemList;
import com.apu.olxcrawler.utils.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Date;
import java.util.List;
import org.apache.commons.httpclient.Cookie;
import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpHost;
import org.apache.commons.httpclient.HttpState;
import org.apache.commons.httpclient.cookie.CookiePolicy;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.http.HttpHeaders;

/**
 *
 * @author Anikeichyk Pavel
 * @email  pasha_anik@ukr.net
 * 
 */
public class GetRequest {
    
    private static final Log log = Log.getInstance();
    private final Class classname = GetRequest.class;
    
    public static String OLX_HOST = "www.olx.ua";
    public static String PROXY_HOST = "www.hidemy.name";
    
    /** ConnectionManager instance */
    private final ConnectionManager connectionManager = 
                            ConnectionManager.getInstance();
    
    /**
     * Make GET request 
     * 
     * @param parameters
     * @return OlxResult - query response
     * @throws GetRequestException
     */
    public QueryResult makeRequest(QueryParams parameters) throws GetRequestException {
        String urlStr = null;
        String host = null;
        QueryResult previousResult = null;
        String refererUrlStr = null;
        boolean isProxy = false;
        boolean headerEnable = false;
        String encodingType = "utf-8";
        
        if(parameters.get(QueryParams.Parameter.URL_STR) != null)
            urlStr = (String)parameters.get(QueryParams.Parameter.URL_STR);
        if(parameters.get(QueryParams.Parameter.HOST_STR) != null)
            host = (String)parameters.get(QueryParams.Parameter.HOST_STR);        
        if(parameters.get(QueryParams.Parameter.PREVIOUS_RESULT) != null)
            previousResult = (QueryResult)parameters.get(QueryParams.Parameter.PREVIOUS_RESULT);
        if(parameters.get(QueryParams.Parameter.REFERER_URL) != null)
            refererUrlStr = (String)parameters.get(QueryParams.Parameter.REFERER_URL);
        if(parameters.get(QueryParams.Parameter.IS_PROXY) != null)
            isProxy = (boolean)parameters.get(QueryParams.Parameter.IS_PROXY);
        if(parameters.get(QueryParams.Parameter.HEADER_ENABLE) != null)
            headerEnable = (boolean)parameters.get(QueryParams.Parameter.HEADER_ENABLE);
        if(parameters.get(QueryParams.Parameter.ENCODING_TYPE) != null)
            encodingType = (String)parameters.get(QueryParams.Parameter.ENCODING_TYPE);        
        
        HttpClientItem httpClientItem = connectionManager.getClient();
        HttpClient client = httpClientItem.getHttpClient();
        client.getParams().setConnectionManagerTimeout(5000);
//        client.getHttpConnectionManager().getParams().setConnectionTimeout(5000);
        
        if((previousResult != null) && (refererUrlStr != null))
            client.getParams().setCookiePolicy(CookiePolicy.RFC_2109);
        GetMethod request = new GetMethod(urlStr);  

        ProxyManager proxyManager = ProxyManager.getInstance();
        ProxyItem proxy = null;        
        if((previousResult != null) && (refererUrlStr != null)) {        
            proxy = proxyManager.take();
            if(proxy != null) {
                HttpHost proxyHost = new HttpHost(proxy.getIp());
                HostConfiguration config = client.getHostConfiguration();
                config.setProxy(proxyHost.getHostName(), proxy.getPort());
                if(proxy.isValid() == false) {
                    log.info(classname, Thread.currentThread().getName() + 
                        ". Set INVALID proxy ip: " + proxy.getIp() + ":" + proxy.getPort() + ". "  +
                        proxyManager.getProxyInfo());
                } else 
                log.info(classname, Thread.currentThread().getName() + 
                        ". Set proxy ip: " + proxy.getIp() + ":" + proxy.getPort() + ". "  +
                        proxyManager.getProxyInfo());
                
            } else {
                HostConfiguration config = client.getHostConfiguration();
                config.setProxyHost(null);
                log.info(classname, Thread.currentThread().getName() + ". Clear proxy. " +
                        proxyManager.getProxyInfo());
            }
        }
        
        try {
            if((previousResult != null) && (refererUrlStr != null)) {                
                HttpState state = new HttpState();
                Cookie[] cookies = 
                        cookieItemListToCookies(previousResult.getCookieList());
                state.addCookies(cookies);
                client.setState(state);                
            }
            
            if(headerEnable)
                requestSetHeader(request, host, refererUrlStr);
            
            log.debug(classname, Thread.currentThread().getName() + " send request");
            client.executeMethod(request);
            
            log.debug(classname, Thread.currentThread().getName() + " get responce");
            BufferedReader breader = new BufferedReader(
                                        new InputStreamReader(
                                        request.getResponseBodyAsStream(), encodingType), 4096);  //"windows-1251"
            StringBuilder sb = new StringBuilder();
            String line;
            while((line = breader.readLine()) != null) {
                sb.append(line);
            }
            String responseBody = sb.toString();
            
            Cookie[] cookiesRet = client.getState().getCookies();
            CookieItemList cookieList = 
                        cookiesToCookieItemList(cookiesRet);
            
            return new QueryResult(responseBody, cookieList, proxy);
        } catch (IOException ex) {            
            if(proxy != null) {
                proxy.setInvalid();
                log.info(classname, Thread.currentThread().getName() + 
                        ". Proxy invalid. ip:" + proxy.getIp() + ":" + proxy.getPort() +
                        ". " + proxyManager.getProxyInfo());
            } else {
                log.info(classname, Thread.currentThread().getName() + 
                        ". Proxy invalid. ip: null" +
                        ". " + proxyManager.getProxyInfo());
            }
            throw new GetRequestException("gerRequestError.", ex);            
        } finally {
            if(proxyManager != null)
                proxyManager.put(proxy);
            request.releaseConnection();
            connectionManager.putClient(httpClientItem);
        }
    }
    
    /**
     * Make GET request with additional parameters.
     * It repeats queries until good response has come, 
     * but no more than ERRORS_MAX times.
     * As we have ability to use proxies it repeats queries through few proxies
     * 
     * @param parameters
     * @return QueryResult - query response
     * @throws GetRequestException
     */
    public QueryResult makeRequestWithRetrans(QueryParams parameters) 
                throws GetRequestException {
        final int ERRORS_MAX = 5;
        int errorCounter = 0;
        QueryResult response;
        while(true) {
            try {
                response = makeRequest(parameters);
                if(response.getContent() != null)
                    return response;
            } catch(GetRequestException ex) {
                errorCounter++;
                log.error(classname, "Request error #" + errorCounter);
                if(errorCounter >= ERRORS_MAX) {
                    throw new GetRequestException("Server or proxy unavailable", ex);
                }
            }
        }
    }
    
    /**
     * Add HTTP header to GET request 
     * 
     * @param request
     * @param refererUrlStr
     */
    private void requestSetHeader(GetMethod request, String host, String refererUrlStr) {
        request.setRequestHeader(HttpHeaders.HOST, host);
        if(refererUrlStr != null) {
            request.setRequestHeader(HttpHeaders.REFERER, refererUrlStr);
        }
        request.setRequestHeader(HttpHeaders.ACCEPT, "text/html,application/xhtml+xml,"
                + "application/xml;q=0.9,image/webp,image/apng,*/*");
        request.setRequestHeader(HttpHeaders.ACCEPT_LANGUAGE, "ru-RU,ru;q=0.9,en-US;q=0.8,en;q=0.7");
        request.setRequestHeader(HttpHeaders.ACCEPT_CHARSET, "utf-8");
        request.setRequestHeader(HttpHeaders.ACCEPT_ENCODING, "Accept-Encoding: gzip, deflate");
        request.setRequestHeader(HttpHeaders.CONNECTION, "keep-alive");
        request.setRequestHeader("X-Requested-With", "XMLHttpRequest");
        request.setRequestHeader(HttpHeaders.USER_AGENT, "Mozilla/5.0 (Windows NT 6.1; Win64; x64) "
                + "AppleWebKit/537.36 (KHTML, like Gecko) Chrome/64.0.3282.167 Safari/537.36");
    }
    
    /**
     * Remake Cookie array to CookieItemList 
     * 
     * @param cookiesFirst
     * @return CookieItemList
     */
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
    
    /**
     * Remake CookieItemList to Cookie array
     * 
     * @param cookieListItem
     * @return Cookie[]
     */
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
