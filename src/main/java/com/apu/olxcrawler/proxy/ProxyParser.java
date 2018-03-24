/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.proxy;

import static com.apu.olxcrawler.parser.OlxParserUtils.getPatternCutOut;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.html.HtmlTable;
import com.gargoylesoftware.htmlunit.html.HtmlTableCell;
import com.gargoylesoftware.htmlunit.html.HtmlTableRow;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author apu
 */
public class ProxyParser {
    
    List<ProxyItem> parse(String link) {
        List<ProxyItem> list = new ArrayList<>();
        try {           
            final WebClient client  = new WebClient();
            HtmlPage currentPage = client.getPage(link);
            client.waitForBackgroundJavaScript(10000);
            HtmlTable table = (HtmlTable) currentPage.getByXPath("//table[@class='proxy__t']").get(0);
            ProxyItem proxyItem;
            String delay;
            if(table != null) {
                int ptr = 0;
                List<HtmlTableRow> rows = table.getRows();
                for (HtmlTableRow row : rows) {                    
                    if(ptr==0) {
                        ptr++;
                        continue;
                    }
                    proxyItem = new ProxyItem();
                    List<HtmlTableCell> cells = row.getCells();
                    
                    proxyItem.setIp(cells.get(0).asText());
                    
                    try{
                        proxyItem.setPort(Integer.parseInt(cells.get(1).asText()));
                    } catch (NumberFormatException ex) {

                    }
                    
                    proxyItem.setCountry(cells.get(2).asText());
                    
                    try{
                        delay = cells.get(3).asText();
                        if(delay != null) {
                            delay = delay.replace(" мс", "");
                            proxyItem.setDelay(Integer.parseInt(delay));
                        }
                    } catch (NumberFormatException ex) {

                    }
                    
                    list.add(proxyItem);
                    ptr++;
                }
            }
            client.close();            
        } catch (IOException ex) {
            Logger.getLogger(ProxyParser.class.getName()).log(Level.SEVERE, null, ex);
        } catch (FailingHttpStatusCodeException ex) {
            Logger.getLogger(ProxyParser.class.getName()).log(Level.SEVERE, null, ex);
        }
        return list;
    }
    
    private String getUserIdFromLink(String link) {
        if(link == null)    return null;
        String regExpUrl = "https://www\\.olx\\.ua/" + "(.*)\\-ID(.*)\\.html(.*)";    
        Pattern pattern = Pattern.compile(regExpUrl, Pattern.DOTALL);  
        
        Matcher matcher = pattern.matcher(link);        
        if(matcher.matches() == false) return null;
        
        String startPattern = "-ID";
        String endPattern = ".html";
        return getPatternCutOut(link, startPattern, endPattern);    
    }
    
    public static void main(String[] args) {
        ProxyParser parser = new ProxyParser();
        parser.parse("https://hidemy.name/ru/proxy-list/?maxtime=500&type=h#list");
        System.out.println("");
    }
    
}
