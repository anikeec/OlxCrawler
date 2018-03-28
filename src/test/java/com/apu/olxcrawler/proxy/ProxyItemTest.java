/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.proxy;

import java.util.Date;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author apu
 */
public class ProxyItemTest {
    
    private final String IP_TEST = "1.1.1.1";
    private final String IP_ZERO_TEST = "0.0.0.0";
    private final Integer PORT_TEST = 3128;
    private final Integer PORT_ZERO_TEST = 0;
    private final int VALID_PROXY_AMOUNT = 5;
    
    public ProxyItemTest() {
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
     * Test of getIp method, of class ProxyItem.
     */
    @Test
    public void testGetIp() {
        System.out.println("getIp");
        ProxyItem instance = new ProxyItem(IP_TEST, PORT_TEST);
        String expResult = IP_TEST;
        String result = instance.getIp();
        assertEquals(expResult, result);
        ProxyItem instance1 = null;
        try {
            instance1 = new ProxyItem(null, PORT_TEST);
            assertNull(instance1);
        } catch (NullPointerException ex) {}
        assertNull(instance1);
    }

    /**
     * Test of setIp method, of class ProxyItem.
     */
    @Test
    public void testSetIp() {
        System.out.println("setIp");
        ProxyItem instance = new ProxyItem(IP_TEST, PORT_TEST);
        instance.setIp(IP_ZERO_TEST);
        assertEquals(IP_ZERO_TEST, instance.getIp());
        try {
            instance.setIp(null);
            instance = null;
        } catch (NullPointerException ex) {}
        assertNotNull(instance);
    }

    /**
     * Test of getPort method, of class ProxyItem.
     */
    @Test
    public void testGetPort() {
        System.out.println("getPort");
        ProxyItem instance = new ProxyItem(IP_TEST, PORT_TEST);
        Integer expResult = PORT_TEST;
        Integer result = instance.getPort();
        assertTrue(expResult.intValue() == result);
        ProxyItem instance1 = null;
        try {
            instance1 = new ProxyItem(IP_TEST, null);
            assertNull(instance1);
        } catch (NullPointerException ex) {}
        assertNull(instance1);
    }

    /**
     * Test of setPort method, of class ProxyItem.
     */
    @Test
    public void testSetPort() {
        System.out.println("setPort");
        ProxyItem instance = new ProxyItem(IP_TEST, PORT_TEST);
        instance.setPort(PORT_ZERO_TEST);
        assertTrue(PORT_ZERO_TEST.intValue() == instance.getPort());
        try {
            instance.setPort(null);
            instance = null;
        } catch (NullPointerException ex) {}
        assertNotNull(instance);
    }

    /**
     * Test of isValid method, of class ProxyItem.
     */
    @Test
    public void testIsValid() {
        System.out.println("isValid");
        ProxyItem instance = new ProxyItem(IP_TEST, PORT_TEST);
        boolean result = instance.isValid();
        assertTrue(result);
        
        for(int i=0;i<VALID_PROXY_AMOUNT;i++)
            instance.setInvalid();
        result = instance.isValid();
        assertFalse(result);
        
        instance.setValid();
        result = instance.isValid();
        assertTrue(result);
        
        instance.setInvalid();
        result = instance.isValid();
        assertFalse(result);        
    }

    /**
     * Test of setValid method, of class ProxyItem.
     */
    @Test
    public void testSetValid() {
        System.out.println("setValid");
        ProxyItem instance = new ProxyItem(IP_TEST, PORT_TEST);
        for(int i=0;i<VALID_PROXY_AMOUNT;i++)
            instance.setInvalid();
        boolean result = instance.isValid();
        assertFalse(result);
        
        instance.setValid();
        result = instance.isValid();
        assertTrue(result);        
    }

    /**
     * Test of setInvalid method, of class ProxyItem.
     */
    @Test
    public void testSetInvalid() {
        System.out.println("setInvalid");
        ProxyItem instance = new ProxyItem(IP_TEST, PORT_TEST);
        boolean result = instance.isValid();
        assertTrue(result);
        
        for(int i=0;i<VALID_PROXY_AMOUNT;i++)
            instance.setInvalid();
        result = instance.isValid();
        assertFalse(result);    
    }

    /**
     * Test of isUnused method, of class ProxyItem.
     */
    @Ignore
    @Test
    public void testIsUnused() {
        System.out.println("isUnused");
        ProxyItem instance = new ProxyItem(IP_TEST, PORT_TEST);
        boolean expResult = false;
        boolean result = instance.isUnused();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setUsed method, of class ProxyItem.
     */
    @Ignore
    @Test
    public void testSetUsed() {
        System.out.println("setUsed");
        ProxyItem instance = new ProxyItem(IP_TEST, PORT_TEST);
        instance.setUsed();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of clearUsed method, of class ProxyItem.
     */
    @Ignore
    @Test
    public void testClearUsed() {
        System.out.println("clearUsed");
        ProxyItem instance = new ProxyItem(IP_TEST, PORT_TEST);
        instance.clearUsed();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getTimeBecameInvalid method, of class ProxyItem.
     */
    @Ignore
    @Test
    public void testGetTimeBecameInvalid() {
        System.out.println("getTimeBecameInvalid");
        ProxyItem instance = new ProxyItem(IP_TEST, PORT_TEST);
        Date expResult = null;
        Date result = instance.getTimeBecameInvalid();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setTimeBecameInvalid method, of class ProxyItem.
     */
    @Ignore
    @Test
    public void testSetTimeBecameInvalid() {
        System.out.println("setTimeBecameInvalid");
        Date timeBecameInvalid = null;
        ProxyItem instance = new ProxyItem(IP_TEST, PORT_TEST);
        instance.setTimeBecameInvalid(timeBecameInvalid);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getCountry method, of class ProxyItem.
     */
    @Ignore
    @Test
    public void testGetCountry() {
        System.out.println("getCountry");
        ProxyItem instance = new ProxyItem(IP_TEST, PORT_TEST);
        String expResult = "";
        String result = instance.getCountry();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setCountry method, of class ProxyItem.
     */
    @Ignore
    @Test
    public void testSetCountry() {
        System.out.println("setCountry");
        String country = "";
        ProxyItem instance = new ProxyItem(IP_TEST, PORT_TEST);
        instance.setCountry(country);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of getDelay method, of class ProxyItem.
     */
    @Ignore
    @Test
    public void testGetDelay() {
        System.out.println("getDelay");
        ProxyItem instance = new ProxyItem(IP_TEST, PORT_TEST);
        Integer expResult = null;
        Integer result = instance.getDelay();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of setDelay method, of class ProxyItem.
     */
    @Ignore
    @Test
    public void testSetDelay() {
        System.out.println("setDelay");
        Integer delay = null;
        ProxyItem instance = new ProxyItem(IP_TEST, PORT_TEST);
        instance.setDelay(delay);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    
}
