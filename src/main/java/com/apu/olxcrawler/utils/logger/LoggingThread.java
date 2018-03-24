/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.utils.logger;

import java.util.concurrent.BlockingQueue;
import org.apache.log4j.Logger;

/**
 *
 * @author apu
 */
public class LoggingThread extends Thread {

    static Logger logger = Logger.getLogger(Logger.class);
    
    private final BlockingQueue<LogItem> logQueue;

    public LoggingThread(BlockingQueue<LogItem> logQueue) {
        this.logQueue = logQueue;
        this.init();
    }
    
    private void init() {
        this.setDaemon(true);
        this.setName("LoggingThread");
    }
    
    @Override
    public void run() {
        while(Thread.currentThread().isInterrupted() == false) {
            try {
                LogItem logItem = logQueue.take();
                switch(logItem.getType()) {
                    case DEBUG:
                                logger.debug(logItem.getValue());
                                break;
                    case INFO:
                                logger.info(logItem.getValue());
                                break;
                    case WARN:
                                logger.warn(logItem.getValue());
                                break;
                    case ERROR:
                                logger.error(logItem.getValue());
                                break;
                    case TRACE:
                                logger.trace(logItem.getValue());
                                break;
                    default:
                                break;
                }
            } catch (InterruptedException ex) {
                logger.error(LoggingThread.class.getName(), ex);
            }
        }
    }
    
    
}
