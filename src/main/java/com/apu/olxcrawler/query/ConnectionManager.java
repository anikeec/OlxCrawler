/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.query;

import com.apu.olxcrawler.utils.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author apu
 */
public class ConnectionManager {
    
    private static final Log log = Log.getInstance();
    private final Class classname = ConnectionManager.class;
    
    private static final int CONN_MANAGERS_AMOUNT_MAX = 10;
    private static final int MAX_CONNECTION_PER_HOST = 1;
    
    private final List<MultiThreadedHttpConnectionManager> cmList;
    private final BlockingQueue<HttpClientItem> clientQueue;
    
    private static ConnectionManager instance;    

    public ConnectionManager() {
        this.cmList = new ArrayList<>();
        this.clientQueue = 
            new LinkedBlockingQueue<>(MAX_CONNECTION_PER_HOST * CONN_MANAGERS_AMOUNT_MAX);
        int counter = 0;
        for(int i=0; i<CONN_MANAGERS_AMOUNT_MAX; i++) {
            MultiThreadedHttpConnectionManager cm = 
                                new MultiThreadedHttpConnectionManager();
            cm.getParams().setDefaultMaxConnectionsPerHost(MAX_CONNECTION_PER_HOST);
            cmList.add(cm);
            HttpClient client = new HttpClient(cm);
            for(int j=0; j<MAX_CONNECTION_PER_HOST; j++) {
                try {
                    clientQueue.put(new HttpClientItem(counter, client));
                    counter++;
                } catch (InterruptedException ex) {
                    log.error(classname, ExceptionUtils.getStackTrace(ex));
                }
            }
        }
    }
    
    public static ConnectionManager getInstance() {
        if(instance == null)
            instance = new ConnectionManager();
        return instance;
    }
    
    public HttpClientItem getClient() {       
        try {
            return clientQueue.take();
        } catch (InterruptedException ex) {
            log.error(classname, ExceptionUtils.getStackTrace(ex));
        }
        return null;
    }
    
    public void putClient(HttpClientItem client) {
        try {
            clientQueue.put(client);
        } catch (InterruptedException ex) {
            log.error(classname, ExceptionUtils.getStackTrace(ex));
        }
    }
    
}
