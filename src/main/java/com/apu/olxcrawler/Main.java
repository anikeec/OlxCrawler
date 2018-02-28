/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler;

import com.apu.olxcrawler.entity.AnAdwert;
import java.util.List;

/**
 *
 * @author apu
 */
public class Main {
    
    public static void main(String[] args) {
        Crawler crawler = new Crawler();
        crawler.init();
        List<AnAdwert> list = crawler.getAdwertsByQueryStr("Операционные  системы");
        System.out.println("Ready");
    }   
    
}
