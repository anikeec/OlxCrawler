/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler;

import com.apu.olxcrawler.entity.AnAdwert;
import com.apu.olxcrawler.query.ConnectionManager;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author apu
 */
public class OlxParsersPool {    
    
    private static final int THREAD_POOL_SIZE = 40;
    
    private final List<Thread> threadList;
    private final BlockingQueue<String> inputLinkQueue;
    private final BlockingQueue<AnAdwert> outputAnAdwertQueue;

    public OlxParsersPool(BlockingQueue<String> inputLinkQueue, 
                            BlockingQueue<AnAdwert> outputAnAdwertQueue) {
        this.threadList = new ArrayList<>();
        this.inputLinkQueue = inputLinkQueue;
        this.outputAnAdwertQueue = outputAnAdwertQueue;
    }
    
    public void init() {
        OlxParserThread olxThread;
        Thread thread;
        for(int i=0; i<THREAD_POOL_SIZE; i++) {
            olxThread = new OlxParserThread(inputLinkQueue, outputAnAdwertQueue);
            thread = new Thread(olxThread);
            thread.setDaemon(true);
            threadList.add(thread);
            thread.start();
        }
    }
    
}
