/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.parseProcess;

import com.apu.olxcrawler.entity.ExpandedLink;
import com.apu.olxcrawler.parser.OlxSearchParser;
import com.apu.olxcrawler.query.OlxRequest;
import com.apu.olxcrawler.utils.Log;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author Anikeichyk Pavel
 * @email  pasha_anik@ukr.net
 * 
 */
public class OlxSearchPageParserThread implements Runnable {
    
    private static final Log log = Log.getInstance();
    private final Class classname = OlxSearchPageParserThread.class;

    private final BlockingQueue<ExpandedLink> inputLinkQueue;
    private final BlockingQueue<SearchPageQuery> outputSearchPageQueue;

    public OlxSearchPageParserThread(BlockingQueue<ExpandedLink> inputLinkQueue, 
                                BlockingQueue<SearchPageQuery> outputSearchPageQueue) {
        this.inputLinkQueue = inputLinkQueue;
        this.outputSearchPageQueue = outputSearchPageQueue;
    }
    
    @Override
    public void run() {
        OlxSearchParser parser;
        parser = new OlxSearchParser();
        while(Thread.currentThread().isInterrupted() == false) {
            try {
                ExpandedLink searchPageLink = inputLinkQueue.take();
                log.error(classname, Thread.currentThread().getName() + " take link.");
                
                String content = 
                        new OlxRequest().makeRequest(searchPageLink.getLink()).getContent();
                
                List<String> linkList = parser.parseSearchResultOnePage(content);
                
                SearchPageQuery searchResult = 
                        new SearchPageQuery(searchPageLink.getLink(), 
                                            linkList,
                                            searchPageLink.getInitQuery());
                
                log.error(classname, Thread.currentThread().getName() + " put result links.");                
                outputSearchPageQueue.put(searchResult);
            } catch (InterruptedException ex) {
                log.error(classname, ExceptionUtils.getStackTrace(ex));
            }
        }
    }
    
}
