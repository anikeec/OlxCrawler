/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.proxy;

import com.apu.olxcrawler.utils.Log;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author apu
 */
public class ProxyManager {
    
    private static final Log log = Log.getInstance();
    private final Class classname = ProxyManager.class;
    
    private static final String SERVER_CHECK_PROXY = "http://www.google.com";
    private static final int SERVER_CHECK_TIMEOUT = 1000;
    private final List<ProxyItem> proxyList;
    private int proxyPtr = 0;
    private static ProxyManager instance;

    private ProxyManager() {
        this.proxyList = new ArrayList<>();
        this.init();
    }
    
    private void init() {
        ProxyItem item;
        item = new ProxyItem("176.235.11.6", 8080);        
        this.add(item);
//        item = new ProxyItem("62.109.14.242", 80);
//        this.add(item);
    }
    
    public static ProxyManager getInstance() {
        if(instance == null) {
            instance = new ProxyManager();
        }
        return instance;
    }
    
    public synchronized ProxyItem take() {
        ProxyItem item;
        do {
            if(proxyPtr >= proxyList.size()) {
                proxyPtr = 0;
                if(checkForValidProxy() == false)
                    return null;
                if(checkForUnusedProxy() == false)
                    return null;
            }
            item = proxyList.get(proxyPtr++);            
        } while((item.isValid() == false)||(item.isUsed()));
        if(ping(item)) {
            item.setUsed();
            return item;
        }
        return null;
    }
    
    public synchronized void put(ProxyItem item) {
        if(item != null)
            item.clearUsed();
    }
    
    public synchronized void add(ProxyItem item) {
        if(item != null)
            proxyList.add(item);
    }
    
    private boolean checkForValidProxy() {
        for(ProxyItem item:proxyList) {
            if(item.isValid())
                return true;
        }
        return false;
    }
    
    private boolean checkForUnusedProxy() {
        for(ProxyItem item:proxyList) {
            if(item.isUsed() == false)
                return true;
        }
        return false;
    }
    
    private boolean ping(ProxyItem item) {
//        try {
//            InetAddress address = InetAddress.getByAddress(item.getIp().getBytes());
//            log.error(classname, "ping: " + address.isReachable(200));
//            return true;
//        }
//        catch (UnknownHostException ex) { 
//            log.error(classname, ExceptionUtils.getStackTrace(ex));
//        }
//        catch (IOException ex) {
//            log.error(classname, ExceptionUtils.getStackTrace(ex));
//        }
        log.error(classname, "Check proxy: " + item.getIp() + ":" + item.getPort());
        Proxy proxy = new Proxy(Proxy.Type.HTTP, 
                new InetSocketAddress(item.getIp(), item.getPort()));
        URL url;
        try {
            url = new URL(SERVER_CHECK_PROXY);
            HttpURLConnection uc = (HttpURLConnection)url.openConnection(proxy);
            uc.setConnectTimeout(SERVER_CHECK_TIMEOUT);
            uc.setRequestMethod("GET");
            uc.connect();
            int code = uc.getResponseCode();
            StringBuilder tmp = new StringBuilder();
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String line = in.readLine();
            uc.disconnect();
            if(code == 200)
                return true;
        } catch (MalformedURLException ex) {
            log.error(classname, "Check proxy error!!!");
//            log.error(classname, ExceptionUtils.getStackTrace(ex));
        } catch (IOException ex) {
            log.error(classname, "Check proxy error!!!");   
//            log.error(classname, ExceptionUtils.getStackTrace(ex));
        }        
        return false;
    }
    
}
