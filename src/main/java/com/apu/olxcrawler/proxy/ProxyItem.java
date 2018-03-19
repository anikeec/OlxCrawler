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
    private String ip;
    private Integer port;
    private boolean valid = true;
//    private boolean used = false;
    private final int SEMAPHORE_INIT = 5;
    private int semaphore = SEMAPHORE_INIT;

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

    public boolean isValid() {
        return valid;
    }

    public void setValid(boolean valid) {
        this.valid = valid;
    }

    public boolean isUsed() {
//        return used;
        if(semaphore > 0)   return false;
        return true;
    }

//    public void setUsed(boolean used) {
//        this.used = used;
//    }
    
    public void setUsed() {
        if(semaphore > 0)
            semaphore--;
    }
    
    public void clearUsed() {
        if(semaphore < SEMAPHORE_INIT)
            semaphore++;
    }
    
}
