/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.mainlogic;

import com.apu.olxcrawler.entity.AnAdvert;
import com.apu.olxcrawler.parseProcess.PhoneNumberQuery;
import com.apu.olxcrawler.utils.Log;
import java.util.concurrent.BlockingQueue;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author apu
 */
public class AnAdvertKeeperThread extends Thread {
    
    private static final Log log = Log.getInstance();
    private final Class classname = AnAdvertKeeperThread.class;
    
    BlockingQueue<AnAdvert> inputAnAdvertQueue;
    private final BlockingQueue<PhoneNumberQuery> outputQueryQueue;

    public AnAdvertKeeperThread(BlockingQueue<AnAdvert> inputAnAdvertQueue,
                        BlockingQueue<PhoneNumberQuery> outputQueryQueue) {
        this.inputAnAdvertQueue = inputAnAdvertQueue;
        this.outputQueryQueue = outputQueryQueue;
    }

    @Override
    public void run() {
        AnAdvertKeeper keeper = new AnAdvertKeeper();
        AnAdvert advert;
        PhoneNumberQuery phoneQuery;
        int querySize = 0;
        while(!Thread.currentThread().isInterrupted()) {
            try {
                advert = inputAnAdvertQueue.take();
                if(querySize != inputAnAdvertQueue.size()) {
                    querySize = inputAnAdvertQueue.size();
                    log.info(classname, "AnAdvertKeeperQueue - amount of data: " + querySize);
                }
                phoneQuery = keeper.keepAnAdvert(advert);
                if(phoneQuery != null) {
                    try {
                        outputQueryQueue.put(phoneQuery);
                    } catch (InterruptedException ex) {
                        log.error(classname, ExceptionUtils.getStackTrace(ex));
                    }
                }
                log.debug(classname, "Save to DB advert: " + advert.getId());
            } catch (InterruptedException ex) {
                log.error(classname, ExceptionUtils.getStackTrace(ex));
            }
        }
    }
    
}
