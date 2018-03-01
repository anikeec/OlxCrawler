/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.parser;

/**
 *
 * @author Anikeichyk Pavel
 * @email  pasha_anik@ukr.net
 * 
 */
public class Pointer {
    private Integer value = 0;

    public Integer get() {
        return value;
    }

    public void set(Integer value) {
        this.value = value;
    }
    
    public void add(Integer add) {
        this.value += add;
    }
    
}
