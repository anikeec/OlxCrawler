/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.query;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author apu
 */
public class QueryParams {
    
    public static enum Parameter { 
        IS_PROXY,
        ENCODING_TYPE,
        HEADER_ENABLE,
        URL_STR,
        HOST_STR,
        PREVIOUS_RESULT,
        REFERER_URL
    };
    
    private final Map<Parameter, Object> params = new ConcurrentHashMap<>();
    
    public void add(Parameter parameter, Object value) {
        params.put(parameter, value);
    }
    
    public Object get(Parameter parameter) {
        return params.get(parameter);
    }
    
    public void remove(Parameter parameter) {
        params.remove(parameter);
    }
    
}
