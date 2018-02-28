/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.parseProcess;

import com.apu.olxcrawler.parser.OlxAnAdwertParser;
import com.apu.olxcrawler.entity.AnAdwert;
import com.apu.olxcrawler.utils.Log;
import java.util.concurrent.BlockingQueue;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author apu
 */
public class OlxAnAdwertParserThread implements Runnable {
    
    private static final Log log = Log.getInstance();
    private final Class classname = OlxAnAdwertParserThread.class;

    private final BlockingQueue<String> inputLinkQueue;
    private final BlockingQueue<AnAdwert> outputAnAdwertQueue;

    public OlxAnAdwertParserThread(BlockingQueue<String> inputLinkQueue, BlockingQueue<AnAdwert> outputAnAdwertQueue) {
        this.inputLinkQueue = inputLinkQueue;
        this.outputAnAdwertQueue = outputAnAdwertQueue;
    }
    
    @Override
    public void run() {
        OlxAnAdwertParser parser;
        parser = new OlxAnAdwertParser();
        while(Thread.currentThread().isInterrupted() == false) {
            try {
                String link = inputLinkQueue.take();
                log.error(classname, Thread.currentThread().getName() + " take link.");
                AnAdwert adwert = parser.getAnAdwertFromLink(link);
                log.error(classname, Thread.currentThread().getName() + " put adwert.");
                outputAnAdwertQueue.put(adwert);
            } catch (InterruptedException ex) {
                log.error(classname, ExceptionUtils.getStackTrace(ex));
            }
        }
    }
    
}
