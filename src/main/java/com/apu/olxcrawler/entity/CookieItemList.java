/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.entity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author apu
 */
public class CookieItemList {
    private List<CookieItem> cookieList = new ArrayList<>();
    
    public void add(CookieItem item) {
        cookieList.add(item);
    }

    public List<CookieItem> getCookieList() {
        return cookieList;
    }
    
}
