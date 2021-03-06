/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.parseProcess;

import com.apu.olxcrawler.entity.ExpandedLink;
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
public class OlxSearchLListToAdAdvertLThread implements Runnable {
    
    private static final Log log = Log.getInstance();
    private final Class classname = OlxSearchLListToAdAdvertLThread.class;

    private final BlockingQueue<SearchPageQuery> inputSearchPageQueue;
    private final BlockingQueue<ExpandedLink> outputLinkQueue;

    public OlxSearchLListToAdAdvertLThread(
                            BlockingQueue<SearchPageQuery> inputSearchPageQueue, 
                            BlockingQueue<ExpandedLink> outputLinkQueue) {
        this.inputSearchPageQueue = inputSearchPageQueue;
        this.outputLinkQueue = outputLinkQueue;
    }
    
    @Override
    public void run() {
        SearchPageQuery spQuery;
        while(Thread.currentThread().isInterrupted() == false) {
            try {
                spQuery = inputSearchPageQueue.take();
                List<String> linkList = spQuery.getLinkList();
                for(String link:linkList) {
                    outputLinkQueue.put(new ExpandedLink(link, spQuery.getInitQuery()));
                }
            } catch (InterruptedException ex) {
                log.error(classname, ExceptionUtils.getStackTrace(ex));
            }
        }
    }
    
}
