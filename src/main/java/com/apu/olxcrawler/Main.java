/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler;

import com.apu.olxcrawler.entity.AnAdvert;
import com.apu.olxcrawler.entity.ExpandedLink;
import java.util.List;

/**
 *
 * @author apu
 */
public class Main {
    
    public static void main(String[] args) {
        Crawler crawler = new Crawler();
        crawler.init();
        ExpandedLink link = new ExpandedLink();
        link.setInitQuery("Операционные  системы");
        link.setCategory(OlxCategory.hobbi_otdyh_i_sport);
        List<AnAdvert> list = crawler.getAdvertsByQueryStr(link);
        System.out.println("Ready");
    }   
    
}
