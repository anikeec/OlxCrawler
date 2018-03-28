/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.query;

import com.apu.olxcrawler.proxy.ProxyItem;
import com.apu.olxcrawler.query.cookie.CookieItemList;
import com.apu.olxcrawler.utils.Log;
import java.util.Date;

/**
 *
 * @author Anikeichyk Pavel
 * @email  pasha_anik@ukr.net
 * 
 */
public class QueryResult {
    
    private static final Log log = Log.getInstance();
    private final Class classname = QueryResult.class;
    
    private String content;
    private CookieItemList cookieList = null;
    private ProxyItem usedProxy = null;

    public QueryResult(String content, CookieItemList cookieList) {
        this(content, cookieList, null);
    } 

    public QueryResult(String content, CookieItemList cookieList, ProxyItem usedProxy) {
        this.content = content;
        this.cookieList = cookieList;
        this.usedProxy = usedProxy;
    }    

    public String getContent() {
        return content;
    } 

    public CookieItemList getCookieList() {
        return cookieList;
    }   
    
    public void setValid() {
        if(usedProxy == null)
            return;
        usedProxy.setValid();
        usedProxy.setTimeBecameInvalid(null);
    }
    
    public void setInvalid() {
        if(usedProxy == null)
            return;        
        usedProxy.setInvalid();
        usedProxy.setTimeBecameInvalid(new Date());
    }
    
}
