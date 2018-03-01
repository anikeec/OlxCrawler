/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.query;

import com.apu.olxcrawler.query.cookie.CookieItemList;

/**
 *
 * @author Anikeichyk Pavel
 * @email  pasha_anik@ukr.net
 * 
 */
public class OlxResult {
    private String content;
    private CookieItemList cookieList = null;

    public OlxResult(String content, CookieItemList cookieList) {
        this.content = content;
        this.cookieList = cookieList;
    } 

    public String getContent() {
        return content;
    } 

    public CookieItemList getCookieList() {
        return cookieList;
    }    
    
}
