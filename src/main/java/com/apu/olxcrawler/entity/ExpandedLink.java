/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.entity;

/**
 *
 * @author apu
 */
public class ExpandedLink {
    private String link;
    private String initQuery;

    public ExpandedLink(String link, String initQuery) {
        this.link = link;
        this.initQuery = initQuery;
    }

    public String getLink() {
        return link;
    }

    public String getInitQuery() {
        return initQuery;
    }
    
    
    
}
