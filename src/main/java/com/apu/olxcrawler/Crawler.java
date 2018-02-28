/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler;

import com.apu.olxcrawler.entity.AnAdwert;
import com.apu.olxcrawler.parseProcess.OlxAnAdwertParserPool;
import com.apu.olxcrawler.parseProcess.OlxSearchLListToAdAdwertLThread;
import com.apu.olxcrawler.parseProcess.OlxSearchPageParserPool;
import com.apu.olxcrawler.parseProcess.SearchPageQuery;
import com.apu.olxcrawler.utils.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author apu
 */
public class Crawler {
    
    private static final Log log = Log.getInstance();
    private final Class classname = Crawler.class;
    
    int SEARCH_INPUT_LINK_QUEUE_SIZE = 500;
    int SEARCH_OUTPUT_SEARCHPAGE_QUEUE_SIZE = 500;
    
    int PARSER_INPUT_LINK_QUEUE_SIZE = 1000;
    int PARSER_OUTPUT_ANADWERT_QUEUE_SIZE = 1000; 
    
    BlockingQueue<String> searchInputLinkQueue;
    BlockingQueue<SearchPageQuery> outputSearchPageQueue;
    OlxSearchPageParserPool searchPagePool;
    
    BlockingQueue<String> inputLinkQueue;
    BlockingQueue<AnAdwert> outputAnAdwertQueue;        
    OlxAnAdwertParserPool parserPool;

    public Crawler() {
        this.outputAnAdwertQueue = 
                new ArrayBlockingQueue<>(PARSER_OUTPUT_ANADWERT_QUEUE_SIZE);
        this.inputLinkQueue = 
                new ArrayBlockingQueue<>(PARSER_INPUT_LINK_QUEUE_SIZE);
        this.outputSearchPageQueue = 
                new ArrayBlockingQueue<>(SEARCH_OUTPUT_SEARCHPAGE_QUEUE_SIZE);
        this.searchInputLinkQueue = 
                new ArrayBlockingQueue<>(SEARCH_INPUT_LINK_QUEUE_SIZE);
        this.parserPool = 
                new OlxAnAdwertParserPool(inputLinkQueue, outputAnAdwertQueue);        
        this.searchPagePool = 
                new OlxSearchPageParserPool(searchInputLinkQueue, outputSearchPageQueue);        
    }    
    
        
    public void init() {         
        searchPagePool.init();  
        parserPool.init();
        
        Thread thread = new Thread(
                            new OlxSearchLListToAdAdwertLThread(outputSearchPageQueue, 
                                                                inputLinkQueue));
        thread.setDaemon(true);
        thread.start();
        thread.setName("OlxSearchToAdAdwertThread ");    
    }
    
    public List<AnAdwert> getAdwertsByQueryStr(String strToFind) {
//        String strToFind = "Операционные  системы";
        
        OlxSearch olxSearch = new OlxSearch();
        
        List<String> searchPagesLinkList = 
                            olxSearch.getLinkListBySearchQuery(strToFind);
        
        log.error(classname, "Start");
        
        for(String link:searchPagesLinkList) {
            try {
                searchInputLinkQueue.put(link);
            } catch (InterruptedException ex) {
                log.error(classname, ExceptionUtils.getStackTrace(ex));
            }
        }
        
        List<AnAdwert> adwertList = new ArrayList<>();
        
        int counter = 0;
        while(true) {
            try {
                adwertList.add(outputAnAdwertQueue.take());
            } catch (InterruptedException ex) {
                log.error(classname, ExceptionUtils.getStackTrace(ex));
            }
            counter++;
            if(counter >= 20)
                break;
        }
        
        log.error(classname, "Finish");
        
        System.out.println("Ready");
        
        return adwertList;
    }
    
}
