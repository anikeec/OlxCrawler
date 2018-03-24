/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.proxy;

import com.apu.olxcrawler.utils.Log;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author apu
 */
public class ProxyManager {
    
    private static final Log log = Log.getInstance();
    private final Class classname = ProxyManager.class;

    /** List of available proxies */
    private final List<ProxyItem> proxyList;
    
    /** Pointer to currently used proxy */
    private volatile int proxyPtr = 0;
    
    /** ProxyManager instance */
    private static ProxyManager instance;

    public ProxyManager() {
        this.proxyList = new ArrayList<>();        
    }
    
    /**
     * Initialize ProxyManager
     */
    public void init() {
        ProxyItem item;
        item = new ProxyItem("92.222.207.228", 3128);
        this.add(item);
        item = new ProxyItem("37.17.177.197", 3128);
        this.add(item);
        item = new ProxyItem("163.172.217.103", 3128);
        this.add(item);
        item = new ProxyItem("145.239.92.106", 3128);
        this.add(item);
        item = new ProxyItem("5.9.78.89", 3128);
        this.add(item);
        item = new ProxyItem("80.211.13.152", 3128);
        this.add(item);
        item = new ProxyItem("207.154.230.96", 3128);
        this.add(item);
        item = new ProxyItem("194.182.64.67", 3128);
        this.add(item);
        item = new ProxyItem("194.88.105.156", 3128);
        this.add(item);
        item = new ProxyItem("52.138.203.110", 3128);
        this.add(item);
//        item = new ProxyItem("176.235.11.6", 8080);        
//        this.add(item);        
    }
    
    /**
     * Get ProxyManager's instance
     * @return ProxyManager instance
     */
    public static ProxyManager getInstance() {
        return instance;
    }

    /**
     * Set ProxyManager's instance
     * @param instance
     */
    public static void setInstance(ProxyManager instance) {
        ProxyManager.instance = instance;
    }   
    
    /**
     * @return     current ProxyItem
     */
    public synchronized ProxyItem take() {
        ProxyItem item;
        if(proxyList.isEmpty())   
                return null;
        while(true) {            
            item = proxyList.get(proxyPtr);
            if(item.isValid() && (item.isUnused())) {
                item.setUsed();                
            } else {
                item = null;
                if(proxyPtr == 0) {
                    if(checkForValidUnusedProxy() == false) {
                        return null;
                    }
                }
            }
            proxyPtr++;  
            if(proxyPtr >= proxyList.size()) {
                proxyPtr = 0;                
            } 
            if(item != null)
                return item;
        }
    }
    
    /**
     * return ProxyItem back to ProxyManager
     * 
     * @param item
     */
    public synchronized void put(ProxyItem item) {
        if(item != null)
            item.clearUsed();
    }
    
    /**
     * Add new ProxyItem to ProxyManager
     *
     * @param item
     */
    public synchronized void add(ProxyItem item) {
        if((item != null) && (item.getIp() != null) && (item.getPort() != null)) {
            if(item.getPort() != 3128)  return;
            boolean saveEnable = true;
            for(ProxyItem savedItem:proxyList) {
                if((item.getIp().equals(savedItem.getIp())) && 
                   (item.getPort().intValue() == savedItem.getPort())){
                    saveEnable = false;
                    break;
                }
            }
            if(saveEnable) {
                proxyList.add(item);
            }
        }            
    }
    
    /**
     * Get amount of proxies
     *
     * @return size
     */
    public synchronized int getProxyListSize() {
        return proxyList.size();
    }
    
    /**
     * Add new ProxyItem to ProxyManager
     *
     * @param itemList
     */
    public synchronized void add(List<ProxyItem> itemList) {
        if(itemList != null) {
            if(proxyList.isEmpty()) {
                proxyList.addAll(itemList);
                return;
            }
            for(ProxyItem item:itemList) {
                this.add(item);
            }
        }
    }
    
    /**
     * Checking if valid proxies exist
     * @return TRUE if exist
     */
    private boolean checkForValidUnusedProxy() {
        for(ProxyItem item:proxyList) {
            if(item.isValid()&&item.isUnused())
                return true;
        }
        return false;
    }
    
//    /**
//     * Checking if unused proxies exist
//     * @return true if exist
//     */
//    private boolean checkForProxy() {
//        for(ProxyItem item:proxyList) {
//            if(item.isUnused())
//                return true;
//        }
//        return false;
//    }
    
    private int getAllProxyAmount() {
        return proxyList.size();
    }
    
    private int getValidProxyAmount() {
        int amount = 0;
        for(ProxyItem item:proxyList) {
            if(item.isValid())
                amount++;
        }
        return amount;
    }
    
    private int getValidUnusedProxyAmount() {
        int amount = 0;
        for(ProxyItem item:proxyList) {
            if(item.isValid())
                amount++;
        }
        return amount;
    }
    
    public synchronized String getProxyInfo() {
        return "Amount of proxies - all: " + getAllProxyAmount() +
                ", valid: " + getValidProxyAmount() +
                ", valid&unused: " + getValidUnusedProxyAmount() +
                ", ptr: " + this.proxyPtr;
    }
    
}
