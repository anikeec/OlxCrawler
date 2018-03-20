/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.proxy;

/**
 *
 * @author apu
 */
public class ProxyItem {
    
    /** Proxy server IP address */
    private String ip;
    
    /** Proxy server port number */
    private Integer port;
    
    /** Amount of clients who can use this proxy concurrently */
    private final int USED_SEMAPHORE_INIT = 5;
    private volatile int usedSemaphore = USED_SEMAPHORE_INIT;
    
    /** Amount of tries this proxy usage */
    private final int VALID_SEMAPHORE_INIT = 2;    
    private volatile int validSemaphore = VALID_SEMAPHORE_INIT;

    public ProxyItem(String ip, Integer port) {
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public synchronized boolean isValid() {
        if(validSemaphore > 0)
            return true;
        return false;
    }

    public synchronized void setValid() {
        if(validSemaphore < VALID_SEMAPHORE_INIT)
            validSemaphore++;
    }
    
    public synchronized void setInvalid() {
        if(validSemaphore > 0)
            validSemaphore--;
    }

    public synchronized boolean isUnused() {
        if(usedSemaphore > 0)   return true;
        return false;
    }
    
    public synchronized void setUsed() {
        if(usedSemaphore > 0)
            usedSemaphore--;
    }
    
    public synchronized void clearUsed() {
        if(usedSemaphore < USED_SEMAPHORE_INIT)
            usedSemaphore++;
    }
    
}
