/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.parseProcess;

import java.util.List;

/**
 *
 * @author apu
 */
public class SearchPageQuery {
    private String link;
    private List<String> linkList;

    public SearchPageQuery(String link, List<String> linkList) {
        this.link = link;
        this.linkList = linkList;
    }

    public String getLink() {
        return link;
    }

    public List<String> getLinkList() {
        return linkList;
    }
    
}
