/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.parseProcess;

import com.apu.olxcrawler.entity.ExpandedLink;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author Anikeichyk Pavel
 * @email  pasha_anik@ukr.net
 * 
 */
public class OlxSearchPageParserPool {    
    
    private static final int THREAD_POOL_SIZE = 1;
    
    private final List<Thread> threadList;
    private final BlockingQueue<ExpandedLink> inputLinkQueue;
    private final BlockingQueue<SearchPageQuery> outputSearchPageQueue;

    public OlxSearchPageParserPool(BlockingQueue<ExpandedLink> inputLinkQueue, 
                            BlockingQueue<SearchPageQuery> outputSearchPageQueue) {
        this.threadList = new ArrayList<>();
        this.inputLinkQueue = inputLinkQueue;
        this.outputSearchPageQueue = outputSearchPageQueue;
    }
    
    public void init() {
        OlxSearchPageParserThread olxThread;
        Thread thread;
        for(int i=0; i<THREAD_POOL_SIZE; i++) {
            olxThread = 
                new OlxSearchPageParserThread(inputLinkQueue, outputSearchPageQueue);
            thread = new Thread(olxThread);
            thread.setDaemon(true);
            thread.setName("OlxSearchPageParserThread " + i);
            threadList.add(thread);
            thread.start();
        }
    }
    
}
