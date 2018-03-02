/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.repository.RAM;

import com.apu.olxcrawler.repository.entity.PhoneEntity;
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
public class PhoneRepositoryRAMTest {
    
    private final String PHONE_1 = "0501234567";
    private final String PHONE_2 = "0631234567";
    private final String PHONE_ERROR = "0000000000";
    private final PhoneRepositoryRAM instance = new PhoneRepositoryRAM();
    
    public PhoneRepositoryRAMTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        instance.add(PHONE_1);
        instance.add(PHONE_2);
    }
    
    @After
    public void tearDown() {
    }

    /**
     * Test of getPhoneById method, of class PhoneRepositoryRAM.
     */
    @Test
    public void testGetPhoneById() {
        System.out.println("getPhoneById");
        Integer id = 0;        
        String expResult = PHONE_1;
        PhoneEntity result = instance.getPhoneById(id);
        assertEquals(expResult, result.getPhone());
        id = 1;        
        expResult = PHONE_2;
        result = instance.getPhoneById(id);
        assertEquals(expResult, result.getPhone());
        id = 100;        
        expResult = null;
        result = instance.getPhoneById(id);
        assertEquals(expResult, result);
    }

    /**
     * Test of getIdByPhoneNumber method, of class PhoneRepositoryRAM.
     */
    @Test
    public void testGetIdByPhoneNumber() {
        System.out.println("getIdByPhoneNumber");
        String name = PHONE_1;
        Integer expResult = 0;
        Integer result = instance.getIdByPhoneNumber(name);
        assertEquals(expResult, result);
        name = PHONE_2;
        expResult = 1;
        result = instance.getIdByPhoneNumber(name);
        assertEquals(expResult, result);
        name = PHONE_ERROR;
        expResult = null;
        result = instance.getIdByPhoneNumber(name);
        assertEquals(expResult, result);
    }

    
}
