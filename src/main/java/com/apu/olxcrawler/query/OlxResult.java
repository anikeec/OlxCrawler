/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.query;

import org.apache.http.impl.client.BasicCookieStore;

/**
 *
 * @author apu
 */
public class OlxResult {
    private String content;
    private BasicCookieStore cookieStore;

    public OlxResult(String content, BasicCookieStore cookieStore) {
        this.content = content;
        this.cookieStore = cookieStore;
    }

    public String getContent() {
        return content;
    }

    public BasicCookieStore getCookieStore() {
        return cookieStore;
    }
    
    
}
