/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.parseProcess;

import com.apu.olxcrawler.entity.AnAdvert;
import com.apu.olxcrawler.entity.ExpandedLink;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author apu
 */
public class OlxAnAdvertParserPool {    
    
    private static final int THREAD_POOL_SIZE = 5;
    
    private final List<Thread> threadList;
    private final BlockingQueue<ExpandedLink> inputLinkQueue;
    private final BlockingQueue<AnAdvert> outputAnAdvertQueue;

    public OlxAnAdvertParserPool(BlockingQueue<ExpandedLink> inputLinkQueue, 
                            BlockingQueue<AnAdvert> outputAnAdvertQueue) {
        this.threadList = new ArrayList<>();
        this.inputLinkQueue = inputLinkQueue;
        this.outputAnAdvertQueue = outputAnAdvertQueue;
    }
    
    public void init() {
        OlxAnAdvertParserThread olxThread;
        Thread thread;
        for(int i=0; i<THREAD_POOL_SIZE; i++) {
            olxThread = new OlxAnAdvertParserThread(inputLinkQueue, outputAnAdvertQueue);
            thread = new Thread(olxThread);
            thread.setDaemon(true);
            thread.setName("OlxAnAdvertParserThread " + i);
            threadList.add(thread);
            thread.start();
        }
    }
    
}
