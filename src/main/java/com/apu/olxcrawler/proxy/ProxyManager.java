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
    private int proxyPtr = 0;
    
    /** ProxyManager instance */
    private static ProxyManager instance;

    private ProxyManager() {
        this.proxyList = new ArrayList<>();
        this.init();
    }
    
    /**
     * Initialize ProxyManager
     */
    private void init() {
        ProxyItem item;
        item = new ProxyItem("62.109.14.242", 80);
        this.add(item);
        item = new ProxyItem("62.109.14.242", 80);
        this.add(item);
        item = new ProxyItem("176.235.11.6", 8080);        
        this.add(item);        
    }
    
    /**
     * Get ProxyManager's instance
     * @return ProxyManager instance
     */
    public static ProxyManager getInstance() {
        if(instance == null) {
            instance = new ProxyManager();
        }
        return instance;
    }
    
    /**
     * @return     current ProxyItem
     */
    public synchronized ProxyItem take() {
        ProxyItem item;
        while(true) {
            item = proxyList.get(proxyPtr);
            if(item.isValid() && (item.isUnused())) {
                item.setUsed();
                return item;
            }
            proxyPtr++;  
            if(proxyPtr >= proxyList.size()) {
                proxyPtr = 0;
                if(checkForValidProxy() == false)
                    return null;
                if(checkForUnusedProxy() == false)
                    return null;
            }                      
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
        if(item != null)
            proxyList.add(item);
    }
    
    /**
     * Checking if valid proxies exist
     * @return TRUE if exist
     */
    private boolean checkForValidProxy() {
        for(ProxyItem item:proxyList) {
            if(item.isValid())
                return true;
        }
        return false;
    }
    
    /**
     * Checking if unused proxies exist
     * @return true if exist
     */
    private boolean checkForUnusedProxy() {
        for(ProxyItem item:proxyList) {
            if(item.isUnused())
                return true;
        }
        return false;
    }
    
}
