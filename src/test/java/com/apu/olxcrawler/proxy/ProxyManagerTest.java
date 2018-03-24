/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.olxcrawler.proxy;

import com.apu.olxcrawler.query.GetRequestException;
import java.util.ArrayList;
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
public class ProxyManagerTest {
    
    private ProxyManager proxyManager;
    
    public ProxyManagerTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        this.proxyManager = new ProxyManager();
        ProxyItem proxyItem = new ProxyItem("1.1.1.1", 3128);
        this.proxyManager.add(proxyItem);
    }
    
    @After
    public void tearDown() {
        this.proxyManager = null;
    }

    /**
     * Test of getInstance method, of class ProxyManager.
     */
    @Test
    public void testGetInstance() {
        System.out.println("getInstance");
        ProxyManager result = ProxyManager.getInstance();
        assertNull(result);
        ProxyManager.setInstance(new ProxyManager());
        result = ProxyManager.getInstance();
        assertNotNull(result);
        assertTrue(result instanceof ProxyManager);
    }

    /**
     * Test of take method, of class ProxyManager.
     */
    @Test
    public void testTake() throws GetRequestException {
        System.out.println("take");
        ProxyItem item1 = this.proxyManager.take();
        assertNotNull(item1);
        assertFalse(item1.isUnused());
        ProxyItem item2 = this.proxyManager.take();
        assertNull(item2);
        item1.setInvalid();
        this.proxyManager.put(item1);
        ProxyItem item3 = this.proxyManager.take();
        assertNull(item3);
        
        item1.setValid();
        this.proxyManager.put(item1);
        ProxyItem item4 = null;
        try {
            try {
                item4 = this.proxyManager.take();
                assertNotNull(item4);
                throw new GetRequestException("gerRequestError.");
            } catch (GetRequestException ex) {
                item4.setInvalid();
                throw new GetRequestException("gerRequestError.", ex);
            } finally {
                this.proxyManager.put(item4);
                ProxyItem item5 = this.proxyManager.take();
                assertNull(item5);
            } 
        } catch (GetRequestException ex) {
            item4.setValid();
        } finally {
            assertTrue(item4.isValid());
        }
        
        this.proxyManager.add(new ProxyItem("2.2.2.2", 3128));        
        ProxyItem prevItem = this.proxyManager.take();
        this.proxyManager.put(prevItem);
        ProxyItem nextItem = this.proxyManager.take();
        assertFalse(prevItem == nextItem);
    }

    /**
     * Test of put method, of class ProxyManager.
     */
    @Test
    public void testPut() {
        System.out.println("put");
        ProxyItem item = this.proxyManager.take();
        assertFalse(item.isUnused());
        this.proxyManager.put(item);
        assertTrue(item.isUnused());
    }

    /**
     * Test of add method, of class ProxyManager.
     */
    @Test
    public void testAdd_ProxyItem() {
        System.out.println("add");
        ProxyItem item = new ProxyItem("2.2.2.2", 3128);
        int value1 = this.proxyManager.getProxyListSize();
        this.proxyManager.add(item);
        int value2 = this.proxyManager.getProxyListSize();
        assertTrue(value2 == (value1 + 1));
        item = new ProxyItem(null, null);
        this.proxyManager.add(item);
        int value3 = this.proxyManager.getProxyListSize();
        assertTrue(value3 == value2);
    }

    /**
     * Test of add method, of class ProxyManager.
     */
    @Test
    public void testAdd_List() {
        System.out.println("add");
        List<ProxyItem> itemList = new ArrayList<>();
        itemList.add(new ProxyItem("1.1.1.2", 3128));
        itemList.add(new ProxyItem("1.1.1.3", 3128));
        int value1 = this.proxyManager.getProxyListSize();        
        this.proxyManager.add(itemList);
        int value2 = this.proxyManager.getProxyListSize();
        assertTrue(value2 == (value1 + 2));
        this.proxyManager.add(itemList);
        int value3 = this.proxyManager.getProxyListSize();
        assertTrue(value2 == value3);
    }

    /**
     * Test of getProxyListSize method, of class ProxyManager.
     */
    @Test
    public void testGetProxyListSize() {
        System.out.println("getProxyListSize");
        int expResult = 0;
        int result = this.proxyManager.getProxyListSize();
        assertTrue(result > expResult);
    }
    
}
