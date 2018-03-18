/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author Anikeichyk Pavel
 * @email  pasha_anik@ukr.net
 * 
 */
public class Time {
    
    private static final Log log = Log.getInstance();
    private static final Class classname = Time.class;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
    
    public static String getTime() {        
        return dateFormat.format(new Date());
    }
    
    public static String timeToDateString(long time) {
        return dateFormat.format(new Date(time));
    }
    
    public static Date timeStrToDate(String time) {
        try {
            return dateFormat.parse(time);
        } catch (ParseException ex) {
            log.debug(classname,ExceptionUtils.getStackTrace(ex));
        }
        return null;
    }
    
    public static java.sql.Time timeStrToTime(String time) {
        try {
            return new java.sql.Time(dateFormat.parse(time).getTime());
        } catch (ParseException ex) {
            log.debug(classname,ExceptionUtils.getStackTrace(ex));
        }
        return null;
    }
    
}
