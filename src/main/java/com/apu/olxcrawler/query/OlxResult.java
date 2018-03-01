/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.query;

import com.apu.olxcrawler.entity.CookieItemList;
import org.apache.commons.httpclient.Cookie;

/**
 *
 * @author apu
 */
public class OlxResult {
    private String content;
    private Cookie[] cookies = null;
    private CookieItemList cookieList = null;
    private String phone = null;

    public OlxResult(String content, Cookie[] cookies) {
        this.content = content;
        this.cookies = cookies;
    } 

    public String getContent() {
        return content;
    }

    public Cookie[] getCookies() {
        return cookies;
    }    

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public void setCookieList(CookieItemList cookieList) {
        this.cookieList = cookieList;
    }

    public CookieItemList getCookieList() {
        return cookieList;
    }    
    
}
