/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.mainlogic;

import com.apu.olxcrawler.entity.AnAdvert;
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

    public AnAdvertKeeperThread(BlockingQueue<AnAdvert> inputAnAdvertQueue) {
        this.inputAnAdvertQueue = inputAnAdvertQueue;
    }

    @Override
    public void run() {
        AnAdvertKeeper keeper = new AnAdvertKeeper();
        AnAdvert advert;
        while(!Thread.currentThread().isInterrupted()) {
            try {
                advert = inputAnAdvertQueue.take();
                keeper.keepAnAdvert(advert);
                log.error(classname, "Save to DB advert: " + advert.getId());
            } catch (InterruptedException ex) {
                log.error(classname, ExceptionUtils.getStackTrace(ex));
            }
        }
    }
    
}
