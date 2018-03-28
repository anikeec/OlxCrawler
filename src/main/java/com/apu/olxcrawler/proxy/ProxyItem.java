/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.proxy;

import com.apu.olxcrawler.utils.Log;
import static com.google.common.base.Preconditions.checkNotNull;
import java.util.Date;

/**
 *
 * @author apu
 */
public class ProxyItem {
    
    private static final Log log = Log.getInstance();
    private final Class classname = ProxyItem.class;
    
    /** Proxy server IP address */
    private String ip;
    
    /** Proxy server port number */
    private Integer port;
    
    /** Proxy-server country */
    private String country;
    
    /** Proxy delay, ms */
    private Integer delay;
    
    /** Amount of clients who can use this proxy concurrently */
    private final int USED_SEMAPHORE_INIT = 5;
    private volatile int usedSemaphore = USED_SEMAPHORE_INIT;
    
    /** Amount of tries this proxy usage */
    private final int VALID_SEMAPHORE_INIT = 5;    
    private volatile int validSemaphore = VALID_SEMAPHORE_INIT;
    
    private Date timeBecameInvalid; 

    public ProxyItem(String ip, Integer port) {
        checkNotNull(ip);
        checkNotNull(port);
        this.ip = ip;
        this.port = port;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        checkNotNull(ip);
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        checkNotNull(port);
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
        log.info(classname, "Proxy " + this + " is valid. N = " + validSemaphore);
    }
    
    public synchronized void setInvalid() {        
        if(validSemaphore > 0)
            validSemaphore--;
        log.info(classname, "Proxy " + this + " is invalid. N = " + validSemaphore);
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

    public synchronized Date getTimeBecameInvalid() {
        return timeBecameInvalid;
    }

    public synchronized void setTimeBecameInvalid(Date timeBecameInvalid) {
        this.timeBecameInvalid = timeBecameInvalid;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Integer getDelay() {
        return delay;
    }

    public void setDelay(Integer delay) {
        this.delay = delay;
    }
    
    
    
}
