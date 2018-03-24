/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.parseProcess;

import com.apu.olxcrawler.parser.OlxAnAdvertParser;
import com.apu.olxcrawler.entity.AnAdvert;
import com.apu.olxcrawler.entity.ExpandedLink;
import com.apu.olxcrawler.parser.IllegalInputValueException;
import com.apu.olxcrawler.query.GetRequestException;
import com.apu.olxcrawler.utils.Log;
import java.util.concurrent.BlockingQueue;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author Anikeichyk Pavel
 * @email  pasha_anik@ukr.net
 * 
 */
public class OlxAnAdvertParserThread implements Runnable {
    
    private static final Log log = Log.getInstance();
    private final Class classname = OlxAnAdvertParserThread.class;

    private final BlockingQueue<ExpandedLink> inputLinkQueue;
    private final BlockingQueue<AnAdvert> outputAnAdvertQueue;

    public OlxAnAdvertParserThread(BlockingQueue<ExpandedLink> inputLinkQueue, BlockingQueue<AnAdvert> outputAnAdvertQueue) {
        this.inputLinkQueue = inputLinkQueue;
        this.outputAnAdvertQueue = outputAnAdvertQueue;
    }
    
    @Override
    public void run() {
        OlxAnAdvertParser parser;
        parser = new OlxAnAdvertParser();
        while(Thread.currentThread().isInterrupted() == false) {
            try {
                ExpandedLink link = inputLinkQueue.take();
                log.debug(classname, Thread.currentThread().getName() + " take link.");
                AnAdvert advert = parser.getAnAdvertFromLink(link);
                if(advert.getId() != null) {
                    log.debug(classname, Thread.currentThread().getName() + " put advert.");
                    outputAnAdvertQueue.put(advert);
                } else {
                    log.error(classname, Thread.currentThread().getName() + "Put advert error. Advert id is null.");
                }
            } catch (InterruptedException ex) {
                log.error(classname, ExceptionUtils.getStackTrace(ex));
            } catch (GetRequestException | IllegalInputValueException ex) {
                log.error(classname, ExceptionUtils.getStackTrace(ex));
            } catch (RuntimeException ex) {
                log.error(classname, ExceptionUtils.getStackTrace(ex));
            }
        }
    }
    
}
