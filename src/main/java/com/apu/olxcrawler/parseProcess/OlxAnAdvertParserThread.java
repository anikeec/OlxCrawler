/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.parseProcess;

import com.apu.olxcrawler.parser.OlxAnAdvertParser;
import com.apu.olxcrawler.entity.AnAdvert;
import com.apu.olxcrawler.entity.ExpandedLink;
import com.apu.olxcrawler.mainlogic.AnAdvertKeeper;
import com.apu.olxcrawler.parser.IllegalInputValueException;
import com.apu.olxcrawler.query.GetRequestException;
import com.apu.olxcrawler.utils.Log;
import java.util.ArrayList;
import java.util.List;
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
    private final long GET_AN_ADVERT_QUERY_TIMEOUT = 500;
    private final int ADVERT_QUERY_TRY_COUNTER_MAX = 10;

    public OlxAnAdvertParserThread(BlockingQueue<ExpandedLink> inputLinkQueue, 
                        BlockingQueue<AnAdvert> outputAnAdvertQueue) {
        this.inputLinkQueue = inputLinkQueue;
        this.outputAnAdvertQueue = outputAnAdvertQueue;
    }
    
    @Override
    public void run() {
        OlxAnAdvertParser parser;
        parser = new OlxAnAdvertParser();
        int querySize = 0;
        ExpandedLink link = null;
        AnAdvertKeeper anAdvertKeeper = new AnAdvertKeeper();
        
        while(Thread.currentThread().isInterrupted() == false) {
            try {
                link = inputLinkQueue.take();
            } catch (InterruptedException ex) {
                log.error(classname, ExceptionUtils.getStackTrace(ex));
            }

            int counter = 0;
            List<String> errorList = new ArrayList<>();
            do{            
                try {                
                    log.info(classname, "Input searchOutputLinkQueue: " + inputLinkQueue.size());
                    log.debug(classname, Thread.currentThread().getName() + " take link.");
                    
                    //check if link is exist in DB
                    if(anAdvertKeeper.isLinkExistInDB(link)) {
                        break;
                    }
                    //check if phone is exist in this advert
                    
                    AnAdvert advert = parser.getAnAdvertFromLink(link);
                    if(advert.getId() != null) {
                        log.debug(classname, Thread.currentThread().getName() + " put advert.");
                        log.info(classname, "Output anAdvertParserOutputQueue: " + outputAnAdvertQueue.size());
                        outputAnAdvertQueue.put(advert);
                        Thread.sleep(GET_AN_ADVERT_QUERY_TIMEOUT);
                        break;
                    } else {
                        errorList.add(Thread.currentThread().getName() + "Put advert error. Advert id is null.");
                    }
                } catch (InterruptedException ex) {
                    errorList.add(ExceptionUtils.getStackTrace(ex));
                } catch (GetRequestException | IllegalInputValueException ex) {
                    errorList.add(ExceptionUtils.getStackTrace(ex));
                    if(link != null)
                        errorList.add("Error link: " + link.getLink());
                    else
                        errorList.add("Error link: link is NULL");
                } catch (RuntimeException ex) {
                    errorList.add(ExceptionUtils.getStackTrace(ex));
                }
                counter++;
            } while(counter < ADVERT_QUERY_TRY_COUNTER_MAX);
            
            if(errorList.size()>0) {
                log.error(classname, "Error list size = " + errorList.size());
                for(String error:errorList) {
                    log.error(classname, error);
                }
            }
        }
    }
    
}
