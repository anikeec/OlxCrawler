/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler;

import com.apu.olxcrawler.entity.AnAdvert;
import com.apu.olxcrawler.entity.ExpandedLink;
import com.apu.olxcrawler.parseProcess.OlxAnAdvertParserPool;
import com.apu.olxcrawler.parseProcess.OlxSearchLListToAdAdvertLThread;
import com.apu.olxcrawler.parseProcess.OlxSearchPageParserPool;
import com.apu.olxcrawler.parseProcess.SearchPageQuery;
import com.apu.olxcrawler.utils.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author Anikeichyk Pavel
 * @email  pasha_anik@ukr.net
 * 
 */
public class Crawler {
    
    private static final Log log = Log.getInstance();
    private final Class classname = Crawler.class;
    
    int POLL_TIMEOUT = 5;
    
    int SEARCH_INPUT_LINK_QUEUE_SIZE = 500;
    int SEARCH_OUTPUT_SEARCHPAGE_QUEUE_SIZE = 500;
    
    int PARSER_INPUT_LINK_QUEUE_SIZE = 1000;
    int PARSER_OUTPUT_ANADVERT_QUEUE_SIZE = 1000; 
    
    BlockingQueue<ExpandedLink> searchInputLinkQueue;
    BlockingQueue<SearchPageQuery> outputSearchPageQueue;
    OlxSearchPageParserPool searchPagePool;
    
    BlockingQueue<ExpandedLink> inputLinkQueue;
    BlockingQueue<AnAdvert> outputAnAdvertQueue;        
    OlxAnAdvertParserPool parserPool;

    public Crawler() {
        this.outputAnAdvertQueue = 
                new ArrayBlockingQueue<>(PARSER_OUTPUT_ANADVERT_QUEUE_SIZE);
        this.inputLinkQueue = 
                new ArrayBlockingQueue<>(PARSER_INPUT_LINK_QUEUE_SIZE);
        this.outputSearchPageQueue = 
                new ArrayBlockingQueue<>(SEARCH_OUTPUT_SEARCHPAGE_QUEUE_SIZE);
        this.searchInputLinkQueue = 
                new ArrayBlockingQueue<>(SEARCH_INPUT_LINK_QUEUE_SIZE);
        this.parserPool = 
                new OlxAnAdvertParserPool(inputLinkQueue, outputAnAdvertQueue);        
        this.searchPagePool = 
                new OlxSearchPageParserPool(searchInputLinkQueue, outputSearchPageQueue);        
    }    
    
        
    public void init() {         
        searchPagePool.init();  
        parserPool.init();
        
        Thread thread = new Thread(
                            new OlxSearchLListToAdAdvertLThread(outputSearchPageQueue, 
                                                                inputLinkQueue));
        thread.setDaemon(true);
        thread.start();
        thread.setName("OlxSearchToAdAdvertThread ");    
    }
    
    public List<AnAdvert> getAdvertsByQueryStr(ExpandedLink strToFind) {
//        String strToFind = "Операционные  системы";
        
        OlxSearch olxSearch = new OlxSearch();
        
        List<ExpandedLink> searchPagesLinkList = 
                            olxSearch.getLinkListBySearchQuery(strToFind);
        
        log.error(classname, "Start");
        
        for(ExpandedLink link:searchPagesLinkList) {
            try {
                searchInputLinkQueue.put(link);
            } catch (InterruptedException ex) {
                log.error(classname, ExceptionUtils.getStackTrace(ex));
            }
        }
        
        List<AnAdvert> advertList = new ArrayList<>();
        
        AnAdvert advert;
        while(true) {
            try {
                advert = outputAnAdvertQueue.poll(POLL_TIMEOUT, TimeUnit.SECONDS);
                if(advert == null)
                    break;
                advertList.add(advert);
            } catch (InterruptedException ex) {
                log.error(classname, ExceptionUtils.getStackTrace(ex));
            }
        }
        
        log.error(classname, "Finish");
        
        System.out.println("Ready");
        
        return advertList;
    }
    
}
