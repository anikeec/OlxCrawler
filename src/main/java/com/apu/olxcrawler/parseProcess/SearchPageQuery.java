/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.parseProcess;

import java.util.List;

/**
 *
 * @author Anikeichyk Pavel
 * @email  pasha_anik@ukr.net
 * 
 */
public class SearchPageQuery {
    private String link;
    private List<String> linkList;
    private String initQuery;

    public SearchPageQuery(String link, List<String> linkList, String initQuery) {
        this.link = link;
        this.linkList = linkList;
        this.initQuery = initQuery;
    }

    public String getLink() {
        return link;
    }

    public List<String> getLinkList() {
        return linkList;
    }

    public String getInitQuery() {
        return initQuery;
    }
    
}
