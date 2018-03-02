/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.repository.RAM;

import java.util.Date;
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
public class PhoneNameRepositoryRAMTest {
    
    private final int PHONE_1_ID = 0;
    private final int PHONE_2_ID = 1;
    private final int PHONE_ID_ERROR = 100;
    private final int NAME_1_ID = 0;
    private final int NAME_2_ID = 1;
    private final int NAME_ID_ERROR = 100;
    private final Date DATE_1 = new Date(1000);
    private final Date DATE_2 = new Date(2000);
    private final PhoneNameRepositoryRAM instance = new PhoneNameRepositoryRAM();
    
    public PhoneNameRepositoryRAMTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        instance.add(PHONE_1_ID, NAME_1_ID, DATE_1);
        instance.add(PHONE_2_ID, NAME_2_ID, DATE_2);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getPhoneIdByNameId method, of class PhoneNameRepositoryRAM.
     */
    @Test
    public void testGetPhoneIdByNameId() {
        System.out.println("getPhoneIdByNameId");       
        Integer expResult = 0;
        Integer result = instance.getPhoneIdByNameId(NAME_1_ID);
        assertEquals(expResult, result);       
        expResult = 1;
        result = instance.getPhoneIdByNameId(NAME_2_ID);
        assertEquals(expResult, result);      
        expResult = null;
        result = instance.getPhoneIdByNameId(NAME_ID_ERROR);
        assertEquals(expResult, result);
    }

    /**
     * Test of getNameIdByPhoneId method, of class PhoneNameRepositoryRAM.
     */
    @Test
    public void testGetNameIdByPhoneId() {
        System.out.println("getNameIdByPhoneId");
        Integer expResult = 0;
        Integer result = instance.getNameIdByPhoneId(PHONE_1_ID);
        assertEquals(expResult, result);       
        expResult = 1;
        result = instance.getNameIdByPhoneId(PHONE_2_ID);
        assertEquals(expResult, result);      
        expResult = null;
        result = instance.getNameIdByPhoneId(PHONE_ID_ERROR);
        assertEquals(expResult, result);
    }

    /**
     * Test of add method, of class PhoneNameRepositoryRAM.
     */
    @Test
    public void testAdd() {
        System.out.println("add");
        Integer expResult = 0;
        Integer result = instance.add(PHONE_1_ID, NAME_1_ID, DATE_1);
        assertEquals(expResult, result);
        expResult = 2;
        result = instance.add(PHONE_1_ID, NAME_2_ID, DATE_2);
        assertEquals(expResult, result);
    }
    
}
