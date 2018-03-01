/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler;

import com.apu.olxcrawler.structure.DirectoryTree;
import java.util.List;
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
public class OlxStructureTest {
    
    public OlxStructureTest() {
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
     * Test of getCategoriesList method, of class OlxStructure.
     */
    @Test
    public void testGetCategoriesList() {
        System.out.println("getCategoriesList");
        OlxStructure instance = new OlxStructure();
        int expResult = 2;
        List<String> result = instance.getCategoriesList();
        assertTrue(expResult <= result.size());
    }
    
}
