/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler;

import com.apu.olxcrawler.parser.OlxAnAdwertParser;
import com.apu.olxcrawler.parser.OlxSearchParser;
import com.apu.olxcrawler.parseProcess.OlxAnAdwertParserPool;
import com.apu.olxcrawler.entity.AnAdwert;
import com.apu.olxcrawler.query.OlxRequest;
import com.apu.olxcrawler.utils.Log;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 *
 * @author apu
 */
public class OlxSearch {
    
    private static final Log log = Log.getInstance();
    private static final Class classname = OlxSearch.class;
    
    private final String OLX_SEARCH_URL = "list/q-";
    
    public List<String> find(String searchStr) {
        try {
            String searchStrEncoded = URLEncoder.encode(searchStr, "utf-8");
            searchStrEncoded = OlxVariables.OLX_HOST_URL +
                    OLX_SEARCH_URL +
                    formatSearchStr(searchStrEncoded) +
                    "/";
            
            OlxAnAdwertParser parser = new OlxAnAdwertParser();
            OlxSearchParser searchParser = new OlxSearchParser();
            String searchContent = getRequest(searchStrEncoded);
            
            Integer amountOfPages = searchParser.getAmountOfPagesFromContent(searchContent);
            List<String> list = searchParser.parseSearchResultOnePage(searchContent);
            if(amountOfPages != null) {
                for(int i=2; i<(amountOfPages + 1); i++) {
                    String link = searchStrEncoded + "?page=" + i;
                    searchContent = getRequest(link);
                    list.addAll(searchParser.parseSearchResultOnePage(searchContent));
                }
            }            
            return list;
        } catch (UnsupportedEncodingException ex) {
            Logger.getLogger(OlxSearch.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }
    
    public String find(String searchStr, String region) throws Exception {
        throw new Exception("Not realized yet");
    }
    
    private String formatSearchStr(String searchStr) {
        Pattern CLEAR_PATTERN = Pattern.compile("[\\s]+");
        return CLEAR_PATTERN.matcher(searchStr).replaceAll("-").trim();
    }
    
    private String getRequest(String queryStr) {         
        return new OlxRequest().makeRequest(queryStr).getContent();
    }
    
    public static void main(String[] args) throws InterruptedException {
        OlxSearch olxSearch = new OlxSearch();
        List<String> list = olxSearch.find("системный блок новый");        
        
        List<AnAdwert> adwertList = new ArrayList<>();
        
        int QUEUE_SIZE = 50;
        
        BlockingQueue<String> inputLinkQueue = 
                                    new ArrayBlockingQueue<>(QUEUE_SIZE);
        BlockingQueue<AnAdwert> outputAnAdwertQueue = 
                                    new ArrayBlockingQueue<>(QUEUE_SIZE);
        
        OlxAnAdwertParserPool parserPool = 
                    new OlxAnAdwertParserPool(inputLinkQueue, outputAnAdwertQueue);
        parserPool.init();
        
        log.error(classname, "Start");
        
        for(String link:list) {
            inputLinkQueue.put(link);
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
