/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.entity;

/**
 *
 * @author Anikeichyk Pavel
 * @email  pasha_anik@ukr.net
 * 
 */
public class ExpandedLink {
    private String link;
    private String initQuery;
    private String category;

    public ExpandedLink() {
        this(null);
    }
    
    public ExpandedLink(String link) {
        this(link, null);
    }
    
    public ExpandedLink(String link, String initQuery) {
        this(link, initQuery, null);
    }

    public ExpandedLink(String link, String initQuery, String category) {
        this.link = link;
        this.initQuery = initQuery;
        this.category = category;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getInitQuery() {
        return initQuery;
    }

    public void setInitQuery(String initQuery) {
        this.initQuery = initQuery;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }
    
}
