/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler;

import com.apu.olxcrawler.entity.AnAdvert;
import com.apu.olxcrawler.entity.ExpandedLink;
import com.apu.olxcrawler.mainlogic.AnAdvertKeeperThread;
import com.apu.olxcrawler.parseProcess.OlxAnAdvertParserPool;
import com.apu.olxcrawler.parseProcess.OlxPhoneNumberParserThread;
import com.apu.olxcrawler.parseProcess.OlxSearchLListToAdAdvertLThread;
import com.apu.olxcrawler.parseProcess.OlxSearchPageParserPool;
import com.apu.olxcrawler.parseProcess.PhoneNumberQuery;
import com.apu.olxcrawler.parseProcess.SearchPageQuery;
import com.apu.olxcrawler.parser.IllegalInputValueException;
import com.apu.olxcrawler.proxy.ProxyGetThread;
import com.apu.olxcrawler.proxy.ProxyManager;
import com.apu.olxcrawler.utils.Log;
import com.apu.olxcrawler.utils.ThreadPool;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    
    int SEARCH_INPUT_LINK_QUEUE_SIZE = 10000;
    int SEARCH_OUTPUT_SEARCHPAGE_QUEUE_SIZE = 10000;
    
    int PARSER_INPUT_LINK_QUEUE_SIZE = 1000000;
    int PARSER_OUTPUT_ANADVERT_QUEUE_SIZE = 1000000;
    int PARSER_OUTPUT_PHONE_QUEUE_SIZE = 1000;
    
    private int PHONE_NUMBER_POOL_SIZE = 3;
    
    BlockingQueue<ExpandedLink> searchInputLinkQueue;
    BlockingQueue<SearchPageQuery> searchOutputPageQueue;
    OlxSearchPageParserPool searchPagePool;
    
    BlockingQueue<ExpandedLink> searchOutputLinkQueue;
    BlockingQueue<AnAdvert> anAdvertParserOutputQueue;
    private final BlockingQueue<PhoneNumberQuery> phoneQueryQueue;
    OlxAnAdvertParserPool parserPool;

    public Crawler() {
        this.anAdvertParserOutputQueue = 
                new ArrayBlockingQueue<>(PARSER_OUTPUT_ANADVERT_QUEUE_SIZE);
        this.searchOutputLinkQueue = 
                new ArrayBlockingQueue<>(PARSER_INPUT_LINK_QUEUE_SIZE);
        this.searchOutputPageQueue = 
                new ArrayBlockingQueue<>(SEARCH_OUTPUT_SEARCHPAGE_QUEUE_SIZE);
        this.searchInputLinkQueue = 
                new ArrayBlockingQueue<>(SEARCH_INPUT_LINK_QUEUE_SIZE);
        this.phoneQueryQueue = 
                new ArrayBlockingQueue<>(PARSER_OUTPUT_PHONE_QUEUE_SIZE);
        this.searchPagePool = 
                new OlxSearchPageParserPool(searchInputLinkQueue, searchOutputPageQueue);    
        this.parserPool = 
                new OlxAnAdvertParserPool(searchOutputLinkQueue, anAdvertParserOutputQueue);        
    }    
    
        
    public void init() {         
        searchPagePool.init();  
        parserPool.init();
        
        ThreadPool phoneNumberThreadPool = new ThreadPool(PHONE_NUMBER_POOL_SIZE);
        OlxPhoneNumberParserThread phoneNumberParserThread;
        for(int i=0; i<PHONE_NUMBER_POOL_SIZE; i++) {
            phoneNumberParserThread = 
                new OlxPhoneNumberParserThread(phoneQueryQueue, anAdvertParserOutputQueue);
            phoneNumberThreadPool.submit(phoneNumberParserThread, "OlxPhoneNumberParserThread " + (i + 1));
        }        
        
        ProxyManager proxyManager = new ProxyManager();
        ProxyManager.setInstance(proxyManager);
        proxyManager.init();
        
        ProxyGetThread proxyGetThread = new ProxyGetThread();
        proxyGetThread.init();
        proxyGetThread.start();
        
        try {
            Thread.sleep(200);
        } catch (InterruptedException ex) {
            Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        while(proxyGetThread.isQueryActive()) {}
        
        Thread thread = new Thread(
                            new OlxSearchLListToAdAdvertLThread(searchOutputPageQueue, 
                                                                searchOutputLinkQueue));
        thread.setDaemon(true);
        thread.start();
        thread.setName("OlxSearchToAdAdvertThread ");    
    }
    
    public List<AnAdvert> getAdvertsByQueryStr(ExpandedLink strToFind) {
//        String strToFind = "Операционные  системы";
        
        OlxSearch olxSearch = new OlxSearch();
        
        List<ExpandedLink> searchPagesLinkList;
        List<AnAdvert> advertList = new ArrayList<>();
        try {
            searchPagesLinkList = olxSearch.getLinkListBySearchQuery(strToFind);
            log.info(classname, "Seach str: " + strToFind.getInitQuery() + 
                    ". Found: " + searchPagesLinkList.size() + " links");
            for(ExpandedLink link:searchPagesLinkList) {
                try {
                    searchInputLinkQueue.put(link);
                } catch (InterruptedException ex) {
                    log.error(classname, ExceptionUtils.getStackTrace(ex));
                }
            }

            AnAdvertKeeperThread keeperThread = 
                            new AnAdvertKeeperThread(anAdvertParserOutputQueue, phoneQueryQueue);
            keeperThread.setDaemon(true);
            keeperThread.start();           
            while(true){}
//            try {
//                Thread.sleep(60000);
//            } catch (InterruptedException ex) {
//                Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
//            }
        } catch (IllegalInputValueException ex) {
            Logger.getLogger(Crawler.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        log.debug(classname, "Start");
        
        
        
//        AnAdvert advert;
//        while(true) {
//            try {
//                advert = outputAnAdvertQueue.poll(POLL_TIMEOUT, TimeUnit.SECONDS);
//                if(advert == null)
//                    break;
//                advertList.add(advert);
//            } catch (InterruptedException ex) {
//                log.error(classname, ExceptionUtils.getStackTrace(ex));
//            }
//        }
        
        log.debug(classname, "Finish");
        
        System.out.println("Ready");
        
        return advertList;
    }
    
}
