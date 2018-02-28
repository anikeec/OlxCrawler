/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.parseProcess;

import com.apu.olxcrawler.utils.Log;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author apu
 */
public class OlxSearchLListToAdAdwertLThread implements Runnable {
    
    private static final Log log = Log.getInstance();
    private final Class classname = OlxSearchLListToAdAdwertLThread.class;

    private final BlockingQueue<SearchPageQuery> inputSearchPageQueue;
    private final BlockingQueue<String> outputLinkQueue;

    public OlxSearchLListToAdAdwertLThread(
                            BlockingQueue<SearchPageQuery> inputSearchPageQueue, 
                            BlockingQueue<String> outputLinkQueue) {
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
                    outputLinkQueue.put(link);
                }
            } catch (InterruptedException ex) {
                log.error(classname, ExceptionUtils.getStackTrace(ex));
            }
        }
    }
    
}
