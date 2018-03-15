/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.repository.RAM;

import com.apu.olxcrawler.repository.entity.UserName;
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
public class NameRepositoryRAMTest {
    
    private final String NAME_1 = "Name1";
    private final String NAME_2 = "Name2";
    private final String NAME_ERROR = "Name100";
    private final NameRepositoryRAM instance = new NameRepositoryRAM();
    
    public NameRepositoryRAMTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        instance.add(NAME_1);
        instance.add(NAME_2);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getNameById method, of class NameRepositoryRAM.
     */
    @Test
    public void testGetNameById() {
        System.out.println("getNameById");        
        Integer id = 0;        
        String expResult = NAME_1;
        UserName result = instance.getNameById(id);
        assertEquals(expResult, result.getName());
        id = 1;        
        expResult = NAME_2;
        result = instance.getNameById(id);
        assertEquals(expResult, result.getName());
        id = 100;        
        expResult = null;
        result = instance.getNameById(id);
        assertEquals(expResult, result);
    }

    /**
     * Test of getIdByNameValue method, of class NameRepositoryRAM.
     */
    @Test
    public void testGetIdByNameValue() {
        System.out.println("getIdByNameValue");
        String name = NAME_1;
        Integer expResult = 0;
        Integer result = instance.getIdByNameValue(name);
        assertEquals(expResult, result);
        name = NAME_2;
        expResult = 1;
        result = instance.getIdByNameValue(name);
        assertEquals(expResult, result);
        name = NAME_ERROR;
        expResult = null;
        result = instance.getIdByNameValue(name);
        assertEquals(expResult, result);
    }
    
}
