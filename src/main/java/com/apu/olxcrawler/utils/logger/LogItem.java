/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.utils.logger;

/**
 *
 * @author apu
 */
public class LogItem {
    
    private LogType type;
    private String value;

    public LogItem(LogType type, String value) {
        this.type = type;
        this.value = value;
    }

    public LogType getType() {
        return type;
    }

    public void setType(LogType type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
    
}
