/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.proxy;

import com.apu.olxcrawler.query.GetRequest;
import com.apu.olxcrawler.query.GetRequestException;
import com.apu.olxcrawler.query.QueryParams;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author apu
 */
public class ProxyParser {
    
    private String parseHostFromLink(String link) {
        if(link == null)    
                return null;
        String startPattern = "//";
        int startPosition = link.indexOf(startPattern);
        if(startPosition == -1)
                return null;
        String endPattern = "/";
        int endPosition = link.indexOf(endPattern, startPosition + startPattern.length());
        if(endPosition == -1)
                return null;
        return link.substring(startPosition + startPattern.length(), endPosition);
    }
    
    List<ProxyItem> parse(String link) {
        List<ProxyItem> list = new ArrayList<>();
        try { 
            String host = parseHostFromLink(link);
            QueryParams parameters = new QueryParams();
            parameters.add(QueryParams.Parameter.URL_STR, link);
            parameters.add(QueryParams.Parameter.HOST_STR, GetRequest.PROXY_HOST);
            parameters.add(QueryParams.Parameter.ENCODING_TYPE, "windows-1251");
            parameters.add(QueryParams.Parameter.IS_PROXY, true);
            parameters.add(QueryParams.Parameter.HEADER_ENABLE, false);
            
            String content = 
                        new GetRequest().makeRequest(parameters).getContent();
            Document doc = Jsoup.parse(content);
            Element table = doc.getElementsByClass("proxy__t").first();
            if(table == null)
                return list;
            Elements rows = table.select("tr");
            
            ProxyItem proxyItem;
            
            Element row;
            Elements cols;
            String ipStr;
            String portStr;
            String delayStr;
            for (int i = 1; i < rows.size(); i++) { //first row is the col names so skip it.
                row = rows.get(i);
                cols = row.select("td");
                ipStr = cols.get(0).text();
                portStr = cols.get(1).text();
                if(portStr != null) {
                    int port = Integer.parseInt(portStr);
                    proxyItem = new ProxyItem(ipStr, port);
                    proxyItem.setCountry(cols.get(2).text());
                    delayStr = cols.get(3).text();
                    if(delayStr != null) {
                        String endPattern = " мс";
                        int ptr = delayStr.indexOf(endPattern);
                        int delay = Integer.parseInt(delayStr.substring(0, ptr));
                        proxyItem.setDelay(delay);
                    }  
                    list.add(proxyItem);
                }                
            }            
        } catch (GetRequestException ex) {
            Logger.getLogger(ProxyParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
}
