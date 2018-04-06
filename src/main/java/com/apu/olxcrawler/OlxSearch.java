/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler;

import com.apu.olxcrawler.entity.ExpandedLink;
import com.apu.olxcrawler.parser.IllegalInputValueException;
import com.apu.olxcrawler.parser.OlxSearchParser;
import com.apu.olxcrawler.query.GetRequestException;
import com.apu.olxcrawler.query.GetRequest;
import com.apu.olxcrawler.query.QueryParams;
import com.apu.olxcrawler.query.QueryResult;
import com.apu.olxcrawler.utils.DataChecker;
import com.apu.olxcrawler.utils.Log;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author Anikeichyk Pavel
 * @email  pasha_anik@ukr.net
 * 
 */
public class OlxSearch {
    
    private static final Log log = Log.getInstance();
    private static final Class classname = OlxSearch.class;
    
    private final String OLX_SEARCH_URL = "list/q-";
    
    public List<ExpandedLink> getLinkListBySearchQuery(ExpandedLink searchStr) 
            throws IllegalInputValueException {
        DataChecker.nullCheck(searchStr, "searchStr");
        try {
            String searchStrEncoded = URLEncoder.encode(searchStr.getInitQuery(), "utf-8");
            String category;
            if(searchStr.getCategory() == null) {
                category = OLX_SEARCH_URL;
            } else {
                category = searchStr.getCategory() + "/q-";
            }
            searchStrEncoded = OlxCategory.OLX_HOST_URL +
                    category +
                    formatSearchStr(searchStrEncoded) +
                    "/";
            return getLinkListBySearchPageLink(searchStrEncoded, searchStr.getInitQuery());
        } catch (UnsupportedEncodingException ex) {
            log.error(classname, ExceptionUtils.getStackTrace(ex));
        }
        List<ExpandedLink> emptyList = new ArrayList<>();
        return emptyList;
    }
    
    private List<ExpandedLink> getLinkListBySearchPageLink(String link, String searchStr) 
            throws IllegalInputValueException { 
        DataChecker.nullCheck(searchStr, "searchStr");
        DataChecker.nullCheck(link, "link");
        String regExpUrl = "https://www\\.olx\\.ua/(.*)";
        DataChecker.regularCheck(link, regExpUrl, "Error input link: " + link);
        
        OlxSearchParser searchParser = new OlxSearchParser();
        List<ExpandedLink> list = new ArrayList<>();
        try {
            String searchContent = getRequest(link);
            Integer amountOfPages =
                    searchParser.getAmountOfPagesFromContent(searchContent);
            
            list.add(new ExpandedLink(link, searchStr));
            if(amountOfPages != null) {
                for(int i=2; i<(amountOfPages + 1); i++) {
                    String resultLink = link + "?page=" + i;
                    list.add(new ExpandedLink(resultLink, searchStr));
                }
            }            
        } catch (GetRequestException ex) {
            log.error(classname, ExceptionUtils.getStackTrace(ex));
        }
        return list;
    }
    
    private String formatSearchStr(String searchStr) {
        Pattern CLEAR_PATTERN = Pattern.compile("[\\s]+");
        return CLEAR_PATTERN.matcher(searchStr).replaceAll("-").trim();
    }
    
    private String getRequest(String queryStr) throws GetRequestException {
        QueryParams parameters = new QueryParams();
        parameters.add(QueryParams.Parameter.URL_STR, queryStr);
        parameters.add(QueryParams.Parameter.HOST_STR, GetRequest.OLX_HOST);
        parameters.add(QueryParams.Parameter.ENCODING_TYPE, "utf-8");
        parameters.add(QueryParams.Parameter.HEADER_ENABLE, true);
            
        QueryResult result = new GetRequest().makeRequest(parameters);
        if(result == null)
            return "";
        return result.getContent();
    }
    
}
