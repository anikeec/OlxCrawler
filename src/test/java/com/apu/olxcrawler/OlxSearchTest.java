/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler;

import com.apu.olxcrawler.entity.ExpandedLink;
import java.util.List;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author apu
 */
public class OlxSearchTest {
    
    public OlxSearchTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getLinkListBySearchQuery method, of class OlxSearch.
     */
    @Test
    public void testGetLinkListBySearchQuery() {
        System.out.println("getLinkListBySearchQuery");        
        OlxSearch instance = new OlxSearch();
        ExpandedLink searchStr = null;
        try {
            instance.getLinkListBySearchQuery(searchStr);
        } catch(IllegalArgumentException e) {
            assertThat(e.getMessage(), is("searchStr is NULL"));
        }        
        searchStr = new ExpandedLink();
        searchStr.setInitQuery("Книга");
        searchStr.setCategory(OlxCategory.hobbi_otdyh_i_sport);
        List<ExpandedLink> result = instance.getLinkListBySearchQuery(searchStr);
        assertTrue(result.size() > 10);
    }
    
}
