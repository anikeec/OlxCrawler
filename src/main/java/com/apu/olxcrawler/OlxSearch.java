/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler;

import com.apu.olxcrawler.entity.ExpandedLink;
import com.apu.olxcrawler.parser.OlxSearchParser;
import com.apu.olxcrawler.query.OlxRequest;
import com.apu.olxcrawler.utils.Log;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author apu
 */
public class OlxSearch {
    
    private static final Log log = Log.getInstance();
    private static final Class classname = OlxSearch.class;
    
    private final String OLX_SEARCH_URL = "list/q-";
    
    public List<ExpandedLink> getLinkListBySearchQuery(String searchStr) {
        try {
            String searchStrEncoded = URLEncoder.encode(searchStr, "utf-8");
            searchStrEncoded = OlxVariables.OLX_HOST_URL +
                    OLX_SEARCH_URL +
                    formatSearchStr(searchStrEncoded) +
                    "/";
            return getLinkListBySearchPageLink(searchStrEncoded, searchStr);
        } catch (UnsupportedEncodingException ex) {
            log.error(classname, ExceptionUtils.getStackTrace(ex));
        }
        return null;
    }
    
    private List<ExpandedLink> getLinkListBySearchPageLink(String link, String searchStr) {
        OlxSearchParser searchParser = new OlxSearchParser();
        String searchContent = getRequest(link);
        Integer amountOfPages = 
                searchParser.getAmountOfPagesFromContent(searchContent);
        List<ExpandedLink> list = new ArrayList<>();
        list.add(new ExpandedLink(link, searchStr));
        if(amountOfPages != null) {
            for(int i=2; i<(amountOfPages + 1); i++) {
                String resultLink = link + "?page=" + i;
                list.add(new ExpandedLink(resultLink, searchStr));
            }
        }
        return list;
    }
    
    public String find(String searchStr, String region) throws Exception {
        throw new Exception("Not realized yet");
    }
    
    private String formatSearchStr(String searchStr) {
        Pattern CLEAR_PATTERN = Pattern.compile("[\\s]+");
        return CLEAR_PATTERN.matcher(searchStr).replaceAll("-").trim();
    }
    
    private String getRequest(String queryStr) {         
        return new OlxRequest().makeRequest(queryStr).getContent();
    }
    
}
