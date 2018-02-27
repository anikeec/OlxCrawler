/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.query;

import org.apache.commons.httpclient.HostConfiguration;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.MultiThreadedHttpConnectionManager;

/**
 *
 * @author apu
 */
public class ConnectionManager {
    
    public static final int MAX_CONNECTION_PER_HOST = 18;
    
    private static ConnectionManager instance;    
    private MultiThreadedHttpConnectionManager connectionManager;
    private HttpClient httpClient;

    public ConnectionManager() {
        this.connectionManager = new MultiThreadedHttpConnectionManager();
        this.httpClient = new HttpClient(connectionManager);
        int maxConnPerHost = connectionManager.getParams().getMaxConnectionsPerHost(HostConfiguration.ANY_HOST_CONFIGURATION);
        int maxConnTotal = connectionManager.getParams().getMaxTotalConnections();
        connectionManager.getParams().setDefaultMaxConnectionsPerHost(MAX_CONNECTION_PER_HOST);
        connectionManager.getParams().setMaxConnectionsPerHost(HostConfiguration.ANY_HOST_CONFIGURATION, MAX_CONNECTION_PER_HOST);
        
    }
    
    public static ConnectionManager getInstance() {
        if(instance == null)
            instance = new ConnectionManager();
        return instance;
    }
    
    public HttpClient getClient() {       
        return httpClient;
    }
    
}
