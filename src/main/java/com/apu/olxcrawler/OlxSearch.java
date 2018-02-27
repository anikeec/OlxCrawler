/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler;

import com.apu.olxcrawler.entity.AnAdwert;
import com.apu.olxcrawler.utils.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.regex.Pattern;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.CookieStore;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

/**
 *
 * @author apu
 */
public class OlxSearch {
    
    private static final Log log = Log.getInstance();
    private static final Class classname = OlxSearch.class;
    
    private final String OLX_SEARCH_URL = "list/q-";
    
    public String find(String searchStr) {
        searchStr = OlxVariables.OLX_HOST_URL +
                            OLX_SEARCH_URL +
                            formatSearchStr(searchStr) +
                            "/";
        return getRequest(searchStr);
    }
    
    public String find(String searchStr, String region) throws Exception {
        throw new Exception("Not realized yet");
    }
    
    private String formatSearchStr(String searchStr) {
        Pattern CLEAR_PATTERN = Pattern.compile("[\\s]+");
        return CLEAR_PATTERN.matcher(searchStr).replaceAll("-").trim();
    }
    
    private String getRequest(String queryStr) {
        String content;
        try (CloseableHttpClient httpClient = HttpClientBuilder.create().build()) {
                HttpGet request = new HttpGet(queryStr);
                HttpClientContext context = HttpClientContext.create();

                try (CloseableHttpResponse response = httpClient.execute(request, context)) {
                    CookieStore cookieStore = context.getCookieStore();
    //                cookies = cookieStore.getCookies();
                    HttpEntity entity = response.getEntity();
                    content = EntityUtils.toString(entity);
                }            
            } catch (Exception ex) {
                log.error(classname, ExceptionUtils.getStackTrace(ex));
                return null;
            }
        return content;
    }
    
    public static void main(String[] args) throws InterruptedException {
        OlxSearch olxSearch = new OlxSearch();
        String content = olxSearch.find("Операционные  системы");
        
        OlxParser parser = new OlxParser();
        List<String> list = parser.parseSearchResult(content);
        List<AnAdwert> adwertList = new ArrayList<>();
        
        int QUEUE_SIZE = 50;
        
        BlockingQueue<String> inputLinkQueue = 
                                    new ArrayBlockingQueue<>(QUEUE_SIZE);
        BlockingQueue<AnAdwert> outputAnAdwertQueue = 
                                    new ArrayBlockingQueue<>(QUEUE_SIZE);
        
        OlxParsersPool parserPool = 
                    new OlxParsersPool(inputLinkQueue, outputAnAdwertQueue);
        parserPool.init();
        
        log.error(classname, "Start");
        
        for(String link:list) {
//            parser = new OlxParser();
//            adwertList.add(parser.getAnAdwertFromLink(link));
            inputLinkQueue.put(link);
            //System.out.println(str);
        }
        
        int counter = 0;
        while(true) {
            adwertList.add(outputAnAdwertQueue.take());
            counter++;
            if(counter >= list.size())
                break;
        }
        
        log.error(classname, "Finish");
        
        System.out.println("Ready");
    }
    
}
