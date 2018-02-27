/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler;

import com.apu.olxcrawler.entity.AnAdwert;
import com.apu.olxcrawler.utils.Log;
import java.util.concurrent.BlockingQueue;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author apu
 */
public class OlxParserThread implements Runnable {
    
    private static final Log log = Log.getInstance();
    private final Class classname = OlxParserThread.class;

    private final BlockingQueue<String> inputLinkQueue;
    private final BlockingQueue<AnAdwert> outputAnAdwertQueue;

    public OlxParserThread(BlockingQueue<String> inputLinkQueue, BlockingQueue<AnAdwert> outputAnAdwertQueue) {
        this.inputLinkQueue = inputLinkQueue;
        this.outputAnAdwertQueue = outputAnAdwertQueue;
    }
    
    @Override
    public void run() {
        OlxParser parser;
        parser = new OlxParser();
        while(Thread.currentThread().isInterrupted() == false) {
            try {
                String link = inputLinkQueue.take();                
                AnAdwert adwert = parser.getAnAdwertFromLink(link);
                outputAnAdwertQueue.put(adwert);
            } catch (InterruptedException ex) {
                log.error(classname, ExceptionUtils.getStackTrace(ex));
            }
        }
    }
    
}
