/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.query;

import org.apache.commons.httpclient.HttpClient;

/**
 *
 * @author apu
 */
public class HttpClientItem {
    private int id;
    private HttpClient httpClient;

    public HttpClientItem(int id, HttpClient httpClient) {
        this.id = id;
        this.httpClient = httpClient;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }
    
}
