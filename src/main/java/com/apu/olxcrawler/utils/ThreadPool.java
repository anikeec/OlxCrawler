/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.utils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author apu
 */
public class ThreadPool {
    
    private final int poolSize;
    private int threadsAmount = 0;
    private final List<Thread> threads;

    public ThreadPool(int poolSize) {
        this.poolSize = poolSize;
        this.threads = new ArrayList<>();
    }
    
    public boolean submit(Runnable thread) {
        return this.submit(thread, null);
    }
    
    public boolean submit(Runnable thread, String name) {
        if(threadsAmount >= poolSize)   
            return false;
        Thread thr = new Thread(thread);
        if(name != null)
            thr.setName(name);
        threads.add(thr);
        threadsAmount++;
        thr.start();
        return true;
    }
    
    public void stop() {
        for(Thread thr:threads) {
            thr.interrupt();
        }
    }
    
}
