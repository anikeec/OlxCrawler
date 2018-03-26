/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.parseProcess;

import com.apu.olxcrawler.entity.AnAdvert;
import com.apu.olxcrawler.query.QueryResult;

/**
 *
 * @author apu
 */
public class PhoneNumberQuery {
    private AnAdvert anAdvert;
    private QueryResult previousQueryResult;

    public PhoneNumberQuery(AnAdvert anAdvert, QueryResult previousQueryResult) {
        this.anAdvert = anAdvert;
        this.previousQueryResult = previousQueryResult;
    }

    public AnAdvert getAnAdvert() {
        return anAdvert;
    }

    public void setAnAdvert(AnAdvert anAdvert) {
        this.anAdvert = anAdvert;
    }

    public QueryResult getPreviousQueryResult() {
        return previousQueryResult;
    }

    public void setPreviousQueryResult(QueryResult previousQueryResult) {
        this.previousQueryResult = previousQueryResult;
    }
    
    
}
