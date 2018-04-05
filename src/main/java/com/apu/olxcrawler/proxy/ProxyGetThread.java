/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.proxy;

import com.apu.olxcrawler.utils.Log;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author apu
 */
public class ProxyGetThread extends Thread {
    
    private static final Log log = Log.getInstance();
    private final Class classname = ProxyGetThread.class;
    
    private final long PROXY_POLLING_TIMEOUT = 10*60*1000;
    private final List<String> proxyUrlList = new ArrayList<>();
    private volatile boolean queryActive = false;

    public void init() {
        proxyUrlList.add("https://hidemy.name/ru/proxy-list/?maxtime=1000&ports=3128&type=h#list");
        this.setName("ProxyGetThread");
        this.setDaemon(true);
    }

    public synchronized boolean isQueryActive() {
        return queryActive;
    }
    
    @Override
    public void run() {
        ProxyManager proxyManager = ProxyManager.getInstance();
        ProxyParser parser = new ProxyParser();
        while(true) {
            for(String proxyUrl:proxyUrlList) {
                queryActive = true;
                try {                
//                String content = 
//                        new GetRequest()
//                                .makeRequest(PROXY_URL, GetRequest.OLX_HOST)
//                                .getContent();                
                    proxyManager.add(parser.parse(proxyUrl));
                    queryActive = false;
                    Thread.sleep(PROXY_POLLING_TIMEOUT);                              
                } catch (InterruptedException ex) {
//                
//            } catch (GetRequestException ex) {
//                log.error(classname, ExceptionUtils.getStackTrace(ex));
                }                
            }
        }
    }
    
}
